package narconsq;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Obstacle 
{
	private static Random obsRng = new Random();
	private int x, y, height;
	
	public Obstacle()
	{
		x = Constants.WINDOW_WIDTH;
		y = Constants.WINDOW_HEIGHT;
		height = obsRng.nextInt(Constants.MAX_OBSTACLE_HEIGHT-Constants.MIN_OBSTACLE_HEIGHT+1) + Constants.MIN_OBSTACLE_HEIGHT;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY() { //by T110
		return y-height;
	}
	
	public void update(long fpsConst)
	{
		x -= Math.round(Constants.SCROLL * fpsConst / Constants.GLOBAL_DIVISOR);
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillRect(x, y-height, Constants.OBSTACLE_WIDTH, height);
	}
}
