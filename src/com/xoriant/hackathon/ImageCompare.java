package com.xoriant.hackathon;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageCompare {
	public ImageCompare() {

	}

	public double getAverageValue(BufferedImage image, BufferedImage oimage) {
		BufferedImage image1 = oimage;

        double finalValue = 0.0;

		try {
			int width = image.getWidth();
			int height = image.getHeight();
			int pixel[] = new int[height * width];
			int[] rgbArray = image.getRGB(0, 0, width, height, pixel, 0, width);
			double avgRed = 0;
			double stdDeviationRed = 0;
			double avgBlue = 0;
			double stdDeviationBlue = 0;
			double avgGreen = 0;
			double stdDeviationGreen = 0;

			for (int j = 0; j < rgbArray.length; j++) {
				int blueValue = rgbArray[j] & 0xFF;
				avgBlue += blueValue;

				int redValue = (rgbArray[j] >> 16) & 0xFF;
				avgRed += redValue;

				int greenValue = (rgbArray[j] >> 8) & 0xFF;
				avgGreen += greenValue;

			}

			avgRed /= rgbArray.length;
			avgBlue /= rgbArray.length;
			avgGreen /= rgbArray.length;

			for (int k = 0; k < rgbArray.length; k++) {
				double numberBlue = (rgbArray[k] & 0xFF) - avgBlue;
				stdDeviationBlue += numberBlue * numberBlue;

				double numberRed = ((rgbArray[k] >> 16) & 0xFF) - avgRed;
				stdDeviationRed += numberRed * numberRed;

				double numberGreen = ((rgbArray[k] >> 8) & 0xFF) - avgGreen;
				stdDeviationGreen += numberGreen * numberGreen;
			}

			stdDeviationRed = Math.sqrt(stdDeviationRed / rgbArray.length);
			stdDeviationBlue = Math.sqrt(stdDeviationBlue / rgbArray.length);
			stdDeviationGreen = Math.sqrt(stdDeviationGreen / rgbArray.length);

			int width1 = image1.getWidth();
			int height1 = image1.getHeight();
			int pixel1[] = new int[height1 * width1];
			int[] rgbArray1 = image1.getRGB(0, 0, width1, height1, pixel1, 0,
					width1);
			double avgRed1 = 0;
			double stdDeviationRed1 = 0;
			double avgBlue1 = 0;
			double stdDeviationBlue1 = 0;
			double avgGreen1 = 0;
			double stdDeviationGreen1 = 0;

			for (int j = 0; j < rgbArray1.length; j++) {
				int blueValue1 = rgbArray1[j] & 0xFF;
				avgBlue1 += blueValue1;

				int redValue1 = (rgbArray1[j] >> 16) & 0xFF;
				avgRed1 += redValue1;

				int greenValue1 = (rgbArray1[j] >> 8) & 0xFF;
				avgGreen1 += greenValue1;

			}

			avgRed1 /= rgbArray1.length;
			avgBlue1 /= rgbArray1.length;
			avgGreen1 /= rgbArray1.length;

			for (int k = 0; k < rgbArray1.length; k++) {
				double numberBlue1 = (rgbArray1[k] & 0xFF) - avgBlue1;
				stdDeviationBlue1 += numberBlue1 * numberBlue1;

				double numberRed1 = ((rgbArray1[k] >> 16) & 0xFF) - avgRed1;
				stdDeviationRed1 += numberRed1 * numberRed1;

				double numberGreen1 = ((rgbArray1[k] >> 8) & 0xFF) - avgGreen1;
				stdDeviationGreen1 += numberGreen1 * numberGreen1;
			}

			stdDeviationRed1 = Math.sqrt(stdDeviationRed1 / rgbArray1.length);
			stdDeviationBlue1 = Math.sqrt(stdDeviationBlue1 / rgbArray1.length);
			stdDeviationGreen1 = Math.sqrt(stdDeviationGreen1
					/ rgbArray1.length);

			double rDiff = Math.pow(avgRed - avgRed1, 2);
			double bDiff = Math.pow(avgBlue - avgBlue1, 2);
			double gDiff = Math.pow(avgGreen - avgGreen1, 2);
			double drDiff = Math.pow(stdDeviationRed - stdDeviationRed1, 2);
			double dbDiff = Math.pow(stdDeviationBlue - stdDeviationBlue1, 2);
			double dgDiff = Math.pow(stdDeviationGreen - stdDeviationGreen, 2);
			double differenceValue = Math.sqrt(rDiff + bDiff + gDiff + drDiff
					+ dbDiff + dgDiff);
			finalValue += differenceValue;
			System.out.println("Final Value : " + finalValue);
			return finalValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void main(String[] args) throws Exception {
		BufferedImage image = ImageIO.read(new File("bot1.jpg"));
		BufferedImage image1 = ImageIO.read(new File("bot.jpg"));
		new ImageCompare().getAverageValue(image, image1);
	}
}
