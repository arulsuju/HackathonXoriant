package com.xoriant.hackathon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MovingObjectsMain extends JFrame {
	private static final long serialVersionUID = -2965130131833530502L;

	public static void main(String[] args) {
		new MovingObjectsMain();
	}

	public MovingObjectsMain() {
		parentSoftMain = this;
		toInitialize();
		setInitialize();
		HomePageOptions pageOptions =  new HomePageOptions(this);
		new MonitoringForm(pageOptions);
		
	
	}

	private void setScreenSize() {
		localSysDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(localSysDimension.width, localSysDimension.height - 30);
	}

	private void setInitialize() {
		setTitle("Moving Objects Detection");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(deskView);
		setResizable(false);
		setScreenSize();
		setVisible(true);
	}

	private void toInitialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		deskView = new JDesktopPane();
		deskView.setBackground(new Color(154, 188, 245));
	}

	public static JDesktopPane deskView = null;
	public Dimension localSysDimension = null;
	public static MovingObjectsMain parentSoftMain = null;
}
