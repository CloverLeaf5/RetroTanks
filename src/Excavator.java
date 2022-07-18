import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Excavator extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private int explosionCounter = 10;
	private int ticksSinceCollision = 0;
	private Color color = Color.WHITE;

	public Excavator(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 100;
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
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(color);
			
	        Ellipse2D outer = new Ellipse2D.Double(x-explosionCounter/2, y-explosionCounter/2, explosionCounter, explosionCounter);
	        Ellipse2D inner = new Ellipse2D.Double(x-explosionCounter/2+5, y-explosionCounter/2+5, explosionCounter-10, explosionCounter-10);
	        Area ring = new Area(outer);
	        ring.subtract(new Area(inner));
			g2d.fill(ring);
		}
	}
	
	public BasicWeapon getTrail() {
		return new BombTrail(WeaponType.BombTrail, x, y, tagNumber);
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		// This Weapon doesn't cause damage
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		standardTerrainAdjustment(terrain, dirtLayer);
	}
}
