package logic;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class Result {
	private int index;
	private double diff;
	
	private Mat matImage;
	private BufferedImage buffImage;
	
	public Result(int indx, double diff){
		this.index = indx;
		this.diff = diff;
	}
	
	public Result(Mat matIm, BufferedImage buffIm){
		this.matImage = matIm;
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
	
	public BufferedImage getBuffImage(){
		return buffImage;
	}
}
