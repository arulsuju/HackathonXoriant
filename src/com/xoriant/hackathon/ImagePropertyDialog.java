package com.xoriant.hackathon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ImagePropertyDialog extends JDialog {
	private static final long serialVersionUID = 5198182698268909531L;

	public ImagePropertyDialog(JFrame parentFrame) {
		super(parentFrame, true);
		toIntialize();

		setInitialize();
	}

	private void setScreenSize() {
		localSysDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((localSysDimension.width - 410) / 2,
				(localSysDimension.height - 600) / 2, 410, 500);
	}

	private void setInitialize() {
		getContentPane().setBackground(new Color(154, 188, 245));
		getRootPane().setBorder(new LineBorder(Color.black));
		setUndecorated(true);
		setScreenSize();
		setImage();
		setVisible(true);
		
	}

	private void toIntialize() {
		JPanel panelView = new JPanel();
		panelView.setBackground(new Color(222, 240, 255));
		panelView.setBorder(new BevelBorder(0));
		panelView.setLayout(null);

		labelImage = new JLabel();
		labelImage.setBorder(new TitledBorder("Image"));

	
		//labelHCm = new JLabel("START : "+CalcDiffernceHeight.);
		//labelInch = new JLabel("START : "+CalcDiffernceHeight.end);

		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panelView.add(labelImage);
		panelView.add(labelStart);
		panelView.add(labelEnd);
		panelView.add(labelHeight);
		//panelView.add(labelHCm);
		//panelView.add(labelInch);
		panelView.add(btnClose);

		labelImage.setBounds(10, 10, 370, 270);

		labelStart.setBounds(30, 290, 370, 30);
		labelEnd.setBounds(30, 320, 370, 30);
		labelHeight.setBounds(30, 350, 370, 30);
		//labelHCm.setBounds(30, 380, 370, 30);
		//labelInch.setBounds(30, 410, 370, 30);

		btnClose.setBounds(150, 450, 90, 20);

		getContentPane().setLayout(null);
		getContentPane().add(panelView);
		panelView.setBounds(10, 10, 390, 480);

	}

	private void setImage() {
		try {
			BufferedImage imageBuffer = ImageIO.read(new File("testx.bmp"));
			labelImage.setIcon(new ImageIcon(imageBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Dimension localSysDimension = null;
	JLabel labelImage;
	JLabel labelStart, labelEnd, labelHeight, labelHCm, labelInch;
	JButton btnClose;

}
