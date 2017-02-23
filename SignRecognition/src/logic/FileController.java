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
	
	public BufferedImage[] start(String path){
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
		
		BufferedImage hsiImage = colorProcessing.colorBinarization(image, 3);
		
		File outputfile = new File(Settings.RESULT_PATH + "newImage.jpg");
		try {
			ImageIO.write(hsiImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("stop1");
		BufferedImage[] edgeImage = edgeProcessing.edgeDetector(hsiImage, "blue", image);
		
		BufferedImage[] finalIm = new BufferedImage[3];
		finalIm[0] = image;
		finalIm[1] = edgeImage[0];
		finalIm[2] = edgeImage[1];
		
		return finalIm;
	}
	
	public BufferedImage[] start(BufferedImage image){
		//long time1= System.currentTimeMillis();
		BufferedImage hsiImage = colorProcessing.colorBinarization(image, 3);
	//	long time2= System.currentTimeMillis();
		//System.out.println("czas koloru: " + (time2 - time1));
		BufferedImage[] edgeImage = edgeProcessing.edgeDetector(hsiImage, "blue", image);
	//	long time3= System.currentTimeMillis();
	//	System.out.println("czas krawedzi: " + (time3 - time2));
		
		return edgeImage;
	}
	
	public void displayImage(Image img2){   
		//BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
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