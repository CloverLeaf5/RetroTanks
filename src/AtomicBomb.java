import java.awt.Color;
import java.awt.Graphics;

public class AtomicBomb extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private final int DAMAGE_FACTOR = 50;
	private int explosionCounter = 0;
	private int ticksSinceCollision = 0;
	private Color color = Color.WHITE;

	public AtomicBomb(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 250;
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
			if (explosionCounter >= explosionDiameter)
				explosionComplete = true;
			else {
				if (ticksSinceCollision%1 == 0) {
					explosionCounter++;
					ticksSinceCollision++;
				} else ticksSinceCollision++;
			}
		}
	}

	public void render(Graphics g) {
		if (!collided) {
			g.setColor(color);
			g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
			
		} else if (!explosionComplete) {
			if (explosionCounter%2 == 0) {
				switch (explosionCounter % 3) {
					case 0:
						color = Color.WHITE;
						break;
					case 1:
						color = Color.RED;
						break;
					case 2:
						color = Color.ORANGE;
						break;
				}
			}
			
			g.setColor(color);
			g.fillOval((int)x-explosionCounter/2,(int)y-explosionCounter/2,explosionCounter,explosionCounter);
		}
	}
	
	public BasicWeapon getTrail() {
		return new BombTrail(WeaponType.BombTrail, x, y, tagNumber);
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		standardDamageCalculation(players, DAMAGE_FACTOR);
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		standardTerrainAdjustment(terrain, dirtLayer);
	}
}
