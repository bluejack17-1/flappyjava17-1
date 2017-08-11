package narconsq;

import java.awt.*;

import javax.swing.JOptionPane;

public class Bird 
{	
	int state = 0;
	int x, y, speedX, speedY, flapCounter;
	double pitch;
	
	public Bird(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.speedX = 0;
		this.speedY = 0;
		this.pitch = 0;
	}
	
	public void move(long fpsConst)
	{
		x += Math.round(speedX * fpsConst / Constants.SPEED_DIVISOR / Constants.GLOBAL_DIVISOR);
		y += Math.round(speedY * fpsConst / Constants.SPEED_DIVISOR / Constants.GLOBAL_DIVISOR);
		speedY += Math.round(Constants.GRAVITY * fpsConst / Constants.ACCEL_DIVISOR / Constants.GLOBAL_DIVISOR);
		// by T110
		if (y > Constants.WINDOW_HEIGHT) {
			JOptionPane.showMessageDialog(null, "You're dead!");
			MainFrame.getFrame().dispose();
			synchronized (this) {
				MainFrame.isDead = true;
			}
		}
		else if (y < 0) {
			y = 0;
			speedY = 0;
		}
	}
	
	public void flap()
	{
		int speedMod = Math.round(speedY/8);
		if(speedMod > Constants.FLAP_ACCEL)
			speedMod = 0;
		speedY = -Constants.FLAP_ACCEL + speedMod;
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillOval(x, y, Constants.BIRDSIZE_X, Constants.BIRDSIZE_Y);
	}
}