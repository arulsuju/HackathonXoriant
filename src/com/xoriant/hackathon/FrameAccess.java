package com.xoriant.hackathon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.stream.ImageOutputStream;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.UnsupportedPlugInException;
import javax.media.control.TrackControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;

public class FrameAccess implements ControllerListener {
	String path;

	public FrameAccess(String path) {
		this.path = path;

	}

	Processor processor;
	Object waitSync = new Object();
	boolean stateTransitionOK = true;

	public boolean open(DataSource ml) {

		try {
			processor = Manager.createProcessor(ml);
		} catch (Exception e) {
			System.err
					.println("Failed to create a processor from the given url: "
							+ e);
			return false;
		}

		processor.addControllerListener(this);

		// Put the Processor into configured state.
		processor.configure();
		if (!waitForState(Processor.Configured)) {
			System.err.println("Failed to configure the processor.");
			return false;
		}

		// So I can use it as a player.
		processor.setContentDescriptor(null);

		// Obtain the track controls.
		TrackControl tc[] = processor.getTrackControls();

		if (tc == null) {
			System.err
					.println("Failed to obtain track controls from the processor.");
			return false;
		}

		// Search for the track control for the video track.
		TrackControl videoTrack = null;

		for (int i = 0; i < tc.length; i++) {
			if (tc[i].getFormat() instanceof VideoFormat) {
				videoTrack = tc[i];
				break;
			}
		}

		if (videoTrack == null) {
			System.err
					.println("The input media does not contain a video track.");
			return false;
		}

		System.err.println("Video format: " + videoTrack.getFormat());

		// Instantiate and set the frame access codec to the data flow path.
		try {
			Codec codec[] = { new PreAccessCodec(), new PostAccessCodec(path) };
			videoTrack.setCodecChain(codec);
		} catch (UnsupportedPlugInException e) {
			System.err.println("The process does not support effects.");
		}

		// Realize the processor.
		processor.prefetch();
		if (!waitForState(Processor.Prefetched)) {
			System.err.println("Failed to realize the processor.");
			return false;
		}

		processor.start();

		return true;
	}

	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (processor.getState() != state && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	/**
	 * Controller Listener.
	 */
	public void controllerUpdate(ControllerEvent evt) {

		if (evt instanceof ConfigureCompleteEvent
				|| evt instanceof RealizeCompleteEvent
				|| evt instanceof PrefetchCompleteEvent) {
			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) {
			processor.close();

		}

		/**
		 * Main program
		 */
	}

	public void filetograb(DataSource dt) {
		DataSource data = dt;
		FrameAccess fa = new FrameAccess(path);

		if (!fa.open(data))
			System.exit(0);
	}

	static void prUsage() {
		System.err.println("Usage: java FrameAccess <url>");
	}

	/***************************************************************************
	 * Inner class.
	 * 
	 * A pass-through codec to access to individual frames.
	 **************************************************************************/

	public class PreAccessCodec implements Codec {

		/**
		 * Callback to access individual video frames.
		 */
		void accessFrame(Buffer frame) {

			// we'll just print out the frame #, time &
			// data length.

			long t = (long) (frame.getTimeStamp() / 10000000f);

			System.err.println("Pre: frame #: " + frame.getSequenceNumber()
					+ ", time: " + ((float) t) / 100f + ", len: "
					+ frame.getLength());
		}

		/**
		 * The code for a pass through codec.
		 */

		// We'll advertize as supporting all video formats.
		protected Format supportedIns[] = new Format[] { new VideoFormat(null) };

		// We'll advertize as supporting all video formats.
		protected Format supportedOuts[] = new Format[] { new VideoFormat(null) };

		Format input = null, output = null;

		public String getName() {
			return "Pre-Access Codec";
		}

		// No op.
		public void open() {
		}

		// No op.
		public void close() {
		}

		// No op.
		public void reset() {
		}

		public Format[] getSupportedInputFormats() {
			return supportedIns;
		}

		public Format[] getSupportedOutputFormats(Format in) {
			if (in == null)
				return supportedOuts;
			else {
				// If an input format is given, we use that input format
				// as the output since we are not modifying the bit stream
				// at all.
				Format outs[] = new Format[1];
				outs[0] = in;
				return outs;
			}
		}

		public Format setInputFormat(Format format) {
			input = format;
			return input;
		}

		public Format setOutputFormat(Format format) {
			output = format;
			return output;
		}

		public int process(Buffer in, Buffer out) {

			// This is the "Callback" to access individual frames.
			accessFrame(in);

			// Swap the data between the input & output.
			Object data = in.getData();
			in.setData(out.getData());
			out.setData(data);

			// Copy the input attributes to the output
			out.setFormat(in.getFormat());
			out.setLength(in.getLength());
			out.setOffset(in.getOffset());

			return BUFFER_PROCESSED_OK;
		}

		public Object[] getControls() {
			return new Object[0];
		}

		public Object getControl(String type) {
			return null;
		}
	}

	public class PostAccessCodec extends PreAccessCodec {
		// We'll advertize as supporting all video formats.
		int check = new File("Images").listFiles().length;
		String path1;

		public PostAccessCodec(String path) {
			this.path1 = path;
			supportedIns = new Format[] { new RGBFormat() };
		}

		/**
		 * Callback to access individual video frames.
		 */
		void accessFrame(Buffer frame) {

			// For demo, we'll just print out the frame #, time &
			// data length.

			BufferToImage stopBuffer = new BufferToImage((VideoFormat) frame
					.getFormat());
			Image stopImage = stopBuffer.createImage(frame);
			try {
				BufferedImage outImage = new BufferedImage(352, 240,
						BufferedImage.TYPE_INT_RGB);
				Graphics og = outImage.getGraphics();
				og.drawImage(stopImage, 0, 0, 352, 240, null);
				// prepareImage(outImage,rheight,rheight, null);
				Iterator<?> writers = ImageIO
						.getImageWritersByFormatName("jpg");
				ImageWriter writer = (ImageWriter) writers.next();

				// Once an ImageWriter has been obtained, its destination must
				// be set to an ImageOutputStream:
				ImageOutputStream ios = null;

				File f = new File("Images/" + check + ".jpg");
				ios = ImageIO.createImageOutputStream(f);
				writer.setOutput(ios);

				check++;

				// Add writer listener to prevent the program from becoming out
				// of memory
				writer
						.addIIOWriteProgressListener(new IIOWriteProgressListener() {
							public void imageStarted(ImageWriter source,
									int imageIndex) {
								System.out.println("Started");
							}

							public void imageProgress(ImageWriter source,
									float percentageDone) {
								System.out.println("Processed");
							}

							public void imageComplete(ImageWriter source) {
								source.dispose();
							}

							public void thumbnailStarted(ImageWriter source,
									int imageIndex, int thumbnailIndex) {
							}

							public void thumbnailProgress(ImageWriter source,
									float percentageDone) {
							}

							public void thumbnailComplete(ImageWriter source) {
							}

							public void writeAborted(ImageWriter source) {
								System.out.println("Aborted");
							}
						});

				writer.write(outImage);
				ios.close();
				BufferedImage image = ImageIO.read(new File("Images/"
						+ (check - 1) + ".jpg"));
				BufferedImage orignalImage = ImageIO.read(new File(
						"original.jpg"));
				 
				double value = new ImageCompare().getAverageValue(image,
						orignalImage);
				if (value < 25) {
					new File("Images/" + (check - 1) + ".jpg").delete();
				} else {
					MonitoringForm.txtInforamtion.append("\r\n Start Value : Image Not Matched " + value);
					System.out.println("Image Not Matched " + value);
					File rec = new File("Images/" + (check - 1) + ".jpg");
					File newfile = new File("records/" + (check - 1) + ".jpg");
					rec.renameTo(newfile);

				}

			} catch (IOException e) {
				System.out.println("Error :" + e);
			}

			long t = (long) (frame.getTimeStamp() / 10000000f);
			MonitoringForm.txtInforamtion.append("\r\n Post: frame #: " + frame.getSequenceNumber()
					+ ", time: " + ((float) t) / 100f + ", len: "
					+ frame.getLength());
			System.err.println("Post: frame #: " + frame.getSequenceNumber()
					+ ", time: " + ((float) t) / 100f + ", len: "
					+ frame.getLength());
		}

		public String getName() {
			return "Post-Access Codec";
		}
	}
}
