import java.awt.Color;
import java.awt.Graphics;

// Makes a layer of dirt where it lands

public class DirtBomb extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private int explosionCounter = 1;
	private int ticksSinceCollision = 1;

	public DirtBomb(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 100;
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
				if (ticksSinceCollision%1 == 0) {
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
			g.setColor(MyColor.DIRT_BROWN);
			g.fillOval((int)x-explosionCounter/2,(int)y-explosionCounter/2,explosionCounter,explosionCounter);
		}
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		// This Class doesn't cause damage
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// From center of explosion (CurrentX, CurrentY)
		// Height of explosion (vertical line) A away from CurrentX
		// = 2 * Sqrt([explosionWidth/2]^2 - A^2)
		int checkX;
		int perpToChordX;
		int explosionHeight;
		for (int i=0; i<explosionDiameter; i++) {
			checkX = (int)x - explosionDiameter/2 + i;
			if (checkX>=0 && checkX<MainGame.GAME_WIDTH) {
				perpToChordX = (int)Math.abs(x-checkX);
				explosionHeight = (int)(2 * Math.sqrt((explosionDiameter/2 * explosionDiameter/2) - (perpToChordX * perpToChordX)));
				dirtLayer.setHeight(checkX, dirtLayer.getHeight(checkX)+explosionHeight);
			}
		}
	}

	

	
}
