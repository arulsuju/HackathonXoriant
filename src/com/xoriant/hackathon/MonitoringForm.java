package com.xoriant.hackathon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class MonitoringForm extends JDialog {
	private static final long serialVersionUID = 3047495414058599805L;

	public MonitoringForm(JDialog parent) {
		super(parent,false);
		toInitialize();
		setInitialize();
	}

	private void setScreenSize() {
		
		setBounds(25, localSysDimension.height - 300,
				localSysDimension.width - 50, 250);
	}

	private void setInitialize() {
		getContentPane().setBackground(new Color(154, 188, 245));
		getRootPane().setBorder(new LineBorder(Color.black));
		setUndecorated(true);
		setScreenSize();
		setVisible(true);
	}

	private void toInitialize() {
		localSysDimension = Toolkit.getDefaultToolkit().getScreenSize();
		JPanel panelView = new JPanel();
		panelView.setBackground(new Color(222, 240, 255));
		panelView.setBorder(new BevelBorder(0));
		panelView.setLayout(null);
		
		txtInforamtion = new JTextArea();
		txtInforamtion.setAutoscrolls(true);
		scrollText = new JScrollPane(txtInforamtion);
		scrollText.setBackground(new Color(155,205,255));
		
		panelView.add(scrollText);
		txtInforamtion.setBackground(Color.black);
		txtInforamtion.setForeground(Color.green);
		
		TitledBorder border = new TitledBorder("Server Monitor Information");
		border.setTitleColor(Color.black);
		scrollText.setBounds(10, 10,localSysDimension.width - 92, 210);
		
		
		getContentPane().setLayout(null);
		getContentPane().add(panelView);
		panelView.setBounds(10, 10, localSysDimension.width - 70, 230);
	}
	JScrollPane scrollText;
	public Dimension localSysDimension = null;
	public static JTextArea txtInforamtion;
}
