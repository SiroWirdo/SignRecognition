package logic;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FileController {
	private ColorProcessing colorProcessing;
	private EdgeProcessing edgeProcessing;

	public FileController(){
		colorProcessing = new ColorProcessing();
		edgeProcessing = new EdgeProcessing();
	}

	public BufferedImage[] start(String path, String color, String shape){
		File file = new File(path);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Blad wczytywania obrazka");
			System.exit(0);
		}

		BufferedImage hsiImage = colorProcessing.colorBinarization(image, color);

		File outputfile = new File(Settings.RESULT_PATH + "newImage.jpg");
		try {
			ImageIO.write(hsiImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("stop1");
		Result edgeImage = edgeProcessing.edgeDetector(hsiImage, color, shape, image);

		BufferedImage[] finalIm = new BufferedImage[3];
		/*finalIm[0] = image;
		finalIm[1] = edgeImage[0];
		finalIm[2] = edgeImage[1];*/

		return finalIm;
	}

	public Result start(BufferedImage image, String color, String shape, String colorMethod){
		BufferedImage hsiImage = null;

		switch(colorMethod){
		case "hsi":
			hsiImage = colorProcessing.colorBinarization(image, color);
			break;
		case "rgb":
			hsiImage = colorProcessing.colorBinarizationRGB(image, color);
			break;
		case "ergb":
			hsiImage = colorProcessing.colorEnhancementRGB(image, color);
			break;
		default:
			hsiImage = image;
			break;
		}

		Result edgeImage = edgeProcessing.edgeDetector(hsiImage, color, shape, image);

		return edgeImage;
	}

	public void displayImage(Image img2){   
		ImageIcon icon=new ImageIcon(img2);
		JFrame frame=new JFrame();
		frame.setLayout(new FlowLayout());        
		frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);     
		JLabel lbl=new JLabel();
		lbl.setIcon(icon);
		frame.add(lbl);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static BufferedImage toBufferedImage(Image img)
	{
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}
}
