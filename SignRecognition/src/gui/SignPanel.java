package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import logic.Settings;

public class SignPanel extends JPanel{
private Image image;
	
	public SignPanel(){
		setSize(Settings.MAX_SIGN_WINDOW_WIDTH, Settings.MAX_SIGN_WINDOW_HEIGH);
		setLayout(null);
		setBackground(Color.WHITE);
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
	    g.drawImage(image, 0, 0, null);
	  }
	
	public void setImage(Image im){
		this.image = im;
		
		repaint();
	}
}
