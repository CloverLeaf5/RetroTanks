import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class Laser extends BasicWeapon {

	private final int DAMAGE_FACTOR = 15;
	private boolean brightening = true;
	private int laserWidth = 6;
	private int laserLength = MainGame.GAME_WIDTH*2;
	private Color color = MyColor.LASER_RED;
	private int alpha = 0;
	private int alphaStep = 5;

	public Laser(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		
	}
	
	public BasicWeapon getTrail() {
		return new DummyAttack();
	}

	public void tick() {
		if (!collided) {
			if (!brightening && alpha <= 0)
				collided = true;
			else {
				if (brightening && alpha < 255)
					alpha = Math.min(alpha+alphaStep, 255);
				else if (brightening)
					brightening = !brightening;
				else if (!brightening)
					alpha = (Math.max(alpha-alphaStep, 0));	
				int r = color.getRed();
				int g = color.getGreen();
				int b = color.getBlue();
				color = new Color(r,g,b,alpha);
			}
			
		} else
			explosionComplete = true; // Since there is no explosion to animate
	}

	public void render(Graphics g) {
		if (!collided) {
			g.setColor(color);
			Graphics2D g2d = (Graphics2D)g;
			Rectangle beam = new Rectangle(-laserWidth/2, 0, laserWidth, laserLength);
			AffineTransform transform = new AffineTransform();
			transform.translate((int)x, (int)y);
			transform.rotate(Math.PI*1.5 - angleR);
			Shape rotatedBeam = transform.createTransformedShape(beam);
			g2d.fill(rotatedBeam);
			
		}
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		// This Class does not currently collide
	}
	
	
	public void calculateDamage(Tank[] players) {
		Rectangle tank;
		Line2D beam = new Line2D.Float(x, y, x + laserLength * (float)Math.cos(angleR), y - laserLength * (float)Math.sin(angleR));
		for (int i=0; i<players.length; i++) {
			tank = new Rectangle((int)(players[i].getLocationX()-players[i].getWidth()/2), (int)(players[i].getLocationY()-players[i].getWidth()/2),
					(int)(players[i].getWidth()), (int)(players[i].getWidth())/2);
			if(tank.intersectsLine(beam)) // collided, just using a rectangle bounding box
				players[i].setHealth(players[i].getHealth()-DAMAGE_FACTOR);
		}
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// This Class does not currently change the terrain
	}

	
}
