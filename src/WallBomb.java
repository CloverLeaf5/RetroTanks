import java.awt.Color;
import java.awt.Graphics;

public class WallBomb extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private int explosionCounter = 0;
	private int ticksSinceCollision = 0;
	private int explosionHeight = 200;
	private int explosionWidth = 15;

	public WallBomb(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
	}
	
	public BasicWeapon getTrail() {
		return new BombTrail(WeaponType.BombTrail, x, y, tagNumber);
	}

	public void tick() {
		if (!collided) {
			lastX = trueX;
			trueX += velX;
			x = (int)trueX % (int)MainGame.GAME_WIDTH;
			if (x<0)
				x = MainGame.GAME_WIDTH+x;
			y += velY;
			velY += MainGame.gravity;
			
		} else if (!explosionComplete) {
			if (explosionCounter >= explosionHeight)
				explosionComplete = true;
			else {
				if (ticksSinceCollision%1 == 0) {
					explosionCounter+=2;
					ticksSinceCollision++;
				} else ticksSinceCollision++;
			}
		}
	}

	public void render(Graphics g) {
		if (!collided) {
			g.setColor(Color.WHITE);
			g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
			
		} else if (!explosionComplete) {
			g.setColor(Color.GREEN);
			g.fillRect((int)x-explosionWidth/2,(int)y-explosionCounter,explosionWidth,explosionCounter);
		}
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		// This Class doesn't cause damage
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// Create a width by height wall of terrain where it lands
		int leftX = (int)x - explosionWidth/2;
		for (int i=0; i<explosionWidth; i++) {
			if (leftX+i < 0 || leftX+i >= MainGame.GAME_WIDTH) {
				// Do nothing
			} else
				terrain.setHeight(leftX+i, terrain.getHeight(leftX+i)+explosionHeight);
		}
	}
	
	
}
