import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;

// For classes like Airstrike that just wait for a click

public class FirstStageBlankGeneric extends BasicWeapon {

	private final Label howToUse;
	private final Label timer;
	private int tickCounter = 0;
	private final int MAX_TICKS = 300; // 5 seconds
	private float angle;

	public FirstStageBlankGeneric(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		this.angle = (float)Math.toDegrees(angleR);
		
		howToUse = new Label("CLICK AGAIN TO DEPLOY", 260, MainGame.GAME_HEIGHT/2, 100, 20,
				new Font("Arial", Font.BOLD, 30), Color.RED, Color.BLACK, true);
		timer = new Label("5", 260, MainGame.GAME_HEIGHT/2-40, 100, 20,
				new Font("Arial", Font.BOLD, 36), Color.RED, Color.BLACK, true);
	}

	public void tick() {
		if (MouseInput.wasPressed(MouseEvent.BUTTON1) || tickCounter > MAX_TICKS) {
			nextStageReady = true;
			int mx;
			if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
				mx = MouseInput.getX();
			} else {
				Random rand = new Random();
				mx = rand.nextInt(MainGame.GAME_WIDTH);
			}
			switch (type) {
				case AirStrike:
					nextStage = new BasicWeapon[5];
					if (angle <= 90) { // Going right
						nextStage[0] = new LargeBomb(WeaponType.LargeBomb, mx-80-60, -50, 1, 0, 0, tagNumber+1);
						nextStage[1] = new LargeBomb(WeaponType.LargeBomb, mx-40-60, -50, 1, 0, 0, tagNumber+2);
						nextStage[2] = new LargeBomb(WeaponType.LargeBomb, mx-60   , -50, 1, 0, 0, tagNumber+3);
						nextStage[3] = new LargeBomb(WeaponType.LargeBomb, mx+40-60, -50, 1, 0, 0, tagNumber+4);
						nextStage[4] = new LargeBomb(WeaponType.LargeBomb, mx+80-60, -50, 1, 0, 0, tagNumber+5);
					} else { // Going left
						nextStage[0] = new LargeBomb(WeaponType.LargeBomb, mx-80+60, -50, -1, 0, (float)Math.PI, tagNumber+1);
						nextStage[1] = new LargeBomb(WeaponType.LargeBomb, mx-40+60, -50, -1, 0, (float)Math.PI, tagNumber+2);
						nextStage[2] = new LargeBomb(WeaponType.LargeBomb, mx+60   , -50, -1, 0, (float)Math.PI, tagNumber+3);
						nextStage[3] = new LargeBomb(WeaponType.LargeBomb, mx+40+60, -50, -1, 0, (float)Math.PI, tagNumber+4);
						nextStage[4] = new LargeBomb(WeaponType.LargeBomb, mx+80+60, -50, -1, 0, (float)Math.PI, tagNumber+5);
					}
					break;
				default:
					break;	
			}
			
		} else
			tickCounter++;

		
		timer.setText(Integer.toString(MAX_TICKS/60 - tickCounter/60));

	}

	public void render(Graphics g) {
		
		howToUse.render(g);
		timer.render(g);
			
	}
	
	public BasicWeapon getTrail() {
		return new DummyAttack();
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {

		// This Class doesn't collide
		
	}
	
	
	public void calculateDamage(Tank[] players) {

		// This Class doesn't do damage
		
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {

		// This Class doesn't change the terrain
		
	}
	
	
}
