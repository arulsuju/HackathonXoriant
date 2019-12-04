package com.xoriant.hackathon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.protocol.DataSource;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class SelectOptionsForm extends JDialog {
	private static final long serialVersionUID = 964822404272137154L;

	public SelectOptionsForm(JFrame parentFrame) {
		super(parentFrame, true);
		toInitialzie();
		setInitialize();

	}

	private void setScreenSize() {
		localSysDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((localSysDimension.width - 740) / 2,
				(localSysDimension.height - 550) / 2, 740, 535);
	}

	private void setInitialize() {
		getContentPane().setBackground(new Color(154, 188, 245));
		getRootPane().setBorder(new LineBorder(Color.black));
		setUndecorated(true);
		setScreenSize();
		setVisible(true);
	}

	private void createPlayer() {
		if (videoFile == null)
			return;

		try {
			MediaLocator mediaLocator = new MediaLocator(txtLocation.getText());
			mediaSources[0] = javax.media.Manager.createDataSource(mediaLocator);
			dataSourceManagement = javax.media.Manager.createMergingDataSource(mediaSources);
			videoPlayer = Manager.createPlayer(dataSourceManagement);
			videoPlayer.addControllerListener(new EventHandler());
			videoPlayer.start();
			

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Invalid file or location",
					"Error loading file", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void toInitialzie() {
		panelView = new JPanel();
		panelView.setBackground(new Color(222, 240, 255));
		panelView.setBorder(new BevelBorder(0));
		panelView.setLayout(null);

		labelLocation = new JLabel("File Location : ");
		txtLocation = new JTextField(
				"vfw:Microsoft WDM Image Capture (Win 32):0");
		btnCapture = new JButton("Capture");

		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPlayer();
			}
		});
		btnNext = new JButton("Next");
		btnClose = new JButton("Close");
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPlayer();
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						while (this != null) {
							FrameAccess access = new FrameAccess("");
							access.filetograb(dataSourceManagement);
							try {
								Thread.sleep(2000);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				}.start();
				/*new ReadFromVideo("file:///" + txtLocation.getText());
				try {
					Thread.sleep(5000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}*/
				new SelectImageDatabase(MovingObjectsMain.parentSoftMain);
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panelView.add(labelLocation);
		panelView.add(txtLocation);
		panelView.add(btnCapture);
		panelView.add(btnNext);
		panelView.add(btnClose);

		labelLocation.setBounds(20, 20, 100, 20);
		txtLocation.setBounds(130, 20, 450, 20);
		btnCapture.setBounds(600, 20, 80, 20);
		btnNext.setBounds(600, 150, 90, 20);
		btnPlay.setBounds(600, 180, 90, 20);
		btnClose.setBounds(600, 210, 90, 20);

		getContentPane().setLayout(null);
		getContentPane().add(panelView);
		panelView.setBounds(10, 10, 720, 515);
	}

	private class EventHandler implements ControllerListener {
		public void controllerUpdate(ControllerEvent e) {
			if (e instanceof RealizeCompleteEvent) {
				Component visualComponent = (Component) videoPlayer
						.getVisualComponent();
				panelVideo = new JPanel();
				panelVideo.setBorder(new LineBorder(Color.black));
				panelVideo.setBackground(new Color(154, 188, 245));
				panelVideo.setLayout(null);
				if (visualComponent != null) {
					panelVideo.add(visualComponent);
					panelView.add(panelVideo);
					panelVideo.setBounds(20, 70, 450, 400);
					visualComponent.setBounds(10, 10, 430, 380);

				}

			}
		}
	}

	JLabel labelLocation;
	JTextField txtLocation;
	JButton btnCapture, btnClose, btnNext, btnPlay;
	public Dimension localSysDimension = null;
	public Player videoPlayer;
	DataSource mediaSources[] = new DataSource[1];
	public JPanel panelView = null, panelVideo = null;
	public File videoFile = new File("mpeg.mpg");
	DataSource dataSourceManagement;
}
