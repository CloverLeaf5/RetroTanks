import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;

// Used for Weapons that do something interesting on collision

public class FirstStageNoClick extends BasicWeapon {

	private final int BOMB_SIZE = 10;
	private final int DAMAGE_FACTOR = 10;
	private int explosionCounter = 1;
	private int ticksSinceCollision = 1;
	private Tank[] players;
	private Terrain terrain;
	private DirtLayer dirtLayer;

	public FirstStageNoClick(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber, Tank[] players, Terrain terrain, DirtLayer dirtLayer) {
		super(type, x, y, velX, velY, angleR, tagNumber);
		this.trueX = x;
		this.collided = false;
		this.explosionComplete = false;
		this.players = players;
		this.terrain = terrain;
		this.dirtLayer = dirtLayer;
		
	}

	public void tick() {
		if (!collided) {
			lastX = trueX;
			trueX += velX;
			x = (int)trueX % (int)MainGame.GAME_WIDTH;
			if (x<0)
				x = MainGame.GAME_WIDTH+x;
			y += velY;
			velY += MainGame.gravity;
			
		} else {
			nextStageReady = true;
			switch (type) {	
			
				case Scatter:
					nextStage = new BasicWeapon[25];
					Random rand = new Random();
					float newVelX;
					float newVelY;
					for (int i=0; i<nextStage.length; i++) {
						newVelX = rand.nextFloat() * ScatterPoint.MAX_SPEED - ScatterPoint.MAX_SPEED/2;
						newVelY = rand.nextFloat() * ScatterPoint.MAX_SPEED - ScatterPoint.MAX_SPEED/2;
						nextStage[i] = new ScatterPoint(WeaponType.Scatter, x, y, newVelX, newVelY, angleR, tagNumber+i);
					}
					break;
					
				case Crawler:
					nextStage = new BasicWeapon[1];
					nextStage[0] = new CrawlingBomb(WeaponType.Crawler, x, y, velX, velY, angleR, tagNumber+1, players, terrain, dirtLayer);
					break;
					
				default:
					break;	
			}
		}
	}

	public void render(Graphics g) {
		if (!collided) {
			g.setColor(Color.WHITE);
			g.fillOval((int)x-BOMB_SIZE/2,(int)y-BOMB_SIZE/2,BOMB_SIZE,BOMB_SIZE);
		} 
	}
	
	public BasicWeapon getTrail() {
		return new BombTrail(WeaponType.BombTrail, x, y, tagNumber);
	}
	
	
	public void checkForCollision(Terrain terrain, DirtLayer dirtLayer) {
		parabolicCollisionCheck(terrain, dirtLayer);
	}
	
	
	public void calculateDamage(Tank[] players) {
		// This Class doesn't do damage
	}


	public void adjustTerrain(Terrain terrain, DirtLayer dirtLayer) {
		// This class doesn't change the terrain
	}
}
