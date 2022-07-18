import java.awt.Color;
import java.awt.Graphics;

// Shoots in a straight line once deployed

public class SecondStageRocket extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private final int DAMAGE_FACTOR = 10;
	private int explosionCounter = 0;
	private int ticksSinceCollision = 0;
	private int ticksSinceDeployed = 0;
	private final int MAX_TICKS = 300; // 5 seconds

	public SecondStageRocket(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 50;
	}

	public void tick() {
		if (!collided) {
			lastX = trueX;
			trueX += velX;
			x = (int)trueX % (int)MainGame.GAME_WIDTH;
			if (x<0)
				x = MainGame.GAME_WIDTH+x;
			y += velY;
			ticksSinceDeployed++;
			
			// Must account for the fact that it may not collide with terrain
			if (ticksSinceDeployed > MAX_TICKS)
				collided = true;
			
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
	
	public BasicWeapon getTrail() {
		return new FireTrail(WeaponType.BombTrail, x, y, tagNumber);
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		linearCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		standardDamageCalculation(players, DAMAGE_FACTOR);
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		standardTerrainAdjustment(terrain, dirtLayer);
	}
}
