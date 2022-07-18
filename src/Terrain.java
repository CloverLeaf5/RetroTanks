import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Terrain {

	private float[] groundHeight = null;
	private float width, height;
	public static final float MAX_HEIGHT = MainGame.GAME_HEIGHT - 100;
	public static final float MIN_HEIGHT = 100;
	
	public Terrain(int gameWidth, int gameHeight) {
		this.width = (float)gameWidth;
		this.height = (float)gameHeight;
		
		createTerrain();
	}
	
	private void createTerrain() {
		groundHeight = new float[(int)width];
		
		Random rand = new Random();
		final int CHANGE_RATE = 30; // How often the dy is recalculated
		float currentHeight = rand.nextFloat()*(MAX_HEIGHT-MIN_HEIGHT)+MIN_HEIGHT;
		float dy = rand.nextFloat()*10-5;
		final float MAX_DY = 5;
		final float MIN_DY = -5;
		float ddy = rand.nextFloat()*2-1f;
		
		for (int i=0; i<(int)width; i++) {
			groundHeight[i] = currentHeight;
			if ((currentHeight+dy)>MAX_HEIGHT || (currentHeight+dy)<MIN_HEIGHT) {
				while ((currentHeight+dy)>MAX_HEIGHT) {
					dy--;
					currentHeight += dy;
				}
				while ((currentHeight+dy)<MIN_HEIGHT) {
					dy++;
					currentHeight += dy;
				}
			} else {
				currentHeight += dy;
				if (dy+ddy>MAX_DY || dy+ddy<MIN_DY)
					ddy = -ddy;
				if (i%5==0) dy += ddy;
				if (i%CHANGE_RATE == 0) {
					dy = rand.nextFloat()*2-1+dy;
					dy = MainGame.clamp(dy, MIN_DY, MAX_DY);
					ddy = rand.nextFloat()*2-1;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		
		for (int i=0; i<groundHeight.length; i++) {
			g.fillRect(i, (int)(height-groundHeight[i]), 1, (int)groundHeight[i]);
		}
	}
	
	public float getHeight(int x) {
		return groundHeight[x];
	}
	
	public void setHeight(int x, float height) {
		groundHeight[x] = height;
	}
	
	
}
