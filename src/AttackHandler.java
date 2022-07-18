import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

// Takes current player, firing angle, firing power, Graphics, and current weapon to create and render the attack

/*
 * If a new weapon does not extend BasicWeapon (as with Cluster for example):
 * 1. Add to WeaponType enumeration
 * 2. Add to AttackHandler createAttack() method - here
 * 3. Add to Inventory list, name, and cost HashMap
 * 4. Increase the number of weapons in BasicWeapon
 */

// TODO Only buy things between rounds

public class AttackHandler {
	private LinkedList <BasicWeapon> currentAttack; // Will hold the bomb itself and the trail
	private LinkedList <BasicWeapon> attackQueue; // Will hold future bombs for sequential attacks
	private LinkedList <Float> angleQueue;
	private LinkedList <Float> powerQueue;
	private Terrain terrain;
	private DirtLayer dirtLayer;
	private Tank[] players;
	private int currentPlayer;
	private boolean allCalculationsComplete = true;
	private float angleR;
	private float velX, velY;
	private WeaponType currentType;

	
	public AttackHandler(Terrain terrain, DirtLayer dirtLayer, Tank[] players) {
		currentAttack = new LinkedList <BasicWeapon>();
		attackQueue = new LinkedList <BasicWeapon>();
		this.terrain = terrain;
		this.dirtLayer = dirtLayer;
		this.players = players;
	}
	
	// To be called by the fire button
	// Creates a new Weapon based on the input - power and angle
	public void createAttack(WeaponType type, float x, float y, float power, float angle) {
		currentPlayer = MainGame.getCurrentPlayer();
		allCalculationsComplete = false;
		angleR = (float)Math.toRadians(angle);
		currentType = type;
		
		velX = power * (float)Math.cos(angleR);
		velY = -power * (float)Math.sin(angleR);
		
		Random rand = new Random();
		
		switch (type) {
			case SmallBomb:
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case LargeBomb:
				currentAttack.add(new LargeBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case HugeBomb:
				currentAttack.add(new HugeBomb(WeaponType.HugeBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case AtomicBomb:
				currentAttack.add(new AtomicBomb(WeaponType.AtomicBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case ClusterBomb3:
				float velX2 = power * (float)Math.cos(angleR+0.1);
				float velY2 = -power * (float)Math.sin(angleR+0.1);
				float velX3 = power * (float)Math.cos(angleR-0.1);
				float velY3 = -power * (float)Math.sin(angleR-0.1);
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX2, velY2, angleR+0.1f, 1));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX3, velY3, angleR-0.1f, 2));
				break;
				
			case ClusterBomb5:
				velX2 = power * (float)Math.cos(angleR+0.1);
				velY2 = -power * (float)Math.sin(angleR+0.1);
				velX3 = power * (float)Math.cos(angleR+0.2);
				velY3 = -power * (float)Math.sin(angleR+0.2);
				float velX4 = power * (float)Math.cos(angleR-0.1);
				float velY4 = -power * (float)Math.sin(angleR-0.1);
				float velX5 = power * (float)Math.cos(angleR-0.2);
				float velY5 = -power * (float)Math.sin(angleR-0.2);
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX2, velY2, angleR+0.1f, 1));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX3, velY3, angleR+0.2f, 2));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX4, velY4, angleR-0.1f, 3));
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX5, velY5, angleR-0.2f, 4));
				break;
				
			case RandomBomb5: // Also adjusts the displayed power and angle, and realigns the cannon to match the shot
				power = rand.nextFloat()*100;
				players[currentPlayer].setPower((int)power);
				power = power * Tank.POWER_CONVERSION;
				angle = rand.nextFloat()*180;
				players[currentPlayer].setAngle((int)angle);
				angleR = (float)Math.toRadians(angle);
				velX = power * (float)Math.cos(angleR);
				velY = -power * (float)Math.sin(angleR);
				x = players[currentPlayer].getLocationX() + Tank.CANNON_LENGTH*(float)Math.cos(angleR);
				y = players[currentPlayer].getLocationY() - Tank.CANNON_LENGTH*(float)Math.sin(angleR);
				
				currentAttack.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				
				angleQueue = new LinkedList <Float>();
				powerQueue = new LinkedList <Float>();
				for (int i=0; i<4; i++) {
					power = rand.nextFloat()*100;
					powerQueue.add(power);
					power = power * Tank.POWER_CONVERSION;
					angle = rand.nextFloat()*180;
					angleQueue.add(angle);
					angleR = (float)Math.toRadians(angle);
					velX = power * (float)Math.cos(angleR);
					velY = -power * (float)Math.sin(angleR);
					x = players[currentPlayer].getLocationX() + Tank.CANNON_LENGTH*(float)Math.cos(angleR); // x will not change, but y may
					attackQueue.add(new SmallBomb(WeaponType.SmallBomb, x, y, velX, velY, angleR, 0));
				}
				break;
				
			case Rocket:
				currentAttack.add(new FirstStageGeneric(WeaponType.Rocket, x, y, velX, velY, angleR, 0));
				break;
				
			case AirStrike:
				currentAttack.add(new FirstStageBlankGeneric(WeaponType.AirStrike, x, y, velX, velY, angleR, 0));
				break;
				
			case Laser:
				currentAttack.add(new Laser(WeaponType.Laser, x, y, velX, velY, angleR, 0));
				break;
				
			case DirtBomb:
				currentAttack.add(new DirtBomb(WeaponType.DirtBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case WallBomb:
				currentAttack.add(new WallBomb(WeaponType.WallBomb, x, y, velX, velY, angleR, 0));
				break;
				
			case Excavator:
				currentAttack.add(new Excavator(WeaponType.Excavator, x, y, velX, velY, angleR, 0));
				break;
				
			case Scatter:
				currentAttack.add(new FirstStageNoClick(WeaponType.Scatter, x, y, velX, velY, angleR, 0, players, terrain, dirtLayer));
				break;
				
			case Crawler:
				currentAttack.add(new FirstStageNoClick(WeaponType.Crawler, x, y, velX, velY, angleR, 0, players, terrain, dirtLayer));
				break;
				
			case Health:
				players[currentPlayer].setHealth(players[currentPlayer].getHealth() + 50);
				currentAttack.add(new DummyAttack());
			
			default:
				break;
		}
	}
	
	
	public void tick() {
		if (currentAttack.isEmpty())
			return;
		else
			for (int i=0; i<currentAttack.size(); i++) {
				// Add the bomb trail before it moves for each uncollided projectile
				if (currentAttack.get(i).getType() != WeaponType.BombTrail
						&& currentAttack.get(i).getType() != WeaponType.Dummy
						&& !currentAttack.get(i).isCollided()) {
					currentAttack.add(currentAttack.get(i).getTrail());
				}
				
				currentAttack.get(i).tick();
				
				if (!currentAttack.get(i).isNextStageReady()) {
					// For all non-trail types
					if (currentAttack.get(i).getType() != WeaponType.BombTrail) {

						// Check for collision if it hasn't yet
						if (!currentAttack.get(i).isCollided())
							currentAttack.get(i).checkForCollision(terrain, dirtLayer);

						// Remove bomb trails after collision
						else if (!currentAttack.get(i).areTrailsRemoved()) {
							currentAttack.get(i).setTrailsRemoved(true);
							removeBombTrails(currentAttack.get(i).getTagNumber());
						}

						else if (currentAttack.get(i).isExplosionComplete() && !currentAttack.get(i).completedCalculations()) {
							currentAttack.get(i).setCalculationsComplete(true);
							currentAttack.get(i).calculateDamage(players);
							currentAttack.get(i).adjustTerrain(terrain, dirtLayer);
						}
					}
				} else { // This weapon has a second stage which is ready to go
					BasicWeapon[] nextStage =  currentAttack.get(i).getNextStage();
					currentAttack.clear();
					for (int j=0; j<nextStage.length; j++)
						currentAttack.add(nextStage[j]);
				}
					
			}
		
		// If all calculations complete then clear
		if (allCalculationsComplete) {
			currentAttack.clear();
			
			if (attackQueue.isEmpty()) {
				updatePlayerWeaponInventory();
				PostAttackCalculations.runUpdates(players);
					
			} else {
				// Have sequential attacks to do
				currentAttack.add(attackQueue.pop());
				players[currentPlayer].setAngle((int)angleQueue.pop().floatValue());
				players[currentPlayer].setPower((int)powerQueue.pop().floatValue());
				currentAttack.get(0).setY((players[currentPlayer].getLocationY()-(float)(Tank.CANNON_LENGTH*Math.sin(Math.toRadians(players[currentPlayer].getAngle())))));
				currentAttack.get(0).recalibrateLocation();
				allCalculationsComplete = false;
			}
		} else
			checkForCalculationsComplete();	
	}
	
	
	public void render(Graphics g) {
		if (currentAttack.isEmpty())
			return;
		else
			for (int i=0; i<currentAttack.size(); i++)
				currentAttack.get(i).render(g);
	}
	
	
	// Figure out when all damage and terrain calculations have been finished for the attack
	private void checkForCalculationsComplete() {
		allCalculationsComplete = true;
		for (int i=0; i<currentAttack.size(); i++)
			if (!currentAttack.get(i).isExplosionComplete() && currentAttack.get(i).getType() != WeaponType.BombTrail)
				allCalculationsComplete = false;
		
	}
	
	// Remove the trails for only a specific projectile based on tag t
	private void removeBombTrails(int t) {
		for (int i=currentAttack.size()-1; i>=0; i--)
			if (currentAttack.get(i).getType() == WeaponType.BombTrail && currentAttack.get(i).getTagNumber() == t)
				currentAttack.remove(i);
	}
	
	
	private void updatePlayerWeaponInventory() {
		int quantity = players[currentPlayer].getWeaponInventory().get(currentType).intValue();
		players[currentPlayer].getWeaponInventory().replace(currentType, --quantity);
		if (quantity == 0)
			players[currentPlayer].setCurrentWeapon(WeaponType.SmallBomb);
	}
	
	public void updateTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	public void updatePlayers(Tank[] players) {
		this.players = players;
	}
	

}
