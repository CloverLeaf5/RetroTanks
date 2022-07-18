import java.awt.Color;
import java.awt.Graphics;

public class SmallBomb extends BasicWeapon {
	
	private final int BOMB_SIZE = 10;
	private final int DAMAGE_FACTOR = 10;
	private int explosionCounter = 1;
	private int ticksSinceCollision = 1;

	public SmallBomb(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 50;
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
			if (explosionCounter >= explosionDiameter)
				explosionComplete = true;
			else {
				if (ticksSinceCollision%2 == 0) {
					explosionCounter++;
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
			switch (explosionCounter % 3) {
				case 0:
					g.setColor(Color.WHITE);
					break;
				case 1:
					g.setColor(Color.RED);
					break;
				case 2:
					g.setColor(Color.ORANGE);
					break;
			}
			g.fillOval((int)x-explosionCounter/2,(int)y-explosionCounter/2,explosionCounter,explosionCounter);
		}
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
