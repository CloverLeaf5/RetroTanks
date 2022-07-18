import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class CrawlingBomb extends BasicWeapon {

	private final int DAMAGE_FACTOR = 15;
	private int tickCounter = 0;
	private final int MAX_TICKS = 240; // 4 seconds
	private final int MAX_CLIMB = 10;
	private int speed;
	public final int BOMB_WIDTH = 15;
	public final int BOMB_HEIGHT = 5;
	private int explosionCounter = 0;
	private int ticksSinceCollision = 0;
	private Tank[] players;
	private Terrain terrain;
	private DirtLayer dirtLayer;
	private float lastY;

	public CrawlingBomb(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber, Tank[] players, Terrain terrain, DirtLayer dirtLayer) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 50;
		this.players = players;
		this.terrain = terrain;
		this.dirtLayer = dirtLayer;
		
		if (Math.toDegrees(angleR) <= 90)
			speed = 1;
		else
			speed = -1;
		
		lastY = y;
	}
	
	public BasicWeapon getTrail() {
		return new DummyAttack();
	}

	public void tick() {
		
		if (!collided) {
			lastX = x;
			x += speed;
			if (x < 0 || x >= MainGame.GAME_WIDTH)
				collided = true;
			else {
				lastY = y;
				y = MainGame.GAME_HEIGHT - terrain.getHeight((int)x) - dirtLayer.getHeight((int)x);
			}
			tickCounter++;
			
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
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(Color.WHITE);
			Ellipse2D ellipse = new Ellipse2D.Float(-BOMB_WIDTH/2, -BOMB_HEIGHT/2, BOMB_WIDTH, BOMB_HEIGHT);
			AffineTransform transform = new AffineTransform();
			transform.translate((int)x, (int)(y));
			
			if (tickCounter < MAX_TICKS && speed > 0) {
				switch (tickCounter % 4) {
					case 0:
						transform.rotate(0);
						break;
					case 1:
						transform.rotate(Math.PI*0.25);
						break;
					case 2:
						transform.rotate(Math.PI*0.5);
						break;
					case 3:
						transform.rotate(Math.PI*0.75);
						break;
				}
						
						//Spin the other way
			} else if (tickCounter < MAX_TICKS && speed < 0) {
				switch (tickCounter % 4) {
					case 0:
						transform.rotate(0);
						break;
					case 1:
						transform.rotate(Math.PI*0.75);
						break;
					case 2:
						transform.rotate(Math.PI*0.5);
						break;
					case 3:
						transform.rotate(Math.PI*0.25);
						break;
				}
			}
			
			Shape rotatedCannon = transform.createTransformedShape(ellipse);
			g2d.fill(rotatedCannon);

			
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
		
		// Has been going for too long
		if(tickCounter > MAX_TICKS) {
			collided = true;
			
		} else if (x < 0 || x >= MainGame.GAME_WIDTH) { // This does not wrap around
			collided = true;
		
			// Too much of a climb
		} else if (lastY-y > MAX_CLIMB) {
			collided = true;
			y = lastY;
			
		} else {
			Rectangle tank;
			for (int i=0; i<players.length; i++) {
				tank = new Rectangle((int)(players[i].getLocationX()-players[i].getWidth()/2), (int)(players[i].getLocationY()-players[i].getWidth()/2),
						(int)(players[i].getWidth()), (int)(players[i].getWidth()/2));
				if (tank.intersects(new Rectangle((int)x, (int)y, 1, 1)) && players[i].isAlive())
						collided = true;
				if (Math.abs(players[i].getLocationX()-x) < 5  && players[i].isAlive()) // If the player is at the top of a peak, it may throw off collision detection
					collided = true;
			}
		}
		
		
	}
	
	
	public void calculateDamage(Tank[] players) {
		standardDamageCalculation(players, DAMAGE_FACTOR);
		
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		standardTerrainAdjustment(terrain, dirtLayer);
	}
}
