import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;


public class MainMenu {
	
	private final Label titleLabel;
	private final Button[] menuButtons;
	private int currentSelection;
	
	public MainMenu() {
		titleLabel = new Label(MainGame.TITLE, 266, 60, 100, 20, new Font("Arial", Font.BOLD, 48), Color.GREEN, Color.BLACK, true);
		
		menuButtons = new Button[3];
		menuButtons[0] = new Button("Start", 245, 150, 150, 50, new Font("Arial", Font.BOLD, 36), Color.DARK_GRAY, Color.YELLOW, false);
		menuButtons[1] = new Button("Options", 245, 250, 150, 50, new Font("Arial", Font.BOLD, 36), Color.DARK_GRAY, Color.YELLOW, false);
		menuButtons[2] = new Button("Exit", 245, 350, 150, 50, new Font("Arial", Font.BOLD, 36), Color.DARK_GRAY, Color.YELLOW, false);
	}

	
	public void tick() {
		
		for(int i=0; i<menuButtons.length; i++) {
			if (menuButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1)) && menuButtons[i].isAvailable()) {
				currentSelection = i;
				menuButtons[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					menuClick();
			} else {
				menuButtons[i].setSelected(false);
			}
		}
		
	}
	
	public void render(Graphics g) {
		titleLabel.render(g);
		
		for (int i=0; i<menuButtons.length; i++)
			menuButtons[i].render(g);
	}
	
	
	private void menuClick() {
		
		switch (currentSelection) {
			case 0: // Play
				MainGame.gameState = STATE.PlayerInput;
				MouseInput.update();
				break;
			case 1: // Options
				MainGame.gameState = STATE.OptionsMenu;
				MouseInput.update();
				break;
			case 2: // Exit
				System.exit(0);
				break;
		}
	}
}
