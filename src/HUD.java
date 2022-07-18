import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class HUD {
	
	private final Button[] inputButtons;
	private final Label[] inputLabels;
	private final Label[] tankInfoLabels;
	private final Label currentWeaponLabel;
	private final Label roundLabel;
	private final Button[] menuButtons;
	private final Button[] sureQuitButtons;
	private final Label sureQuitLabel;
	private int currentSelection;
	private boolean everyOther;
	
	private Tank[] players;
	private int currentPlayer;
	private MainGame game;
	
	
	public HUD(Tank[] players, MainGame game) {
		this.players = players;
		currentPlayer = MainGame.getCurrentPlayer();
		this.game = game;
		
		//Constructor (String text, int x, int y, int width, int height, Font font, Color color, Color bgColor, boolean clearBackground)
		
		menuButtons = new Button[2];
		menuButtons[0] = new Button("Menu", 10, 15, 70, 40, new Font("Arial", Font.BOLD, 16), Color.RED, Color.GRAY, false);
		menuButtons[1] = new Button("Inventory", 550, 15, 80, 40, new Font("Arial", Font.BOLD, 16),  Color.RED, Color.GRAY, false);
		
		
		inputButtons = new Button[5];
		inputButtons[0] = new Button("Fire", 273, MainGame.GAME_HEIGHT-70, 100, 50, new Font("Arial", Font.BOLD, 36), Color.RED, Color.BLACK, false);
		inputButtons[1] = new Button("Power Up", 35, MainGame.GAME_HEIGHT-72, 130, 30, new Font("Arial", Font.BOLD, 20), Color.RED, Color.GRAY, false);
		inputButtons[2] = new Button("Power Down", 35, MainGame.GAME_HEIGHT-38, 130, 30, new Font("Arial", Font.BOLD, 20), Color.RED, Color.GRAY, false);
		inputButtons[3] = new Button("Angle Up", 475, MainGame.GAME_HEIGHT-72, 130, 30, new Font("Arial", Font.BOLD, 20), Color.RED, Color.GRAY, false);
		inputButtons[4] = new Button("Angle down", 475, MainGame.GAME_HEIGHT-38, 130, 30, new Font("Arial", Font.BOLD, 20), Color.RED, Color.GRAY, false);
		
		inputLabels = new Label[2];
		inputLabels[0] = new Label(Integer.toString(players[MainGame.getCurrentPlayer()].getPower()), 175, MainGame.GAME_HEIGHT-56, 50, 30,
				new Font("Arial", Font.BOLD, 30), Color.BLACK, Color.WHITE, false);
		inputLabels[1] = new Label(Integer.toString(players[MainGame.getCurrentPlayer()].getAngle()), 415, MainGame.GAME_HEIGHT-56, 50, 30,
				new Font("Arial", Font.BOLD, 30), Color.BLACK, Color.WHITE, false);
	
		// May want to change this to account for more players
		tankInfoLabels = new Label[players.length];
		if (players.length == 2) {
			for (int i=0; i<tankInfoLabels.length; i++) {
				tankInfoLabels[i] = new Label(players[i].getName(), i*280 +130, 50, 100, 20, new Font("Arial", Font.PLAIN, 20),
						players[i].getColor(), Color.BLACK, true);
			}
		} else if (players.length <= 4) {
			for (int i=0; i<tankInfoLabels.length; i++) {
				tankInfoLabels[i] = new Label(players[i].getName(), (int)players[i].getLocationX()-40, 60, 100, 20, new Font("Arial", Font.PLAIN, 15),
						players[i].getColor(), Color.BLACK, true);
			}
		} else { // Clamps the X location of the labels and staggers the Y location by even or odd
			for (int i=0; i<tankInfoLabels.length; i++) {
				tankInfoLabels[i] = new Label(players[i].getName(), (int)MainGame.clamp(players[i].getLocationX()-40, 20, 520),
						60 + ((i%2)*20), 100, 20, new Font("Arial", Font.PLAIN, 15),
						players[i].getColor(), Color.BLACK, true);
			}
		}
		
		currentWeaponLabel = new Label(Inventory.getTypetoNameMap().get(players[currentPlayer].getCurrentWeapon()), 273, MainGame.GAME_HEIGHT-100, 100, 20,
				new Font("Arial", Font.BOLD, 18), Color.WHITE, Color.BLACK, true);
		
		roundLabel = new Label("Round " + Integer.toString(MainGame.getCurrentRound()), 273, 15, 100, 20,
				new Font("Arial", Font.BOLD, 28), Color.WHITE, Color.BLACK, true);
		
		sureQuitLabel = new Label("Are you sure you want to quit?", 180, 160, 300, 35,
				new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, false);
		//sureQuitLabel.setvisible(false);
		
		sureQuitButtons = new Button[2];
		sureQuitButtons[0] = new Button("Yes", 230, 210, 60, 40, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.DARK_GRAY, false);
		sureQuitButtons[1] = new Button("No", 350, 210, 60, 40, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.DARK_GRAY, false);
		
	}

	public void tick() {
		currentPlayer = MainGame.getCurrentPlayer();
		
		if (MainGame.gameState == STATE.PlayerInput) {
			for(int i=0; i<inputButtons.length; i++) {
				if (inputButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
					currentSelection = i;
					inputButtons[i].setSelected(true);
					if (MouseInput.wasPressed(MouseEvent.BUTTON1))
						inputClick();
					if (MouseInput.isHeld(MouseEvent.BUTTON1) && everyOther) {
						inputClick();
						everyOther = !everyOther;
					} else if (MouseInput.isHeld(MouseEvent.BUTTON1))
						everyOther = !everyOther;
				} else {
					inputButtons[i].setSelected(false);
				}
			}

			for(int i=0; i<menuButtons.length; i++) {
				if (menuButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
					currentSelection = i;
					menuButtons[i].setSelected(true);
					if (MouseInput.wasPressed(MouseEvent.BUTTON1))
						menuClick();
				} else {
					menuButtons[i].setSelected(false);
				}
			}

		}
		
		
		inputLabels[0].setText(Integer.toString(players[MainGame.getCurrentPlayer()].getPower()));
		inputLabels[1].setText(Integer.toString(players[MainGame.getCurrentPlayer()].getAngle()));
		
		
		for (int i=0; i<tankInfoLabels.length; i++) {
			tankInfoLabels[i].setText(players[i].getName() + ":  " + Integer.toString(Integer.max(players[i].getHealth(), 0)));
			if (MainGame.getCurrentPlayer() == i)
				tankInfoLabels[i].setSelected(true);
			else
				tankInfoLabels[i].setSelected(false);
		}
		
		currentWeaponLabel.setText(Inventory.getTypetoNameMap().get(players[currentPlayer].getCurrentWeapon()));
		roundLabel.setText("Round " + Integer.toString(MainGame.getCurrentRound()));
		
		// Check for a click on Sure Quit buttons when they are visible
		if (MainGame.gameState == STATE.SureQuit) {
			for(int i=0; i<sureQuitButtons.length; i++) {
				if (sureQuitButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
						&& sureQuitButtons[i].isAvailable()
						&& sureQuitButtons[i].isVisible()) {
					currentSelection = i;
					sureQuitButtons[i].setSelected(true);
					if (MouseInput.wasPressed(MouseEvent.BUTTON1))
						sureQuitClick();
				} else {
					sureQuitButtons[i].setSelected(false);
				}
			}
		}
	}
	
	public void render (Graphics g) {
		
		if (MainGame.gameState != STATE.SureQuit) {
			for (int i=0; i<menuButtons.length; i++) {
				menuButtons[i].render(g);
			}

			for (int i=0; i<inputButtons.length; i++) {
				inputButtons[i].render(g);
			}

			for (int i=0; i<inputLabels.length; i++)
				inputLabels[i].render(g);

			for (int i=0; i<tankInfoLabels.length; i++)
				tankInfoLabels[i].render(g);

			currentWeaponLabel.render(g);
			roundLabel.render(g);
		}
		
		else {
			sureQuitLabel.render(g);
			sureQuitButtons[0].render(g);
			sureQuitButtons[1].render(g);

		}
		
	}

	
	
	private void inputClick() {
		MouseInput.update();
		
		// Only do this if it is accepting inputs
		if (MainGame.gameState == STATE.PlayerInput) {
			int currentPower = players[MainGame.getCurrentPlayer()].getPower();
			int currentAngle = players[MainGame.getCurrentPlayer()].getAngle();

			switch(currentSelection) {
				case 0: // Fire
					MainGame.gameState = STATE.Firing;
					inputButtons[0].setSelected(false);
					players[MainGame.getCurrentPlayer()].attack();
					break;
				case 1: // Power Up
					currentPower++;
					currentPower = (int)MainGame.clamp(currentPower, 0, 100);
					players[MainGame.getCurrentPlayer()].setPower(currentPower);
					break;
				case 2: // Power Down
					currentPower--;
					currentPower = (int)MainGame.clamp(currentPower, 0, 100);
					players[MainGame.getCurrentPlayer()].setPower(currentPower);
					break;
				case 3: // Angle Up
					currentAngle++;
					currentAngle = (int)MainGame.clamp(currentAngle, 0, 180);
					players[MainGame.getCurrentPlayer()].setAngle(currentAngle);
					break;
				case 4: // Angle Down
					currentAngle--;
					currentAngle = (int)MainGame.clamp(currentAngle, 0, 180);
					players[MainGame.getCurrentPlayer()].setAngle(currentAngle);
					break;
			}
		}
	}
	
	private void menuClick() {
		MouseInput.update();

		// Only do this if it is accepting inputs
		if (MainGame.gameState == STATE.PlayerInput) {

			switch(currentSelection) {
				case 0: // Main Menu
					sureQuitLabel.setVisible(true);
					MainGame.gameState = STATE.SureQuit;
					break;
				case 1: // Inventory
					MainGame.gameState = STATE.Inventory;
					break;
			}
		}
	}
	
	private void sureQuitClick() {
		MouseInput.update();

		switch(currentSelection) {
			case 0: // Yes
				game.init();
				MainGame.gameState = STATE.MainMenu;
				break;
			case 1: // No
				MainGame.gameState = STATE.PlayerInput;
				break;
		}
	}
	
}