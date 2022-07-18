import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.HashMap;

public class Tank implements Comparable <Tank> {
	private WeaponType currentWeapon = WeaponType.SmallBomb;
	private AttackHandler attackHandler;
	private float locationX, locationY;
	private Terrain terrain;
	private final float TANK_WIDTH = 25;
	public static final float CANNON_LENGTH = 20;
	private final float CANNON_WIDTH = 5;
	private Color color;
	private int power = 40, angle = 60;
	private int health = 100;
	private int credits = 1000;
	private int bonusCredits = 0;
	private int score = 0;
	private int roundScore = 0;
	private int roundsWon = 0;
	private int thisRoundWon = 0;
	private int kills = 0;
	private int roundKills = 0;
	private int currentInventoryPage = 1;
	private boolean alive = true;
	public static final float POWER_CONVERSION = 0.10f;
	private float angleR;
	private String name;
	private HashMap <WeaponType, Integer> weaponInventory;
	
	public Tank (int playerNumber, float locationX, AttackHandler attackHandler, Terrain terrain) {
		this.locationX = locationX;
		this.attackHandler = attackHandler;
		this.terrain = terrain;
		this.locationY = MainGame.GAME_HEIGHT - terrain.getHeight((int)locationX);
		this.name = "Player " + Integer.toString(playerNumber+1);
		
		decideColor(playerNumber);
		angleR = (float)Math.toRadians(angle);
		
		weaponInventory = new HashMap <WeaponType, Integer>();
		for (int i=0; i<Inventory.listOfWeapons.length; i++)
			weaponInventory.put(Inventory.listOfWeapons[i], 0);
		weaponInventory.replace(WeaponType.SmallBomb, 99);
	}
	
	public void tick() {
		locationY = MainGame.GAME_HEIGHT - terrain.getHeight((int)locationX);
		
		if (weaponInventory.get(WeaponType.SmallBomb) <= 0)
			weaponInventory.replace(WeaponType.SmallBomb, 99);
	}
	
	public void render(Graphics g) {
		
		if (alive) {
			// Draw cannon
			g.setColor(Color.GRAY);
			Graphics2D g2d = (Graphics2D)g;
			Rectangle cannon = new Rectangle(-(int)(CANNON_WIDTH/2), 0, (int)CANNON_WIDTH, (int)CANNON_LENGTH);
			AffineTransform transform = new AffineTransform();
			transform.translate((int)locationX, (int)(MainGame.GAME_HEIGHT-terrain.getHeight((int)locationX)));
			transform.rotate(Math.PI*1.5 - angleR);
			Shape rotatedCannon = transform.createTransformedShape(cannon);
			g2d.fill(rotatedCannon);

			// Draw Tank
			g.setColor(color);
			g.fillOval((int)(locationX-(TANK_WIDTH/2)),
					(int)(MainGame.GAME_HEIGHT-terrain.getHeight((int)locationX)-(TANK_WIDTH/2)),
					(int)TANK_WIDTH, (int)TANK_WIDTH);

			// Erase bottom
			g.setColor(Color.BLACK);
			g.fillRect((int)(locationX-(TANK_WIDTH/2)),
					(int)(MainGame.GAME_HEIGHT-terrain.getHeight((int)locationX)),
					(int)TANK_WIDTH, (int)(TANK_WIDTH/2)+2);

			//		// Draw tank a different way. Doesn't look as good
			//		g2d.setColor(color);
			//		g2d.fill(new Arc2D.Float(locationX-TANK_WIDTH/2, locationY-TANK_WIDTH/2, TANK_WIDTH, TANK_WIDTH, 0, 180, Arc2D.PIE));
		}
		
	}
	
	private void decideColor(int playerNumber) {
		switch (playerNumber) {
			case 0:
				color = Color.ORANGE;
				break;
			case 1:
				color = Color.BLUE;
				break;
			case 2:
				color = Color.RED;
				break;
			case 3:
				color = Color.YELLOW;
				break;
			case 4:
				color = Color.MAGENTA;
				break;
			case 5:
				color = Color.WHITE;
				break;
			case 6:
				color = Color.CYAN;
				break;
			case 7:
				color = Color.PINK;
				break;
		}
		
	}
	
	public void attack() {
		attackHandler.createAttack(currentWeapon,
				locationX + (float)(CANNON_LENGTH*Math.cos(angleR)),
				locationY - (float)(CANNON_LENGTH*Math.sin(angleR)),
				power*POWER_CONVERSION, angle);
	}
	
	public void nextRound(Terrain terrain) {
		this.terrain = terrain;
		locationY = MainGame.GAME_HEIGHT - terrain.getHeight((int)locationX);
		roundScore = 0;
		bonusCredits = 0;
		roundKills = 0;
		thisRoundWon = 0;
		health = 100;
		alive = true;
	}
	

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
		angleR = (float)Math.toRadians(angle);
	}

	public float getLocationX() {
		return locationX;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLocationY() {
		return locationY;
	}

	public void setLocationY(float locationY) {
		this.locationY = locationY;
	}
	
	public float getWidth() {
		return TANK_WIDTH;
	}

	public HashMap<WeaponType, Integer> getWeaponInventory() {
		return weaponInventory;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getBonusCredits() {
		return bonusCredits;
	}

	public void setBonusCredits(int bonusCredits) {
		this.bonusCredits = bonusCredits;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public void setRoundScore(int roundScore) {
		this.roundScore = roundScore;
	}

	public int getRoundsWon() {
		return roundsWon;
	}

	public void setRoundsWon(int roundsWon) {
		this.roundsWon = roundsWon;
	}

	public int getThisRoundWon() {
		return thisRoundWon;
	}

	public void setThisRoundWon(int thisRoundWon) {
		this.thisRoundWon = thisRoundWon;
	}

	public WeaponType getCurrentWeapon() {
		return currentWeapon;
	}

	public void setCurrentWeapon(WeaponType currentWeapon) {
		this.currentWeapon = currentWeapon;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getRoundKills() {
		return roundKills;
	}

	public void setRoundKills(int roundKills) {
		this.roundKills = roundKills;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getCurrentInventoryPage() {
		return currentInventoryPage;
	}

	public void setCurrentInventoryPage(int currentInventoryPage) {
		this.currentInventoryPage = currentInventoryPage;
	}
	
	

	@Override // Compares for a reverse order sort (higher first)
	public int compareTo(Tank t) {
		// Positive if other tank is higher
		// Negative if this tank is higher
		// 0 if equal
		if (MainGame.getScoringType() == Scoring.Points)
			return t.getScore() - this.score;
		else if (MainGame.getScoringType() == Scoring.Rounds)
			return  t.getRoundsWon() - this.roundsWon;
		else // Kills
			return  t.getKills() - this.kills;
	}
	
	
}
