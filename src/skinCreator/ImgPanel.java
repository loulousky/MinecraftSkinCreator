/**
 * Copyright (C) Matt Fisher 2011
 * This software is provided as-is. I am not responsible nor liable for 
 * any data loss, hardware damage, disaster nor any other damages from 
 * use of this code. Use of this software is at your own risk.
 * 
 * Feel free to use, modify and distribute the source code and programs 
 * containing parts of the source code, as long as I am credited 
 * where necessary.
 */

package skinCreator;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImgPanel extends JPanel {

	private static final long serialVersionUID = 6436369557797536045L;
	// private Controller control;
	private final int imageHeight = 32;
	private JLabel imageLabel;
	private final int imageScale = 5;
	private final int imageWidth = 64;
	private BufferedImage outputImage;
	private BufferedImage scaledImage;

	public ImgPanel() {
		// this.control = control;
		initPanel();
	}

	private BufferedImage combineImages(BufferedImage original,
			BufferedImage toCombine) {

		int newColourVal = 0;
		int newAlpha = 0;
		int origColourVal = 0;
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				newColourVal = toCombine.getRGB(x, y);
				// Shift to read alpha channel easily
				newAlpha = newColourVal >>> 24;
				// if greater than 0, then pixel is visible
				if (newAlpha > 0) {
					
					if (newAlpha == 255) { // If new colour is solid
						original.setRGB(x, y, newColourVal);
					} else { // if new colour is semi transparent
						// setRGB value = 0xAARRGGBB
						// AA = 0 - transparent, AA = FF - solid
						origColourVal = original.getRGB(x, y);
						
						int origBlue = (origColourVal & 0x000000FF); // get blue value
						int origGreen = (origColourVal & 0x0000FF00) >>> 8; // get green value
						int origRed = (origColourVal & 0x00FF0000) >>> 16; // get red value
						
						int newBlue = (newColourVal & 0x000000FF);
						int newGreen = (newColourVal & 0x0000FF00) >>> 8;
						int newRed = (newColourVal & 0x00FF0000) >>> 16;
						
						// Colour-out = Colour-new + (1 - alpha-new)*Colour-original
						newBlue = newBlue + (((255 - newAlpha) * origBlue)/255);
						newGreen = newGreen + (((255 - newAlpha) * origGreen)/255);
						newRed = newRed + (((255 - newAlpha) * origRed)/255);
						
						int colourToSet = 0x0000FF00;
						
						colourToSet = colourToSet | newRed; // colour is now 0x0000FFRR
						colourToSet = colourToSet << 8; // colour is now 0x00FFRR00
						colourToSet = colourToSet | newGreen; // colour is now 0x00FFRRGG
						colourToSet = colourToSet << 8; // colour is now 0xFFRRGG00
						colourToSet = colourToSet | newBlue; // colour is now 0xFFRRGGBB
						//System.out.println("R=" + Integer.toHexString(newRed) + "G=" + Integer.toHexString(newGreen) + "B=" + Integer.toHexString(newBlue));
						//System.out.println(Integer.toHexString(colourToSet));
						
						original.setRGB(x, y, colourToSet);
					}
					
				}

			}
		}
		return original;

	}

	public void drawList(List<File> files) {
		if (files.size() == 0) {
			// Draw default image
		} else {
			BufferedImage output = makeBlankImage();
			BufferedImage tempImage = null;

			for (File file : files) {
				try {
					// System.out.println("File: " + file.toString());
					tempImage = ImageIO.read(file);
					output = combineImages(output, tempImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			outputImage = output;
			scaledImage = scaleImage(outputImage, imageScale);
			imageLabel.setIcon(new ImageIcon(scaledImage));

		}
	}

	private void initPanel() {
		outputImage = makeInitImage();
		scaledImage = scaleImage(outputImage, imageScale);

		this.setLayout(new BorderLayout());
		imageLabel = new JLabel(new ImageIcon(scaledImage));
		add(imageLabel, BorderLayout.CENTER);
	}

	private BufferedImage makeBlankImage() {
		BufferedImage bimage = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < bimage.getWidth(); x++) {
			for (int y = 0; y < bimage.getHeight(); y++) {
				// setRGB value = 0xAARRGGBB
				// AA = 0 - transparent, AA = FF - solid
				bimage.setRGB(x, y, 0x00000000);
			}
		}

		return bimage;
	}

	private BufferedImage makeInitImage() {
		BufferedImage bimage = makeBlankImage();
		return bimage;
	}

	public void resetImage() {
		outputImage = makeInitImage();
		scaledImage = scaleImage(outputImage, imageScale);
		imageLabel.setIcon(new ImageIcon(scaledImage));
	}

	public void savePNG(File file) {
		try {
			ImageIO.write(outputImage, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage scaleImage(BufferedImage original, int scale) {

		int newWidth = scale * original.getWidth();
		int newHeight = scale * original.getHeight();
		int type = BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaled = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g2 = scaled.createGraphics();
		g2.drawImage(original, 0, 0, newWidth, newHeight, null);
		g2.dispose();
		// return new ImageIcon(dst);

		return scaled;
	}
}
