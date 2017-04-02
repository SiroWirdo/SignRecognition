package logic;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import gui.MainMenu;
import gui.MoviePanel;
import gui.SignPanel;

public class Main {

	public static void main(String[] args){
		Main main = new Main();
		String video = "1.mp4";
		String path = "c:/Studia/magisterka2/Systemy wizyjne/wideo/";
		String color = "yellow";
		String shape = "triangle";
		String colorMethod = "";
		int start = 2310;
		int length = 300;
		main.startVideo(path + video, start, length, color, shape, colorMethod);
		//main.startImage(Settings.SOURCE_PATH + "3.png");

	}

	public void startVideo(String video, int start, int length, String color, String shape, String colorMethod){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		MainMenu menu = MainMenu.getMainMenu();
		MoviePanel moviePanel = menu.getMoviePanel();
		SignPanel signPanel = menu.getSignPanel();

		VideoControl vidControl = new VideoControl(video, start);

		Mat frame = vidControl.getNextFrame();
		int temp = 0;

		while(!frame.empty() && temp < length){
		//	long time = System.currentTimeMillis();
			BufferedImage image = toBufferedImage(frame);
			image = cutImage(image);
			FileController controller = new FileController();
			BufferedImage[] result = controller.start(image, color, shape, colorMethod);
			BufferedImage newImage = result[0];
			BufferedImage newImage2 = result[1];
			fillImage(image, newImage);			
			moviePanel.setImge(image);
			if(newImage2 != null){
				System.out.println("Wykryto");
				signPanel.setImage(newImage2);
			}
			frame = vidControl.getNextFrame();
		//	long time2 = System.currentTimeMillis();
		//	System.out.println("Time: " + (time2 - time));
			temp++;
		}
	}

	public void startImage(String image, String color, String shape){
		FileController controller = new FileController();
		BufferedImage[] newImage = controller.start(image, color, shape);
		fillImage(newImage[0], newImage[1]);

		File outputfile = new File(Settings.RESULT_PATH + "finalImage.jpg");
		try {
			ImageIO.write(newImage[0], "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage toBufferedImage(Mat m){
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte[bufferSize];
		m.get(0,0,b);
		BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);  
		return image;

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

	public void fillImage(BufferedImage image, BufferedImage shapes){
		for(int j = 0; j < shapes.getHeight(); j++){
			for(int i = 0; i < shapes.getWidth(); i++){
				int rgb = shapes.getRGB(i, j);
				Color color = new Color(rgb);
				if(!(color.getRed() == 0 && color.getGreen() == 0 && color.getBlue() == 0)){
					Color newColor = Color.GREEN;
					image.setRGB(i, j, newColor.getRGB());
				}
			}
		}
	}

	public BufferedImage cutImage(BufferedImage image){
		Settings.START_POINT_X = image.getWidth()/2 - 200;
		Settings.FINISH_POINT_Y = image.getHeight() - 200;
		Settings.START_POINT_Y = 200;

		BufferedImage newImage = new BufferedImage(image.getWidth() - Settings.START_POINT_X, Settings.FINISH_POINT_Y - Settings.START_POINT_Y, BufferedImage.TYPE_3BYTE_BGR);
		int tempX = 0;
		int tempY = 0;
		for(int j = Settings.START_POINT_Y; j < Settings.FINISH_POINT_Y; j++){
			for(int i = Settings.START_POINT_X; i < image.getWidth(); i++){
				newImage.setRGB(tempX, tempY, image.getRGB(i, j));
				tempX++;
			}
			tempY++;
			tempX = 0;
		}

		return newImage;
	}
}
