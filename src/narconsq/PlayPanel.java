package narconsq;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class PlayPanel extends JPanel implements Runnable, KeyListener
{
	private Bird flappingBird;
	private Thread gameThread;
	private BufferedImage background;
	private long obstacleWait;
	private int background1Scroll;
	private int background2Scroll;
	private LinkedList<Obstacle> obstacles;
	
	public PlayPanel()
	{
		flappingBird = new Bird(Constants.START_X, Math.round(getHeight()/2));
		gameThread = new Thread(this);
		addKeyListener(this);
		obstacles = new LinkedList<Obstacle>();
		
		try 
		{
			background = ImageIO.read(new File("bg.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		        
		obstacleWait = Constants.WAIT_START;
		background1Scroll = 0;
		background2Scroll = -background.getWidth();
		
		gameThread.start();
		requestFocusInWindow();
	}
	
	//Scroll for looping background
	public void scroll(long fpsConst)
	{
		background1Scroll += Math.round(Constants.SCROLL * fpsConst / Constants.GLOBAL_DIVISOR);
		background2Scroll += Math.round(Constants.SCROLL * fpsConst / Constants.GLOBAL_DIVISOR);
		
		if((background.getWidth() - background1Scroll) <= 0)
			background1Scroll = background2Scroll - background.getWidth() ;
		else if((background.getWidth() - background2Scroll) <= 0)
			background2Scroll = background1Scroll - background.getWidth() ;
	}

	public void paint(Graphics g)
	{	
		super.paint(g);
		
		if(-background1Scroll <= Constants.WINDOW_WIDTH || (background1Scroll - background.getWidth()) >= 0)
			g.drawImage(background, -background1Scroll, 0, null);
		
		if(-background2Scroll <= Constants.WINDOW_WIDTH || (background2Scroll - background.getWidth()) >= 0)
			g.drawImage(background, -background2Scroll, 0, null);

		flappingBird.render(g);
		synchronized(obstacles)
		{
			for(Obstacle o : obstacles)
				o.render(g);
		}
	}
	
	public void updateObstacle(long fpsConst)
	{
		synchronized(obstacles)
		{
			if(obstacleWait <= 0)
			{
				obstacles.addLast(new Obstacle());
				obstacleWait = Constants.WAIT;
			}
			
			for(Obstacle o : obstacles) {
				o.update(fpsConst);
				if (Constants.START_X + Constants.BIRDSIZE_X >= o.getX() && Constants.START_X <= Constants.OBSTACLE_WIDTH + o.getX() && flappingBird.y + Constants.BIRDSIZE_Y >= o.getY()) {
					JOptionPane.showMessageDialog(null, "You're dead!");
					MainFrame.getFrame().dispose();
					synchronized(this) {
						MainFrame.isDead = true;
					}
				}
			}
			
			if(!obstacles.isEmpty() && Constants.OBSTACLE_WIDTH + obstacles.getFirst().getX() <= 0)
				obstacles.removeFirst();
			
		}
	}

	public void run() 
	{
		long lastTime;
		double maxDivisor = 1e9 / Constants.MAX_FPS;
		while(true)
		{
			synchronized (this) {
				if (MainFrame.isDead)
					break;
			}
			lastTime = System.nanoTime();   
			repaint();
			
			long delta = (long) (lastTime - (System.nanoTime() - maxDivisor));
			long deltaMillis = Math.round(delta/1e6);
			
			flappingBird.move(deltaMillis);
			
			try
			{
				Thread.sleep(deltaMillis);
			} catch(InterruptedException e) {} 
			scroll(deltaMillis);
			
			obstacleWait -= deltaMillis;
			updateObstacle(deltaMillis);
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			flappingBird.flap();	
	}
}
