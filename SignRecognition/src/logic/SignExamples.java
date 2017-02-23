package logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SignExamples {
	private ArrayList<BufferedImage> circles;
	private ArrayList<BufferedImage> rects;
	private ArrayList<BufferedImage> triangles;
	private ArrayList<BufferedImage> circlesResult;
	private ArrayList<BufferedImage> rectsResult;
	private ArrayList<BufferedImage> trianglesResult;
	
	public SignExamples(){
		circles = new ArrayList<BufferedImage>();
		rects = new ArrayList<BufferedImage>();
		triangles = new ArrayList<BufferedImage>();
		circlesResult = new ArrayList<BufferedImage>();
		rectsResult = new ArrayList<BufferedImage>();
		trianglesResult = new ArrayList<BufferedImage>();
		addCircles();
		addRectangels();
		addTriangles();
	}
	
	public void addRectangels(){
		for(int i = 1; i <= 8; i++){
			File file = new File(Settings.RECT_SIGNS_PATH + i + ".png");
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			rects.add(image);
			
			File file2 = new File(Settings.RESULT_RECT_SIGNS_PATH + i + ".png");
			BufferedImage image2 = null;
			try {
				image2 = ImageIO.read(file2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			rectsResult.add(image2);
		}
	}
	
	public void addCircles(){
		for(int i = 1; i <= 7; i++){
			File file = new File(Settings.CIRCLE_SIGNS_PATH + i + ".png");
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			circles.add(image);
			
			File file2 = new File(Settings.RESULT_CIRCLE_SIGNS_PATH + i + ".png");
			BufferedImage image2 = null;
			try {
				image2 = ImageIO.read(file2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			circlesResult.add(image2);
		}
	}
	
	public void addTriangles(){
		for(int i = 1; i <= 7; i++){
			File file = new File(Settings.TRIANGLE_SIGNS_PATH + i + ".png");
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			triangles.add(image);
			
			File file2 = new File(Settings.RESULT_TRIANGLE_SIGNS_PATH + i + ".png");
			BufferedImage image2 = null;
			try {
				image2 = ImageIO.read(file2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Blad wczytywania obrazka");
				System.exit(0);
			}

			trianglesResult.add(image2);
		}
	}
	
	public ArrayList<BufferedImage> getCircles(){
		return circles;
	}
	
	public ArrayList<BufferedImage> getRects(){
		return rects;
	}
	
	public ArrayList<BufferedImage> getTriangles(){
		return triangles;
	}
	
	public ArrayList<BufferedImage> getCirclesResult(){
		return circlesResult;
	}
	
	public ArrayList<BufferedImage> getRectsResult(){
		return rectsResult;
	}
	
	public ArrayList<BufferedImage> getTrianglesResult(){
		return trianglesResult;
	}
}
