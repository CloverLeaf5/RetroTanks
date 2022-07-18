import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Includes some framework for accessing out of this class

public class MouseInput extends MouseAdapter {
	
	private static int x = -1, y = -1;
	
	private static final int NUM_BUTTONS = 4;
	private static final boolean[] buttons = new boolean[NUM_BUTTONS];
	private static final boolean[] lastButtons = new boolean[NUM_BUTTONS];
	private static final boolean[] heldButtons = new boolean[NUM_BUTTONS];
	private static int[] heldLength = new int[NUM_BUTTONS];
	private Tank[] players;
	
	public MouseInput (Tank[] players) {
		this.players = players;
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
		
		int mx = e.getX();
		int my = e.getY();
		
		// Change angle to match mouse click if in field and in game mode
		if (MainGame.gameState == STATE.PlayerInput) {
			if (my < MainGame.GAME_HEIGHT - Terrain.MIN_HEIGHT && (my > 60 || (mx>100 && mx<MainGame.GAME_WIDTH-100))) {
				int px = (int)players[MainGame.getCurrentPlayer()].getLocationX();
				int py = (int)players[MainGame.getCurrentPlayer()].getLocationY();
				float angleR = (float)Math.atan2(my-py, mx-px);
				angleR =  -angleR; // Rotate the angle to match the tank
				if (angleR<0 && angleR>-Math.PI/2)
					angleR = 0;
				if (angleR<0 && angleR<-Math.PI/2)
					angleR = (float)Math.PI;
				int angle = (int)Math.toDegrees(angleR);
				players[MainGame.getCurrentPlayer()].setAngle(angle);
			}
		}
		 
	}
	
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}
	
	public static void update() {
		for (int i=0; i<NUM_BUTTONS; i++) {
			lastButtons[i] = buttons[i];
		
			if (isDown(i))
				heldLength[i]++;
			else
				heldLength[i] = 0;
			
			if (heldLength[i] > 30)
				heldButtons[i] = true;
			else
				heldButtons[i] = false;
		}
			
	}
	
	public static boolean isDown(int button) {
		return buttons[button];
	}
	
	public static boolean isHeld(int button) {
		return heldButtons[button];
	}
	
	// Only true for the first tick
	public static boolean wasPressed(int button) {
		return buttons[button] && !lastButtons[button];
	}
	
	public static boolean wasReleased(int button){
		return !buttons[button] && lastButtons[button];
	}

	// Only true for the first tick
	public void mouseMoved (MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
		
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public void updatePlayers(Tank[] players) {
		this.players = players;
	}
}
