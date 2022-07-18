import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class ScatterPoint extends BasicWeapon {

	private final int BOMB_SIZE = 4;
	private final int DAMAGE_FACTOR = 1;
	private int tickCounter = 0;
	private final int MAX_TICKS = 60;
	public static final int MAX_SPEED = 4;

	public ScatterPoint(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
	}
	
	public BasicWeapon getTrail() {
		return new DummyAttack();
	}

	public void tick() {
		if(tickCounter > MAX_TICKS) {
			collided = true;
			trailsRemoved = true;
			explosionComplete = true;
		} else {
			lastX = trueX;
			trueX += velX;
			x = (int)trueX % (int)MainGame.GAME_WIDTH;
			if (x<0)
				x = MainGame.GAME_WIDTH+x;
			y += velY;
			tickCounter++;
		}
		
	}

	public void render(Graphics g) {
		if (tickCounter < MAX_TICKS) {
			g.setColor(Color.RED);
			g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
		}
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		// This Class doesn't collide
	}
	
	
	public void calculateDamage(Tank[] players) {
		Rectangle tank;
		Line2D line = new Line2D.Float(initialX, initialY, x, y);
		for (int i=0; i<players.length; i++) {
			tank = new Rectangle((int)(players[i].getLocationX()-players[i].getWidth()/2), (int)(players[i].getLocationY()-players[i].getWidth()/2),
					(int)(players[i].getWidth()), (int)(players[i].getWidth())/2);
			if(tank.intersectsLine(line)) // collided, just using a rectangle bounding box
				players[i].setHealth(players[i].getHealth()-DAMAGE_FACTOR);
		}
		
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// This Class doesn't change the terrain
	}

}
