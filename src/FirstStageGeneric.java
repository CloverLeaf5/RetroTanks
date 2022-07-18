import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

// Monitors for a click to launch the second stage based on WeaponType

public class FirstStageGeneric extends BasicWeapon {

	
	private final int BOMB_SIZE = 10;
	private final int DAMAGE_FACTOR = 10;
	private int explosionCounter = 1;
	private int ticksSinceCollision = 1;
	private final Label howToUse;

	public FirstStageGeneric(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		explosionDiameter = 50;
		
		howToUse = new Label("CLICK AGAIN TO DEPLOY", 260, MainGame.GAME_HEIGHT/2, 100, 20,
				new Font("Arial", Font.BOLD, 30), Color.RED, Color.BLACK, true);
	}

	public void tick() {
		if (!collided) {
			if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
				nextStageReady = true;
				switch (type) {
				
					case Rocket:
						nextStage = new BasicWeapon[1];
						nextStage[0] = new SecondStageRocket(WeaponType.Rocket, x, y, velX*3, velY*3, angleR, tagNumber+1);
						break;
						
					case Scatter:
						nextStage = new BasicWeapon[50];
						for (int i=0; i<nextStage.length; i++)
							nextStage[i] = new ScatterPoint(WeaponType.Scatter, x,y, 0, 0, angleR, tagNumber+i);
						break;
						
					default:
						break;	
				}
			}
			
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
				if (ticksSinceCollision%2 == 0) {
					explosionCounter++;
					ticksSinceCollision++;
				} else ticksSinceCollision++;
			}
		}
	}

	public void render(Graphics g) {
		if (!collided) {
			g.setColor(Color.WHITE);
			g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
			howToUse.render(g);
			
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
	
	public BasicWeapon getTrail() {
		return new BombTrail(WeaponType.BombTrail, x, y, tagNumber);
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		standardDamageCalculation(players, DAMAGE_FACTOR);
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		standardTerrainAdjustment(terrain, dirtLayer);
	}
}
