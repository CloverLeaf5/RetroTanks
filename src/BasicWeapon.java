import java.awt.Graphics;

/*
 * For any extending weapons:
 * 1. Create the weapon class including collision, damage, and terrain change calculations
 * 2. Add to WeaponType enumeration
 * 3. Add to AttackHandler createAttack() method
 * 4. Add to Inventory list, name, and cost HashMap
 * 5. Increase the number of weapons here
 */

public abstract class BasicWeapon {
	protected float x, y;
	protected float trueX;
	protected float velX, velY;
	protected float lastX;
	protected float initialX, initialY;
	protected float angleR;
	protected WeaponType type;
	protected AttackHandler attackHandler;
	protected int tagNumber;
	protected int explosionDiameter;
	protected boolean collided = false;
	protected boolean trailsRemoved = false;
	protected boolean explosionComplete = false;
	protected boolean calculationsComplete = false;
	protected boolean nextStageReady = false;
	protected BasicWeapon[] nextStage;
	protected int currentPlayer;
	public static final int NUM_WEAPONS = 16;
	
	// For a Basic bomb
	public BasicWeapon(WeaponType type, float x, float y, float velX, float velY, float angleR, int tagNumber) {
		this.x = x;
		this.y = y;
		this.velX = velX;
		this.velY = velY;
		this.type = type;
		initialX = x;
		initialY = y;
		this.angleR = angleR;
		this.tagNumber = tagNumber;
		
		currentPlayer = MainGame.getCurrentPlayer();
	}
	
	// For Bomb Trails
	public BasicWeapon(WeaponType type, float x, float y, int tagNumber) {
		this.x = x;
		this.y = y;
		this.type = type;
		initialX = x;
		initialY = y;
		this.tagNumber = tagNumber;
	}
	
	// For non-attacks (ie Health)
	public BasicWeapon() {};
	
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void checkForCollision(Terrain terrain, DirtLayer dirtLayer);
	public abstract void calculateDamage(Tank[] players);
	public abstract void adjustTerrain(Terrain terrain, DirtLayer dirtLayer);
	public abstract BasicWeapon getTrail();
	
	public void collided() {
		collided = true;
	}
	
	// Needed when sequential attacks are created
	public void recalibrateLocation() {
		trueX = x;
		initialX = x;
		initialY = y;
	}
	
	public BasicWeapon[] getNextStage() {
		return nextStage;
	}
	
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	// This is how far the bomb would have gone without wrapping around
	public float getTrueX() {
		return trueX;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getVelX() {
		return velX;
	}
	public void setVelX(float velX) {
		this.velX = velX;
	}
	public float getVelY() {
		return velY;
	}
	public void setVelY(float velY) {
		this.velY = velY;
	}
	public WeaponType getType() {
		return type;
	}
	public void setType(WeaponType type) {
		this.type = type;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

	public boolean isExplosionComplete() {
		return explosionComplete;
	}

	public void setExplosionComplete(boolean explosionComplete) {
		this.explosionComplete = explosionComplete;
	}
	
	public boolean areTrailsRemoved() {
		return trailsRemoved;
	}

	public void setTrailsRemoved(boolean trailsRemoved) {
		this.trailsRemoved = trailsRemoved;
	}

	public int getExplosionDiameter() {
		return explosionDiameter;
	}

	public int getTagNumber() {
		return tagNumber;
	}

	public void setTagNumber(int tagNumber) {
		this.tagNumber = tagNumber;
	}

	public boolean completedCalculations() {
		return calculationsComplete;
	}

	public void setCalculationsComplete(boolean calculationsComplete) {
		this.calculationsComplete = calculationsComplete;
	}

	public boolean isNextStageReady() {
		return nextStageReady;
	}

	public void setNextStageReady(boolean nextStageReady) {
		this.nextStageReady = nextStageReady;
	}

	
	protected void parabolicCollisionCheck(Terrain terrain, DirtLayer dirtLayer) {
		// y = x*tan(angle) - (0.5*gravity*x^2) / (v0*cos(angle))^2
		
		float paraY, paraX; // location in parabola relative to initial X and Y
		int checkX; // For when the bomb wraps around;

		if (!collided) {
			// Fired to the right
			if (velX > 0.001) {
				for (int i=(int)(trueX-lastX); i>=0; i--) {
					paraX = trueX-initialX-i;
					paraY = (float) ((paraX*Math.tan(angleR)) - 
							((0.5*MainGame.gravity*(paraX*paraX)) / 
									(velX*velX)));
					checkX = ((int)trueX-i) % (int)MainGame.GAME_WIDTH; // Handles wrap around
					if (checkX<0)
						checkX = MainGame.GAME_WIDTH + checkX;
					if (initialY-paraY >= MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))  {
						collided = true;
						x = checkX;
						if (y < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))
							y = MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX);
					}
				}

				// Fired to the left
			} else if (velX < -0.001) {
				for (int i=(int)(trueX-lastX); i<=0; i++) {
					paraX = trueX-initialX-i;
					paraY = (float) ((paraX*Math.tan(angleR)) - 
							((0.5*MainGame.gravity*(paraX*paraX)) / 
									(velX*velX)));
					checkX = ((int)x-i) % (int)MainGame.GAME_WIDTH; // Handles wrap around
					if (checkX<0)
						checkX = MainGame.GAME_WIDTH + checkX;
					if (initialY-paraY >= MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))  {
						collided = true;
						x = checkX;
						if (y < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))
							y = MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX);
					}
				}

				// Fired straight up
			} else {
				if (y >= MainGame.GAME_HEIGHT-terrain.getHeight((int)x)-dirtLayer.getHeight((int)x)) {
					y = MainGame.GAME_HEIGHT-terrain.getHeight((int)x)-dirtLayer.getHeight((int)x);
					collided = true;
					if (y > MainGame.GAME_HEIGHT-Terrain.MIN_HEIGHT)
						y = MainGame.GAME_HEIGHT-Terrain.MIN_HEIGHT;
				}
			}
		
		}
	}
	
	
	protected void linearCollisionCheck (Terrain terrain, DirtLayer dirtLayer) {
		// y = -dy/dx*x
		
		float lineY, lineX; // location in line for current tick
		int checkX; // For when the bomb wraps around;

		if (!collided) {
			// Fired to the right
			if (velX > 0.001) {
				for (int i=(int)(trueX-lastX); i>=0; i--) {
					lineX = trueX-initialX-i;
					lineY = (float) (-velY/velX*lineX);
					checkX = ((int)trueX-i) % (int)MainGame.GAME_WIDTH; // Handles wrap around
					if (initialY-lineY >= MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))  {
						collided = true;
						x = checkX;
						if (y < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))
							y = MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX);
					}
				}

				// Fired to the left
			} else if (velX < -0.001) {
				for (int i=(int)(trueX-lastX); i<=0; i++) {
					lineX = trueX-initialX-i;
					lineY = (float) (-velY/velX*lineX);
					checkX = ((int)x-i) % (int)MainGame.GAME_WIDTH; // Handles wrap around
					if (initialY-lineY >= MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))  {
						collided = true;
						x = checkX;
						if (y < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX))
							y = MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX);
					}
				}

				// Fired straight up
			} else {
				if (y >= MainGame.GAME_HEIGHT-terrain.getHeight((int)x)-dirtLayer.getHeight((int)x)) {
					y = MainGame.GAME_HEIGHT-terrain.getHeight((int)x)-dirtLayer.getHeight((int)x);
					collided = true;
					if (y > MainGame.GAME_HEIGHT-Terrain.MIN_HEIGHT)
						y = MainGame.GAME_HEIGHT-Terrain.MIN_HEIGHT;
				}
			}
		
		}
	}
	
	
	protected void standardDamageCalculation(Tank[] players, final int DAMAGE_FACTOR) {
		// Calculate the distance to each player
		// Subtract health based on this distance
		
		float distance, distToTankEdge;
		float dx, dy;
		int healthLoss;
		for (int i=0; i<players.length; i++) {
			dx = players[i].getLocationX() - x;
			dy = players[i].getLocationY() - y;
			distance = (float)Math.sqrt((dx*dx) + (dy*dy));
			distToTankEdge = Float.max(distance - (players[i].getWidth()/2), 0);
			if (distToTankEdge < explosionDiameter/2) {
				healthLoss = (int)(((explosionDiameter/2) - distToTankEdge) / (explosionDiameter/2) * DAMAGE_FACTOR);
				players[i].setHealth(players[i].getHealth()-healthLoss);
				// Health change is the same as score change, lose points for self damage
				if (i != currentPlayer)
					players[currentPlayer].setRoundScore(players[currentPlayer].getRoundScore()+healthLoss);
				else
					players[currentPlayer].setRoundScore(players[currentPlayer].getRoundScore()-healthLoss);
			}
		}
	}
	
	
	protected void standardTerrainAdjustment(Terrain terrain, DirtLayer dirtLayer) {
		// From center of explosion (CurrentX, CurrentY)
		// Height of explosion (vertical line) A away from CurrentX
		// = 2 * Sqrt([explosionWidth/2]^2 - A^2)
		int checkX;
		int perpToChordX;
		int explosionHeight, explosionHighY, explosionLowY, differenceY, newHeight;
		for (int i=0; i<explosionDiameter; i++) {
			checkX = (int)x - explosionDiameter/2 + i;
			if (checkX>=0 && checkX<MainGame.GAME_WIDTH) {
				perpToChordX = (int)Math.abs(x-checkX);
				explosionHeight = (int)(2 * Math.sqrt((explosionDiameter/2 * explosionDiameter/2) - (perpToChordX * perpToChordX)));
				explosionHighY = (int)y - explosionHeight/2;
				explosionLowY = (int)y + explosionHeight/2;

				// If LowY is above dirt layer, do nothing
				// If HighY is below dirt layer, do nothing
				// If HighY is above and LowY is below, then dirt layer = 0
				// If HighY is above, subtract the difference
				// Else (LowY is below), subtract the difference
				if (explosionLowY < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX)) {
					// Do nothing
				} else if (explosionHighY > MainGame.GAME_HEIGHT-terrain.getHeight(checkX)) {
					// Do nothing
				} else if (explosionLowY > MainGame.GAME_HEIGHT-terrain.getHeight(checkX)
						&& explosionHighY < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX)) {
					dirtLayer.setHeight(checkX, 0);
				} else if (explosionHighY < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX)) { // High is above
					differenceY = explosionLowY - (int)(MainGame.GAME_HEIGHT-terrain.getHeight(checkX)-dirtLayer.getHeight(checkX));
					newHeight = (int)dirtLayer.getHeight(checkX)-differenceY;
					dirtLayer.setHeight(checkX, newHeight);
				} else { // Low is below
					differenceY = (int)(MainGame.GAME_HEIGHT-terrain.getHeight(checkX)) - explosionHighY;
					newHeight = (int)dirtLayer.getHeight(checkX)-differenceY;
					dirtLayer.setHeight(checkX, newHeight);
				}

				// If LowY is above terrain, do nothing
				// If HighY is below terrain, subtract explosionHeight
				// Else subtract the difference
				if (explosionLowY < MainGame.GAME_HEIGHT-terrain.getHeight(checkX)) {
					// Do nothing
				} else if (explosionHighY >= MainGame.GAME_HEIGHT-terrain.getHeight(checkX)) {
					newHeight = (int)MainGame.clamp(terrain.getHeight(checkX)-explosionHeight, Terrain.MIN_HEIGHT, Terrain.MAX_HEIGHT);
					terrain.setHeight(checkX, newHeight);
				} else {
					differenceY = explosionLowY - (int)(MainGame.GAME_HEIGHT-terrain.getHeight(checkX));
					newHeight = (int)MainGame.clamp(terrain.getHeight(checkX)-differenceY, Terrain.MIN_HEIGHT, Terrain.MAX_HEIGHT);
					terrain.setHeight(checkX, newHeight);
				}
			}
		}
	}


}
