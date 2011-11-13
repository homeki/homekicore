package com.homeki.core.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class TemporaryPlayWithGrabbingImages {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		while (true) {
			File f = new File("/dev/video0");
			System.out.println("Can read; " + f.canRead());
			System.out.println("Can Write; " + f.canWrite());
			byte[] b = new byte[720 * 1080 * 3];
			InputStream fis = new ByteArrayInputStream(b);
			try {
				BufferedImage img = ImageIO.read(f);
				System.out.println(img);
				// fis.reset();
				// System.out.println("Available: " + fis.available());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Util.sleep(100);
		}
	}
	
}
