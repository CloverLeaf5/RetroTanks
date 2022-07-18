import java.awt.Graphics;

// Does nothing. Acts as a place holder for non-attacks (ie Health)

public class DummyAttack extends BasicWeapon {

	public DummyAttack() {
		this.type = WeaponType.Dummy;
		collided = true;
		trailsRemoved = true;
		explosionComplete = true;
		calculationsComplete = true;
	}

	public void tick() {
	}

	public void render(Graphics g) {
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
