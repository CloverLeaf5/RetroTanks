import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;


public class OptionsMenu implements KeyListener {

	private final Label optionsLabel;
	private final Label[] headings;
	private final Button[] currentOptions;
	private final Button[] numPlayers;
	private final Button[] numRounds;
	private final Button[] scoringStyle;
	private final Button finishButton;
	private final Label[] nameInputLabels;
	private final TextField nameEntry;
	private int submitKeyCode = KeyEvent.VK_ENTER;
	private boolean getTextEventDone;
	private Tank[] players;
	private int numberOfPlayers;
	private int playerIterator = 1;
	private String currentPlayerName;
	private int currentSelection;
	private MainGame game;
	
	
	public OptionsMenu(MainGame game) {
		this.game = game;
		game.addKeyListener(this);
		
		optionsLabel = new Label("Options", 260, 60, 100, 20, new Font("Arial", Font.BOLD, 42), Color.GREEN, Color.BLACK, true);
		
		headings = new Label[3];
		headings[0] = new Label("Number of Players: ", 140, 150, 100, 20,
				new Font("Arial", Font.PLAIN, 32), Color.WHITE, Color.BLACK, true);
		headings[1] = new Label("Number of Rounds: ", 140, 250, 100, 20,
				new Font("Arial", Font.PLAIN, 32), Color.WHITE, Color.BLACK, true);
		headings[2] = new Label("Scoring Style: ", 100, 350, 100, 20,
				new Font("Arial", Font.PLAIN, 32), Color.WHITE, Color.BLACK, true);
		
		currentOptions = new Button[3];
		currentOptions[0] = new Button(Integer.toString(MainGame.getNumberOfPlayers()), 330, 140, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, true);
		currentOptions[1] = new Button(Integer.toString(MainGame.getNumRounds()), 330, 240, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, true);
		currentOptions[2] = new Button((MainGame.getScoringType().toString()), 280, 340, 120, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, true);
		
		numPlayers = new Button[7];
		numPlayers[0] = new Button("2", 370, 110, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[1] = new Button("3", 430, 110, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[2] = new Button("4", 490, 110, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[3] = new Button("5", 340, 170, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[4] = new Button("6", 400, 170, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[5] = new Button("7", 460, 170, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numPlayers[6] = new Button("8", 520, 170, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		for (int i=0; i<numPlayers.length; i++)
			numPlayers[i].setVisible(false);
		
		numRounds = new Button[8];
		numRounds[0] = new Button("1", 340, 210, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[1] = new Button("3", 400, 210, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[2] = new Button("5", 460, 210, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[3] = new Button("7", 520, 210, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[4] = new Button("9", 340, 270, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[5] = new Button("11", 400, 270, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[6] = new Button("15", 460, 270, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		numRounds[7] = new Button("21", 520, 270, 60, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		for (int i=0; i<numRounds.length; i++)
			numRounds[i].setVisible(false);
		
		scoringStyle = new Button[3];
		scoringStyle[0] = new Button("Points", 280, 300, 120, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		scoringStyle[0].setVisible(false);
		scoringStyle[1] = new Button("Rounds", 400, 300, 120, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		scoringStyle[1].setVisible(false);
		scoringStyle[2] = new Button("Kills", 340, 350, 120, 40,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		scoringStyle[2].setVisible(false);
		
		finishButton = new Button("Done", 273, MainGame.GAME_HEIGHT-70, 100, 50, new Font("Arial", Font.BOLD, 24), Color.WHITE, Color.RED, false);

		nameInputLabels = new Label[1];
		nameInputLabels[0] = new Label("Enter the name for Player 1" , 270, 180, 100, 20, 
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, false);
		nameInputLabels[0].setVisible(false);
		
		nameEntry = new TextField(this.game, 220, 240, 200, 40, new Font("Arial", Font.BOLD, 24), Color.BLACK, Color.DARK_GRAY);
	}

	
	public void tick() {
		
		// Update the text of the current options
		currentOptions[0].setText(Integer.toString(MainGame.getNumberOfPlayers()));
		currentOptions[1].setText(Integer.toString(MainGame.getNumRounds()));
		currentOptions[2].setText(MainGame.getScoringType().toString());
		
		// Check for a click on any one of the current options
		for(int i=0; i<currentOptions.length; i++) {
			if (currentOptions[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
					&& currentOptions[i].isAvailable()
					&& currentOptions[i].isVisible()) {
				currentSelection = i;
				currentOptions[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					optionClick();
			} else {
				currentOptions[i].setSelected(false);
			}
		}
		
		
		// Check for a click on number of player buttons when they are visible
		for(int i=0; i<numPlayers.length; i++) {
			if (numPlayers[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
					&& numPlayers[i].isAvailable()
					&& numPlayers[i].isVisible()) {
				currentSelection = i;
				numPlayers[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					numPlayersClick();
			} else {
				numPlayers[i].setSelected(false);
			}
		}
		
		
		// Check for a click on number of rounds buttons when they are visible
		for(int i=0; i<numRounds.length; i++) {
			if (numRounds[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
					&& numRounds[i].isAvailable()
					&& numRounds[i].isVisible()) {
				currentSelection = i;
				numRounds[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					numRoundsClick();
			} else {
				numRounds[i].setSelected(false);
			}
		}
		
		
		// Check for a click on scoring style buttons when they are visible
		for(int i=0; i<scoringStyle.length; i++) {
			if (scoringStyle[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
					&& scoringStyle[i].isAvailable()
					&& scoringStyle[i].isVisible()) {
				currentSelection = i;
				scoringStyle[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					scoringStyleClick();
			} else {
				scoringStyle[i].setSelected(false);
			}
		}
		
		
		// Switch to main menu and update options if finish button is clicked
		if (finishButton.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
				&& finishButton.isAvailable()
				&& finishButton.isVisible()) {
			finishButton.setSelected(true);
			if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
				game.reinit();
				getPlayerNames(game.getPlayerArray());
			}
		} else {
			finishButton.setSelected(false);
		}
		
		
		// Make Name Entry Text field selected based on mouse over or active based on click
		if (MainGame.gameState == STATE.NameInput) {
			if (nameEntry.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
				nameEntry.setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
					nameEntry.setActive(true);
				}
			} else {
				nameEntry.setSelected(false);
			}
			
			if (MouseInput.wasPressed(MouseEvent.BUTTON1) && !nameEntry.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1)))
					nameEntry.setActive(false);
		}
	}
	
	public void render(Graphics g) {
		
		if (MainGame.gameState != STATE.NameInput) {
			optionsLabel.render(g);

			for (int i=0; i<headings.length; i++)
				headings[i].render(g);

			for (int i=0; i<currentOptions.length; i++)
				if (currentOptions[i].isVisible())
					currentOptions[i].render(g);

			for (int i=0; i<numPlayers.length; i++)
				if (numPlayers[i].isVisible())
					numPlayers[i].render(g);

			for (int i=0; i<numRounds.length; i++)
				if (numRounds[i].isVisible())
					numRounds[i].render(g);

			for (int i=0; i<scoringStyle.length; i++)
				if (scoringStyle[i].isVisible())
					scoringStyle[i].render(g);

			finishButton.render(g);
			
		} else {
			for (int i=0; i<nameInputLabels.length; i++)
				nameInputLabels[i].render(g);
			nameEntry.render(g);
		}
		

	}
	
	// If a current option is clicked, it should bring up the options it can be changed to
	private void optionClick() {
		MouseInput.update();
		
		switch (currentSelection) {
			case 0: // Num Players
				currentOptions[1].setAvailable(false);
				currentOptions[2].setAvailable(false);
				finishButton.setAvailable(false);
				currentOptions[0].setVisible(false);
				for (int i=0; i<numPlayers.length; i++)
					numPlayers[i].setVisible(true);
				break;
				
			case 1: // Num Rounds
				currentOptions[0].setAvailable(false);
				currentOptions[2].setAvailable(false);
				finishButton.setAvailable(false);
				currentOptions[1].setVisible(false);
				for (int i=0; i<numRounds.length; i++)
					numRounds[i].setVisible(true);
				break;
				
			case 2: // Scoring Style
				currentOptions[0].setAvailable(false);
				currentOptions[1].setAvailable(false);
				finishButton.setAvailable(false);
				currentOptions[2].setVisible(false);
				scoringStyle[0].setVisible(true);
				scoringStyle[1].setVisible(true);
				scoringStyle[2].setVisible(true);
				break;
		}
	}
	
	private void numPlayersClick() {
		MouseInput.update();

		currentOptions[1].setAvailable(true);
		currentOptions[2].setAvailable(true);
		finishButton.setAvailable(true);
		currentOptions[0].setVisible(true);
		for (int i=0; i<numPlayers.length; i++)
			numPlayers[i].setVisible(false);
		
		switch (currentSelection) {
		case 0:
			MainGame.setNumPlayers(2);
			break;
		case 1:
			MainGame.setNumPlayers(3);
			break;
		case 2:
			MainGame.setNumPlayers(4);
			break;
		case 3:
			MainGame.setNumPlayers(5);
			break;
		case 4:
			MainGame.setNumPlayers(6);
			break;
		case 5:
			MainGame.setNumPlayers(7);
			break;
		case 6:
			MainGame.setNumPlayers(8);
			break;
		}
		
	}
	
	private void numRoundsClick() {
		MouseInput.update();

		currentOptions[0].setAvailable(true);
		currentOptions[2].setAvailable(true);
		finishButton.setAvailable(true);
		currentOptions[1].setVisible(true);
		for (int i=0; i<numRounds.length; i++)
			numRounds[i].setVisible(false);
		
		switch (currentSelection) {
		case 0:
			MainGame.setNumRounds(1);
			break;
		case 1:
			MainGame.setNumRounds(3);
			break;
		case 2:
			MainGame.setNumRounds(5);
			break;
		case 3:
			MainGame.setNumRounds(7);
			break;
		case 4:
			MainGame.setNumRounds(9);
			break;
		case 5:
			MainGame.setNumRounds(11);
			break;
		case 6:
			MainGame.setNumRounds(15);
			break;
		case 7:
			MainGame.setNumRounds(21);
			break;
		}
	}
	
	private void scoringStyleClick() {
		MouseInput.update();

		currentOptions[0].setAvailable(true);
		currentOptions[1].setAvailable(true);
		finishButton.setAvailable(true);
		currentOptions[2].setVisible(true);
		scoringStyle[0].setVisible(false);
		scoringStyle[1].setVisible(false);
		scoringStyle[2].setVisible(false);
		
		switch (currentSelection) {
			case 0:
				MainGame.setScoringType(Scoring.Points);
				break;
			case 1:
				MainGame.setScoringType(Scoring.Rounds);
				break;
			case 2:
				MainGame.setScoringType(Scoring.Kills);
				break;
		}
	}
	
	
	private void getPlayerNames(Tank[] players) {
		MouseInput.update();
		
		MainGame.gameState = STATE.NameInput;
		this.players = players;
		numberOfPlayers = MainGame.getNumberOfPlayers();
		
		nameInputLabels[0].setVisible(true);
		nameEntry.setVisible(true);
	}


	public void keyTyped(KeyEvent e) {
		// Do nothing
	}

	// For use with Text field
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==submitKeyCode && nameEntry.isActive()) {
			if (!getTextEventDone) {
				getTextEventDone = true;
				currentPlayerName = nameEntry.getText();
				if (currentPlayerName == null)
					players[playerIterator-1].setName("Player" + Integer.toString(playerIterator));
				else
					players[playerIterator-1].setName(currentPlayerName);
				nextPlayer();
				nameEntry.resetText();
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==submitKeyCode) {
			getTextEventDone = false;
		}
		
	}
	
	// For use with name entry
	private void nextPlayer() {
		playerIterator++;
		if (playerIterator > numberOfPlayers) {
			MainGame.gameState = STATE.MainMenu;
			playerIterator = 1;
		} else {
			nameInputLabels[0].setText("Enter the name for Player " + Integer.toString(playerIterator));
		}
	}
		
	
}
