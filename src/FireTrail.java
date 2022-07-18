import java.awt.Color;
import java.awt.Graphics;

public class FireTrail extends BasicWeapon {

	private final int BOMB_SIZE = 7;
	private int alpha = 255;
	private final int ALPHA_DECAY = 30;
	private Color color = MyColor.ROCKET_RED;

	public FireTrail(WeaponType type, float x, float y, int tagNumber) {
		super(type, x, y, tagNumber);
	}

	public void tick() {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		alpha = Integer.max(alpha-ALPHA_DECAY, 0);
		color = new Color(r,g,b,alpha);
	}

	public void render(Graphics g) {
		g.setColor(color);
		g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
	}
	
	public BasicWeapon getTrail() {
		// This class does not leave a trail
		return null;
	}
	
	public void calculateDamage(Tank[] players) {
		// This class does no damage
		return;
	}

	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		// This class does not collide
	}

	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// This class does not change the terrain
		return;
	}
}
