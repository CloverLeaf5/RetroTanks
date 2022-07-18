import java.awt.Graphics;

public class DirtLayer {

	private float[] dirtHeight = null;
	private Terrain terrain;
	private float width, height;
	public static final float MAX_HEIGHT = MainGame.GAME_HEIGHT - 100;
	public static final float MIN_HEIGHT = 100;
	
	public DirtLayer(int gameWidth, int gameHeight, Terrain terrain) {
		this.width = (float)gameWidth;
		this.height = (float)gameHeight;
		this.terrain = terrain;
		
		dirtHeight = new float[(int)width];
		for (int i=0; i<(int)width; i++)
			dirtHeight[i] = 0;
	}
	
	
	public void render(Graphics g) {
		g.setColor(MyColor.DIRT_BROWN);
		
		for (int i=0; i<dirtHeight.length; i++) {
			g.fillRect(i, (int)(height-terrain.getHeight(i)-dirtHeight[i]), 1, (int)dirtHeight[i]);
		}
	}
	
	public float getHeight(int x) {
		return dirtHeight[x];
	}
	
	public void setHeight(int x, float height) {
		dirtHeight[x] = height;
	}
}
