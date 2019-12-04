package com.xoriant.hackathon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.MediaLocator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class HomePageOptions extends JDialog {
	private static final long serialVersionUID = 8060247584951760912L;

	JPanel panelView = null;
	
	public HomePageOptions(JFrame parentFrame) {
		super(parentFrame, false);
		toInitialize();
		setInitialize();
	}

	private void setScreenSize() {
		localSysDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((localSysDimension.width - 1000) / 2,
				(localSysDimension.height - 660) / 2, 1000, 670);
	}

	private void setInitialize() {
		setUndecorated(true);
		getRootPane().setBorder(new LineBorder(Color.black));
		setButtonsInitialize();
		setScreenSize();
		setVisible(true);
	}

	private void setButtonsInitialize() {
		JToolBar panelBtn = new JToolBar();
		panelBtn.setLayout(new GridLayout());
		panelBtn.setFloatable(false);
		panelBtn.setBackground(new Color(222, 240, 255));
		btnHome = new JButton("Home");
		btnHome.setBackground(new Color(154, 188, 245));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnSplit = new JButton("Moving Objects");
		btnSplit.setBackground(new Color(154, 188, 245));
		btnSplit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SelectOptionsForm(MovingObjectsMain.parentSoftMain);
			}
		});

		btnMake = new JButton("Make Video");
		btnMake.setBackground(new Color(154, 188, 245));
		btnMake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int width = 350, height = 242, frameRate = 5;
				Vector<String> inputFiles = new Vector<String>();
				Hashtable<Integer, String> fileName = new Hashtable<Integer, String>();
				String outputURL = "Video.mov";

				File file = new File("records");
				File imgeFile[] = file.listFiles();
				int j = 0;
				System.out.println(imgeFile.length);
				for (j = 0; j < imgeFile.length - 1; j++) {
					int value = Integer.parseInt(imgeFile[j].getName().split(
							"\\.")[0]);
					fileName.put(value, imgeFile[j].getAbsolutePath());
				}
				Enumeration<Integer> fileKeys = fileName.keys();
				Integer[] intValue = new Integer[j];
				int k = 0;
				while (fileKeys.hasMoreElements()) {
					intValue[k] = fileKeys.nextElement();
					k++;
				}
				Arrays.sort(intValue);
				inputFiles = new Vector<String>();
				for (j = 0; j < intValue.length; j++) {
					inputFiles.addElement(fileName.get(intValue[j]));
					System.out.println(fileName.get(intValue[j]));
				}
				MediaLocator oml;

				if ((oml = createMediaLocator(outputURL)) == null) {
					System.err.println("Cannot build media locator from: "
							+ outputURL);
					System.exit(0);
				}
				JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
				imageToMovie.doIt(width, height, frameRate, inputFiles, oml);
			}
		});

	 

		btnExit = new JButton("Exit");
		btnExit.setBackground(new Color(154, 188, 245));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		panelBtn.add(btnHome);
		panelBtn.add(btnSplit);
		panelBtn.add(btnMake);
		//panelBtn.add(btnHeight);
		panelBtn.add(btnExit);
		getContentPane().add(panelBtn, "North");
	}

	static MediaLocator createMediaLocator(String url) {

		MediaLocator mediaLocator;

		if (url.indexOf(":") > 0
				&& (mediaLocator = new MediaLocator(url)) != null)
			return mediaLocator;

		if (url.startsWith(File.separator)) {
			if ((mediaLocator = new MediaLocator("file:" + url)) != null)
				return mediaLocator;
		} else {
			String file = "file:" + System.getProperty("user.dir")
					+ File.separator + url;
			if ((mediaLocator = new MediaLocator(file)) != null)
				return mediaLocator;
		}

		return null;
	}

	private void toInitialize() {

		panelView = new JPanel();

		panelView.setBorder(new BevelBorder(0));
		panelView.setLayout(null);
		panelView.setBackground(new Color(222, 240, 255));

		getContentPane().add(panelView, "Center");
	}

	public Dimension localSysDimension = null;
	JButton btnHome, btnSplit, btnMake, btnHeight, btnExit;

}
