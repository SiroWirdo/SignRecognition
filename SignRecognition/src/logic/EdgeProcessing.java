package logic;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class EdgeProcessing {


	public BufferedImage[] edgeDetector(BufferedImage image, String color, String shape, BufferedImage orginal){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		int lower_threshold = 50;
		int upper_threshold = 150;
		int kernel_size = 3;

		BufferedImage convertedImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		convertedImg.getGraphics().drawImage(image, 0, 0, null);
		convertedImg.getGraphics().dispose();

		Mat matImage = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		Mat detected_edges = new Mat(convertedImg.getHeight(), convertedImg.getWidth(), CvType.CV_8UC3);

		byte[] pixels = ((DataBufferByte) convertedImg.getRaster().getDataBuffer()).getData();

		matImage.put(0, 0, pixels);

		Imgproc.cvtColor(matImage, matImage, Imgproc.COLOR_RGB2GRAY);

		Imgproc.blur(matImage, detected_edges, new Size(kernel_size,kernel_size) );
		
		if(Settings.USE_CANNY){
			Imgproc.Canny( detected_edges, detected_edges, lower_threshold, upper_threshold, kernel_size, false);
		}

		Mat dst = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3, new Scalar(0));
		matImage.copyTo( dst, detected_edges);
		Mat fin = new Mat();
		Result finResult = null;
		if(color.equals("blue")){
			if(shape.equals("circle")){
				finResult = circleDetector(dst, detected_edges, matImage, image.getHeight(), image.getWidth(), image);
			}else{
				finResult = rectangleDetector(dst, detected_edges, matImage, image.getHeight(), image.getWidth(), image);	
			}
			
			fin = finResult.getMatImage();
		}

		if(color.equals("red")){
			finResult = circleDetector(dst, detected_edges, matImage, image.getHeight(), image.getWidth(), image);
			fin = finResult.getMatImage();
		}

		if(color.equals("yellow")){
			finResult = triangleDetector(dst, detected_edges, matImage, image.getHeight(), image.getWidth(), image);
			fin = finResult.getMatImage();
		}

		if(fin.width() > 0 && fin.height() > 0){
			convertedImg = matToBuffered(fin);
		}

		BufferedImage[] toReturn = {convertedImg, finResult.getBuffImage()};
		return toReturn;
	}

	public Result rectangleDetector(Mat dst, Mat detected_edges, Mat matImage, int heigh, int width, BufferedImage original){
		MatOfPoint2f approx = new MatOfPoint2f();
		Result minResult = new Result(-1, 80);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(detected_edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);
		Mat fin = new Mat(heigh, width, CvType.CV_8UC3, new Scalar(0));

		for(int i = 0; i < contours.size(); i++){

			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), approx, Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true)*0.083, true);
			MatOfPoint temp = new MatOfPoint();
			approx.convertTo(temp, CvType.CV_32S);
			if(Math.abs(Imgproc.contourArea(contours.get(i))) > 100 || !Imgproc.isContourConvex((temp))){

				if(approx.rows() == 4){
					Rect rect = Imgproc.boundingRect(contours.get(i));
					double difference = Math.abs(rect.width - rect.height);

					if(rect.width > 30 && rect.height > 30 && difference <= 10){
				//		System.out.println("Difference = " + difference);
						Imgproc.rectangle(fin, new Point(rect.x,rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0,255));
						/** Rozpoznanie znaku **/
						/*	Result result = cutAndCheckSign(original, rect, true);
							if(result.getDiff() < minResult.getDiff()){
								minResult.setIndex(result.getIndex());
								minResult.setDiff(result.getDiff());
							}*/
					}
				}
			}
		}

	//	ArrayList<BufferedImage> rects = Settings.SIGNS_EXAMPLES.getRectsResult();
	//	BufferedImage rect = null;
	/** Rozpoznanie znaku **/
		/*	if(minResult.getIndex() >= 0){
			rect = rects.get(minResult.getIndex());
			System.out.print(minResult.getDiff() + " " + minResult.getIndex() + " ");
		}else{
			//	rect = new BufferedImage(86, 86, BufferedImage.TYPE_3BYTE_BGR);
		}*/

		Result finResult = new Result(fin, null);
		return finResult;
	}

	public Result triangleDetector(Mat dst, Mat detected_edges, Mat matImage, int heigh, int width, BufferedImage original){
		MatOfPoint2f approx = new MatOfPoint2f();
		Result minResult = new Result(-1, 60);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();

		Imgproc.findContours(detected_edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);
		Mat fin = new Mat(heigh, width, CvType.CV_8UC3, new Scalar(0));

		for(int i = 0; i < contours.size(); i++){

			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), approx, Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true)*0.083, true);
			MatOfPoint temp = new MatOfPoint();
			approx.convertTo(temp, CvType.CV_32S);
			if(Math.abs(Imgproc.contourArea(contours.get(i))) > 100 || !Imgproc.isContourConvex((temp))){
				if(approx.rows() == 3){
					Point[] points = approx.toArray();
					double edge1 = Math.sqrt((points[1].x - points[0].x)*(points[1].x - points[0].x) + (points[1].y - points[0].y)*(points[1].y - points[0].y));
					double edge2 = Math.sqrt((points[2].x - points[1].x)*(points[2].x - points[1].x) + (points[2].y - points[1].y)*(points[2].y - points[1].y));
					double edge3 = Math.sqrt((points[0].x - points[2].x)*(points[0].x - points[2].x) + (points[0].y - points[2].y)*(points[0].y - points[2].y));
					
					double difference = edge1 - edge2 - edge3;
					double mean = (edge1 + edge2 + edge3)/3;
					
					if(difference >= (-1 * mean - 8) && difference <= (-1 * mean + 8)){
				//		System.out.println("Edges = " + edge1 + " " + edge2 + " " + edge3 + " " + difference + " " + mean);

						Rect rect = Imgproc.boundingRect(contours.get(i));
						if(rect.width > 30 && rect.height > 30){
							Imgproc.rectangle(fin, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
							/** Rozpoznanie znaku **/
							/*Result result = cutAndCheckSign(original, rect, false);
						if(result.getDiff() < minResult.getDiff()){
							minResult.setIndex(result.getIndex());
							minResult.setDiff(result.getDiff());
						}*/
						}
					}

				}
			}
		}

	//	ArrayList<BufferedImage> triangles = Settings.SIGNS_EXAMPLES.getTrianglesResult();
	// triangle = null;
		/* Rozpoznanie znaku */
		/*if(minResult.getIndex() >= 0){
			triangle = triangles.get(minResult.getIndex());
			//	System.out.println(minResult.getDiff());
		}else{
			//	triangle = new BufferedImage(90, 80, BufferedImage.TYPE_3BYTE_BGR);
		}*/

		Result finResult = new Result(fin, null);
		return finResult;
	}

	public Result circleDetector(Mat dst, Mat detected_edges, Mat matImage, int heigh, int width, BufferedImage original){
		Result minResult = new Result(-1, 60);
		int minRadius = 20;
		int maxRadius = 80;

		Mat lines = new Mat();
		Imgproc.HoughCircles(detected_edges, lines, Imgproc.CV_HOUGH_GRADIENT, 1, 100, 100, 25, 15, 50);

		Mat fin = new Mat(heigh, width, CvType.CV_8UC3, new Scalar(0));

		for (int x = 0; x < lines.cols(); x++){
			double[] vCircle = lines.get(0,x);

			if (vCircle == null)
				break;

			Point pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));


			int radius = (int)Math.abs(Math.round(vCircle[2]));
			if(radius >= minRadius && radius <= maxRadius){
				int x1 = (int)pt.x - radius;
				int y1 = (int)pt.y - radius;
				int x2 = (int)pt.x + radius;
				int y2 = (int)pt.y + radius;

				Imgproc.circle(fin, pt, radius, new Scalar(0,255,0), 2);
				/** Rozpoznanie znaku **/
			/*	Result result = cutAndCheckSign(original, x1 - 2, y1 - 2, x2 + 2, y2 + 2, pt, radius + 2);

				if(result.getDiff() < minResult.getDiff()){
					minResult.setIndex(result.getIndex());
					minResult.setDiff(result.getDiff());
				}*/
			}
			
			/*
			radius *= 1.8;
			if(radius >= minRadius && radius <= maxRadius){
				int x1 = (int)pt.x - radius;
				int y1 = (int)pt.y - radius;
				int x2 = (int)pt.x + radius;
				int y2 = (int)pt.y + radius;

				Imgproc.circle(fin, pt, radius+5, new Scalar(0,255,0), 2);
				/** Rozpoznanie znaku **/
			/*	Result result = cutAndCheckSign(original, x1 - 2, y1 - 2, x2 + 2, y2 + 2, pt, radius + 2);
				if(result.getDiff() < minResult.getDiff()){
					minResult.setIndex(result.getIndex());
					minResult.setDiff(result.getDiff());
				}
			}*/
		}

		for (int x = 0; x < lines.rows(); x++){

			double[] vCircle = lines.get(x,0);

			if (vCircle == null)
				break;

			Point pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
			int radius = (int)Math.abs(Math.round(vCircle[2]));
			if(radius >= minRadius && radius <= maxRadius){
				int x1 = (int)pt.x - radius;
				int y1 = (int)pt.y - radius;
				int x2 = (int)pt.x + radius;
				int y2 = (int)pt.y + radius;
				Imgproc.circle(fin, pt, radius+2, new Scalar(0,255,0), 2);
				/** Rozpoznanie znaku **/
			/*	Result result = cutAndCheckSign(original, x1 - 2, y1 - 2, x2 + 2, y2 + 2, pt, radius + 2);
				if(result.getDiff() < minResult.getDiff()){
					minResult.setIndex(result.getIndex());
					minResult.setDiff(result.getDiff());
				}*/
			}
/*
			if(radius >= minRadius && radius <= maxRadius){
				radius *= 1.8;
				int x1 = (int)pt.x - radius;
				int y1 = (int)pt.y - radius;
				int x2 = (int)pt.x + radius;
				int y2 = (int)pt.y + radius;
				Imgproc.circle(fin, pt, radius+2, new Scalar(0,255,0), 2);
				
				/** Rozpoznanie znaku **/
				/*Result result = cutAndCheckSign(original, x1 - 2, y1 - 2, x2 + 2, y2 + 2, pt, radius + 2);
				if(result.getDiff() < minResult.getDiff()){
					minResult.setIndex(result.getIndex());
					minResult.setDiff(result.getDiff());
				}
			}*/
		}

	//	ArrayList<BufferedImage> circles = Settings.SIGNS_EXAMPLES.getCirclesResult();
	//	BufferedImage circle = null;
	/** Rozpoznanie znaku **/
		/*	if(minResult.getIndex() >= 0){
			circle = circles.get(minResult.getIndex());
			//	System.out.println(minResult.getDiff());
		}else{
			//		circle = new BufferedImage(86, 86, BufferedImage.TYPE_3BYTE_BGR);
		}*/

		Result finResult = new Result(fin, null);
		return finResult;
	}

	public BufferedImage matToBuffered(Mat mat){
		byte[] data = new byte[mat.rows()*mat.cols()*(int)(mat.elemSize())];
		mat.get(0, 0, data);
		if (mat.channels() == 3) {
			for (int i = 0; i < data.length; i += 3) {
				byte temp = data[i];
				data[i] = data[i + 2];
				data[i + 2] = temp;
			}
		}
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), BufferedImage.TYPE_3BYTE_BGR);
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

		return image;
	}

	public double getAngle(Point p1, Point p2, Point p0 ){
		double dx1 = p1.x - p0.x;
		double dy1 = p1.y - p0.y;
		double dx2 = p2.x - p0.x;
		double dy2 = p2.y - p0.y;

		return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) +  0.0000000001);
	}

	public double[] getMinMax(ArrayList<Double> cos){
		double[] result = new double[2];
		result[0] = cos.get(0);
		result[1] = cos.get(0);

		for(Double val : cos){
			if(val < result[0]){
				result[0] = val;
			}else{
				if(val > result[1]){
					result[1] = val;
				}
			}
		}

		return result;
	}

	public Result cutAndCheckSign(BufferedImage original, int x1, int y1, int x2, int y2, Point point, int radius){
		if(x1 < 0){
			x1 = 0;
		}

		if(y1 < 0){
			y1 = 0;
		}

		if(x2 < 0){
			x2 = 0;
		}

		if(y2 < 0){
			y2 = 0;
		}

		BufferedImage sign = new BufferedImage(x2 - x1, y2 - y1, original.getType());
		for(int i = 0; i < sign.getWidth(); i++){
			for(int j = 0; j < sign.getHeight(); j++){
				if(x1 + i < original.getWidth() && y1 + j < original.getHeight()
						&& !(lineLength(x1 + i, y1 + j, (int)point.x, (int)point.y) > radius)){
					sign.setRGB(i, j, original.getRGB(x1 + i, y1 + j));
				}else{
					sign.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}

		BufferedImage image = blackAndWhite(sign, false);
		Image dimg = image.getScaledInstance(88, 88, Image.SCALE_SMOOTH);

		BufferedImage bimage = new BufferedImage(dimg.getWidth(null), dimg.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(dimg, 0, 0, null);
		bGr.dispose();

		ArrayList<BufferedImage> circles = Settings.SIGNS_EXAMPLES.getCircles();
		double minDiff = 120;
		int index = -1;

		for(int k = 0; k < circles.size(); k++){
			BufferedImage circle = circles.get(k);
			circle = blackAndWhite(circle, true);

			double difference = 0;

			for(int i = 0; i < bimage.getWidth(); i++){
				for(int j = 0; j < bimage.getHeight(); j++){
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getRed() - new Color(circle.getRGB(i,j)).getRed());
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getBlue() - new Color(circle.getRGB(i,j)).getBlue());
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getGreen() - new Color(circle.getRGB(i,j)).getGreen());
				}
			}

			double p = difference/((bimage.getWidth()-1) * (bimage.getHeight()-1) * 3);

			if(p < minDiff){
				minDiff = p;
				index = k;
			}
		}

		Result result = new Result(index, minDiff);
		return result;
	}

	public Result cutAndCheckSign(BufferedImage original, Rect rect, boolean rectangle){
		int size_x = rectangle ? 86 : 90;
		int size_y = rectangle ? 86 : 80;
		int x1 = rect.x - 2;
		int y1 = rect.y - 2;
		int x2 = rect.x + rect.width + 2;
		int y2 = rect.y + rect.height + 2;

		if(x1 < 0){
			x1 = 0;
		}

		if(y1 < 0){
			y1 = 0;
		}

		if(x2 < 0){
			x2 = 0;
		}

		if(y2 < 0){
			y2 = 0;
		}

		BufferedImage sign = new BufferedImage(x2 - x1, y2 - y1, original.getType());
		for(int i = 0; i < sign.getWidth(); i++){
			for(int j = 0; j < sign.getHeight(); j++){
				if(x1 + i < original.getWidth() && y1 + j < original.getHeight()){
					sign.setRGB(i, j, original.getRGB(x1 + i, y1 + j));
				}else{
					sign.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}

		BufferedImage image = blackAndWhite(sign, false);
		Image dimg = image.getScaledInstance(size_x, size_y, Image.SCALE_SMOOTH);

		BufferedImage bimage = new BufferedImage(dimg.getWidth(null), dimg.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(dimg, 0, 0, null);
		bGr.dispose();

		ArrayList<BufferedImage> rects = rectangle ? Settings.SIGNS_EXAMPLES.getRects() : Settings.SIGNS_EXAMPLES.getTriangles();
		double minDiff = 120;
		int index = -1;

		//	displayImage(bimage);

		for(int k = 0; k < rects.size(); k++){
			BufferedImage circle = rects.get(k);
			circle = blackAndWhite(circle, true);
			//	displayImage(circle);
			double difference = 0;

			for(int i = 0; i < bimage.getWidth(); i++){
				for(int j = 0; j < bimage.getHeight(); j++){
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getRed() - new Color(circle.getRGB(i,j)).getRed());
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getBlue() - new Color(circle.getRGB(i,j)).getBlue());
					difference += Math.abs(new Color(bimage.getRGB(i,j)).getGreen() - new Color(circle.getRGB(i,j)).getGreen());
				}
			}

			double p = difference/((bimage.getWidth()-1) * (bimage.getHeight()-1) * 3);

			if(p < minDiff){
				minDiff = p;
				index = k;
			}
		}

		Result result = new Result(index, minDiff);
		return result;

	}

	public double lineLength(int x1, int y1, int x2, int y2){
		double line = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return line;
	}

	public BufferedImage blackAndWhite(BufferedImage original, boolean example){
		BufferedImage black = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for(int i = 0; i < original.getWidth(); i++){
			for(int j = 0; j < original.getHeight(); j++){
				int r = new Color(original.getRGB(i, j)).getRed();
				int g = new Color(original.getRGB(i, j)).getGreen();
				int b = new Color(original.getRGB(i, j)).getBlue();
				float[] hsv = new float[3];
				Color.RGBtoHSB(r,g,b,hsv);
				Color color = Color.BLACK;
				if(!example && !(new Color(original.getRGB(i, j)).getRGB() == Color.BLUE.getRGB() || new Color(original.getRGB(i, j)).getRGB() == Color.YELLOW.getRGB())){
					color = Color.WHITE;
				}
				if(example && hsv[1] < 0.3 && hsv[2] > 0.4){
					color = Color.WHITE;
				}

				black.setRGB(i, j, color.getRGB());
			}
		}

		return black;
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
}
