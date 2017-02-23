package logic;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import exceptionsPack.VideoCapException;

public class VideoControl {
	private VideoCapture vidCapture;
	int frameNumber;
	
	public VideoControl(String videoPath, int frameNum){
		vidCapture = new VideoCapture(videoPath);
		frameNumber = frameNum;
		vidCapture.set(Videoio.CAP_PROP_POS_FRAMES, frameNumber);
		if(!vidCapture.isOpened()){
			try {
				throw new VideoCapException("B³¹d wczytywania video");
			} catch (VideoCapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Mat getNextFrame(){
		Mat frame = new Mat();
		vidCapture.read(frame);
	//	frameNumber += 4;
		
		//vidCapture.set(Videoio.CAP_PROP_POS_FRAMES, frameNumber);
		
		return frame;
	}
}
