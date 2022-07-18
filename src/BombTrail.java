import java.awt.Color;
import java.awt.Graphics;

public class BombTrail extends BasicWeapon {
	
	private final int BOMB_SIZE = 3;

	public BombTrail(WeaponType type, float x, float y, int tagNumber) {
		super(type, x, y, tagNumber);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
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
