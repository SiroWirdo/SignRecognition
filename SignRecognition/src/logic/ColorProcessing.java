package logic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class ColorProcessing {

	public static float[] convRGBHSI(int[] rgb) {

		float[] hsi = new float[3];
		float h = 0;
		float s = 0;


		int b = rgb[2];
		int g = rgb[1];
		int r = rgb[0];

		int bgr = b + g + r;
		float in = bgr / 3;

		if(in != 0){
			int min = Math.min(r,g);
			min = Math.min(min, b);
			s = 1 - min/in;
		}

		float x = (float) ((r - 0.5 * g - 0.5 * b) / Math.sqrt(r * r + g * g
				+ b * b - r * g - r * b - g * b));
		float acos = (float) FastMath.acos(x);
		if (g >= b) {

			h = (float) (((57.2957795) * acos));
		} else {
			h = 360 - (float) (((57.2957795) * acos));
		}


		hsi[0] = h;
		hsi[1] = s;
		hsi[2] = in;

		return hsi;
	}

	public BufferedImage colorBinarization(BufferedImage image, String color){
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		//long fintime = 0;
	//	System.out.println(image.getHeight() + " " + image.getWidth());
		for(int j = 0; j < image.getHeight(); j++){
			for(int i = 0; i < image.getWidth(); i++){

				int[] rgbArray =  image.getRaster().getPixel(i, j, new int[3]);

			//	long time = System.currentTimeMillis();
				float[] hsi = convRGBHSI(rgbArray);
				//long time2 = System.currentTimeMillis();
				//fintime += (time2 - time);
				if(color.equals("red")){
					if(hsi[0] <= 10 || hsi[0] >= 300){
						newImage.setRGB(i, j, Color.RED.getRGB());
					}else{
						newImage.setRGB(i, j, Color.BLACK.getRGB());
					}
				}

				if(color.equals("yellow")){
					if(hsi[0] >= 31 && hsi[0] <= 62 && hsi[2] >= 30 && hsi[2] <= 210){
						newImage.setRGB(i, j, Color.YELLOW.getRGB());
					}else{
						newImage.setRGB(i, j, Color.BLACK.getRGB());
					}
				}

				if(color.equals("blue")){
					if(hsi[0] >= 195 && hsi[0] <= 220 && ((hsi[2] >= 40 && hsi[2] <= 79) || (hsi[2] >= 90 && hsi[2] <= 125))){
						newImage.setRGB(i, j, Color.BLUE.getRGB());
					}else{
						newImage.setRGB(i, j, Color.BLACK.getRGB());
					}
				}

			}
		}
	//	System.out.println("final time: " + fintime);
		return newImage;
	}
	
	public BufferedImage colorBinarizationRGB(BufferedImage image, String color){
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		//long fintime = 0;
	//	System.out.println(image.getHeight() + " " + image.getWidth());
		for(int j = 0; j < image.getHeight(); j++){
			for(int i = 0; i < image.getWidth(); i++){

				int[] rgbArray =  image.getRaster().getPixel(i, j, new int[3]);
				int r = rgbArray[0];
				int g = rgbArray[1];
				int b = rgbArray[2];
				int s = rgbArray[0] + rgbArray[1] + rgbArray[2];
				
				double min = Math.min(r - g, r - b);
				double res = Math.max(0, min/s);

				if(color.equals("red")){
					if(res > 0){
						newImage.setRGB(i, j, Color.RED.getRGB());
					}else{
						newImage.setRGB(i, j, Color.BLACK.getRGB());
					}
				}
			}
		}
	//	System.out.println("final time: " + fintime);
		return newImage;
	}

}
