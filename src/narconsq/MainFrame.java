package narconsq;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame
{
	private static JFrame mainFrame;
	public static boolean isDead;
	private PlayPanel playPanel;
	
	// singleton by T110
	public static JFrame getFrame() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}
	
	public MainFrame()
	{
		isDead = false;
		playPanel = new PlayPanel();
		add(playPanel);
		
		addKeyListener(playPanel);
		setTitle("Flap Bird");
		setVisible(true);
		setResizable(false);
		setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args)
	{
		getFrame();
	}
}
