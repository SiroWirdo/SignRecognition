package logic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Mat;

public class Result {
	private int index;
	private double diff;
	
	private Mat matImage;
	private BufferedImage finImage;
	private ArrayList<BufferedImage> buffImage;
	
	public Result(int indx, double diff){
		this.index = indx;
		this.diff = diff;
	}
	
	public Result(Mat matIm, ArrayList<BufferedImage> buffIm){
		this.matImage = matIm;
		this.buffImage = buffIm;
	}
	
	public Result(BufferedImage finImage, ArrayList<BufferedImage> buffIm){
		this.finImage = finImage;
		this.buffImage = buffIm;
	}
	
	public int getIndex(){
		return index;
	}
	
	public double getDiff(){
		return diff;
	}
	
	public void setIndex(int ind){
		this.index = ind;
	}
	
	public void setDiff(double diff){
		this.diff = diff;
	}
	
	public Mat getMatImage(){
		return matImage;
	}
	
	public ArrayList<BufferedImage> getBuffImage(){
		return buffImage;
	}
	
	public BufferedImage getFinImage(){
		return finImage;
	}
}
