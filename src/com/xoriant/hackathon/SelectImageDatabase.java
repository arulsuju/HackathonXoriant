package com.xoriant.hackathon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class SelectImageDatabase extends JDialog {
	private static final long serialVersionUID = -590215018861229192L;
	JFrame parentFrame = null;

	public SelectImageDatabase(JFrame parent) {
		super(parent, true);
		parentFrame = parent;
		toInitialize();
		setInitialize();
	}

	private void setScreenSize() {

		localSystemDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((localSystemDimension.width - 900) / 2,
				(localSystemDimension.height - 500) / 2, 900, 500);

	}

	private void setInitialize() {
		getContentPane().setBackground(new Color(154, 188, 245));
		getRootPane().setBorder(new LineBorder(Color.black));
		setUndecorated(true);
		setResizable(false);
		setScreenSize();
		loadFiles();
		setVisible(true);
	}

	private void loadFiles() {
		try {

			File fileData[] = new File("Images").listFiles();
			for (int i = 0; i < 25; i++) {
				fileList.add(fileData[i].getAbsolutePath());
				BufferedImage image = ImageIO.read(fileData[i]);
				modelImage.addElement(image);
				BufferedImage scaledImage = getScaledImage(image, 100);
				thumbnails.add(scaledImage);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toInitialize() {
		panelView = new JPanel();
		panelView.setBackground(new Color(222, 240, 255));
		panelView.setBorder(new BevelBorder(0));
		panelView.setLayout(null);
		thumbnails = new ArrayList<BufferedImage>();
		fileList = new ArrayList<String>();
		fileName = new ArrayList<String>();

		modelImage = new DefaultListModel();
		listImage = new JList(modelImage);
		scrollImage = new JScrollPane();
		listImage.setCellRenderer(new CellRenderer());
		listImage.setFixedCellHeight(140);
		listImage.setFixedCellWidth(120);
		listImage.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listImage.setSelectionBackground(new Color(180, 213, 255));
		listImage.setSelectionBackground(new Color(255,221,110));
		listImage.setSelectionBackground(new Color(229,232,234));
		
		listImage.setVisibleRowCount(0);

		scrollImage.setViewportView(listImage);
		btnNext = new JButton("Next");
		btnClose = new JButton("Close");

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
				try {
					Thread.sleep(5000);
				}catch (Exception ex) {
					ex.printStackTrace();
				}
				new ViewImageDatabase(MovingObjectsMain.parentSoftMain);
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		panelView.add(scrollImage);
		panelView.add(btnNext);
		panelView.add(btnClose);

		scrollImage.setBounds(10, 10, 700, 450);
		btnNext.setBounds(740, 200, 100, 20);
		btnClose.setBounds(740, 230, 100, 20);

		getContentPane().setLayout(null);
		getContentPane().add(panelView);
		panelView.setBounds(10, 10, 878, 478);
	}

	public static int getTotalCount() {
		return thumbnails.size();
	}

	public static ImageIcon getIcon() {
		return new ImageIcon(thumbnails.get(selectIndex));
	}

	public static BufferedImage getScaledImage(BufferedImage originalImage,
			int sideLength) {
		BufferedImage newImage = new BufferedImage(sideLength, sideLength,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, sideLength, sideLength);
		g.drawImage(originalImage, 0, 0, sideLength, sideLength, null);

		return newImage;
	}

	class CellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -1428144534320920928L;

		public Component getListCellRendererComponent(JList list, Object value,

		int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			setText("");
			setVerticalTextPosition(JTextField.BOTTOM);
			setHorizontalTextPosition(JTextField.CENTER);
			setHorizontalAlignment(JTextField.CENTER);
			setIcon(new ImageIcon(thumbnails.get(index)));
			return this;
		}

	}

	public static Dimension localSystemDimension = null;
	public static ArrayList<BufferedImage> thumbnails = null;
	public static ArrayList<String> fileList = null;
	public static ArrayList<String> fileName = null;

	JList listImage;
	DefaultListModel modelImage;
	JScrollPane scrollImage;
	JPanel panelView = null;
	JButton btnNext, btnClose;
	public static String databaseImage = null;
	public static int selectIndex = 0;

}
