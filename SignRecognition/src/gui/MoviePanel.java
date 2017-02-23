package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import logic.Settings;

public class MoviePanel extends JPanel{
	private Image image;
	
	public MoviePanel(){
		setSize(Settings.MAX_WINDOW_WIDTH, Settings.MAX_WINDOW_HEIGH);
		setLayout(null);
		setBackground(Color.WHITE);
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
	    g.drawImage(image, 0, 0, null);
	  }
	
	public void setImge(Image im){
		this.image = im;
		
		repaint();
	}
}
