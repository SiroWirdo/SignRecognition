package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logic.Settings;

public class MainMenu extends JFrame{
	private static MainMenu mainMenu;
	private MoviePanel moviePanel;
	private SignPanel signPanel;
	
	private MainMenu(){
		init();
	}
	
	public void init(){
		setSize(Settings.MAX_WINDOW_WIDTH + 20 + Settings.MAX_SIGN_WINDOW_WIDTH, Settings.MAX_WINDOW_HEIGH + 40);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setResizable(false);
    	setLayout(null);
    	moviePanel = new MoviePanel();
    	moviePanel.setBounds(0, 0, Settings.MAX_WINDOW_WIDTH, Settings.MAX_WINDOW_HEIGH);
    	add(moviePanel);
    	
    	signPanel = new SignPanel();
    	signPanel.setBounds(Settings.MAX_WINDOW_WIDTH, 0, Settings.MAX_SIGN_WINDOW_WIDTH, Settings.MAX_SIGN_WINDOW_HEIGH);
    	add(signPanel);
    	
    	setVisible(true);
    	repaint();
	}
	
	public static MainMenu getMainMenu(){
		if(MainMenu.mainMenu == null){
			MainMenu.mainMenu = new MainMenu();
		}
		
		return MainMenu.mainMenu;
	}
	
	public MoviePanel getMoviePanel(){
		return moviePanel;
	}
	
	public SignPanel getSignPanel(){
		return signPanel;
	}
}
