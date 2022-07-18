import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class Inventory {
	// Written this way rather than .values() to leave out BombTrail and Dummy
	public static WeaponType[] listOfWeapons = {WeaponType.SmallBomb,
	                                            WeaponType.LargeBomb,
	                                            WeaponType.HugeBomb,
	                                            WeaponType.AtomicBomb,
	                                            WeaponType.ClusterBomb3,
	                                            WeaponType.ClusterBomb5,
	                                            WeaponType.RandomBomb5,
	                                            WeaponType.Rocket,
	                                            WeaponType.AirStrike,
	                                            WeaponType.Laser,
	                                            WeaponType.DirtBomb,
	                                            WeaponType.WallBomb,
	                                            WeaponType.Excavator,
	                                            WeaponType.Scatter,
	                                            WeaponType.Crawler,
	                                            WeaponType.Health};

	private static HashMap <WeaponType, String> weaponNames;
	private static HashMap <WeaponType, Integer> weaponCosts;
	private final Label[] infoLabels;
	private final Label[] columnLabels;
	private final Label[] nameLabels;
	private final Label[] numAvailableLabels;
	private final Label[] costLabels;
	private final Button[] selectButtons;
	private final Button[] purchaseButtons;
	private final Button finishButton;
	private final Button[] pageButtons;
	private final Label pageLabel;
	private Tank[] players;
	private int currentPlayer;
	private int currentSelection;
	private int currentPage = 1;
	private int numOfPages;
	private int weaponsPerPage = 10;
	
	public Inventory(Tank[] players) {
		this.players = players;
		currentPlayer = MainGame.getCurrentPlayer();
		
		weaponNames = new HashMap <WeaponType, String>();
		weaponNames.put(WeaponType.SmallBomb, "Small Bomb");
		weaponNames.put(WeaponType.LargeBomb, "Large Bomb");
		weaponNames.put(WeaponType.HugeBomb, "Huge Bomb");
		weaponNames.put(WeaponType.AtomicBomb, "Atomic Bomb");
		weaponNames.put(WeaponType.ClusterBomb3, "Cluster Bomb");
		weaponNames.put(WeaponType.ClusterBomb5, "Super Cluster Bomb");
		weaponNames.put(WeaponType.RandomBomb5, "Random Bombs");
		weaponNames.put(WeaponType.Rocket, "Rocket");
		weaponNames.put(WeaponType.AirStrike, "Air Strike");
		weaponNames.put(WeaponType.Laser, "Laser");
		weaponNames.put(WeaponType.DirtBomb, "Dirt Bomb");
		weaponNames.put(WeaponType.WallBomb, "Wall Defense");
		weaponNames.put(WeaponType.Excavator, "Excavator");
		weaponNames.put(WeaponType.Scatter, "Scatter Bomb");
		weaponNames.put(WeaponType.Crawler, "Crawler");
		weaponNames.put(WeaponType.Health, "Health");
		
		weaponCosts = new HashMap <WeaponType, Integer>();
		weaponCosts.put(WeaponType.SmallBomb, 0);
		weaponCosts.put(WeaponType.LargeBomb, 50);
		weaponCosts.put(WeaponType.HugeBomb, 200);
		weaponCosts.put(WeaponType.AtomicBomb, 500);
		weaponCosts.put(WeaponType.ClusterBomb3, 50);
		weaponCosts.put(WeaponType.ClusterBomb5, 100);
		weaponCosts.put(WeaponType.RandomBomb5, 10);
		weaponCosts.put(WeaponType.Rocket, 50);
		weaponCosts.put(WeaponType.AirStrike, 200);
		weaponCosts.put(WeaponType.Laser, 150);
		weaponCosts.put(WeaponType.DirtBomb, 75);
		weaponCosts.put(WeaponType.WallBomb, 50);
		weaponCosts.put(WeaponType.Excavator, 50);
		weaponCosts.put(WeaponType.Scatter, 100);
		weaponCosts.put(WeaponType.Crawler, 100);
		weaponCosts.put(WeaponType.Health, 100);
		
		infoLabels = new Label[2];
		infoLabels[0] = new Label(players[currentPlayer].getName() + "'s Inventory", 120, 15, 100, 20,
				new Font("Arial", Font.BOLD, 24), players[currentPlayer].getColor(), Color.BLACK, true);
		infoLabels[1] = new Label("Credits: " + Integer.toString(players[currentPlayer].getCredits()), MainGame.GAME_WIDTH-150, 15, 100, 20,
				new Font("Arial", Font.BOLD, 24), Color.WHITE, Color.BLACK, true);
		
		
		columnLabels = new Label[3];
		columnLabels[0] = new Label("Weapon Type", 75, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		columnLabels[1] = new Label("# Available", 250, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		columnLabels[2] = new Label("Cost", 350, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		
		nameLabels = new Label[BasicWeapon.NUM_WEAPONS];
		for (int i=0; i<nameLabels.length; i++) {
			nameLabels[i] = new Label(weaponNames.get(listOfWeapons[i]), 75, 30*(i%weaponsPerPage)+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), Color.WHITE, Color.BLACK, true);
			if (players[currentPlayer].getCurrentWeapon() == listOfWeapons[i])
				nameLabels[i].setText("**" + weaponNames.get(listOfWeapons[i]) + "**");
		}
			
		numAvailableLabels = new Label[BasicWeapon.NUM_WEAPONS];
		for (int i=0; i<numAvailableLabels.length; i++) {
			numAvailableLabels[i] = new Label(players[currentPlayer].getWeaponInventory().get(listOfWeapons[i]).toString(),
					250, 30*(i%weaponsPerPage)+100, 100, 20, new Font("Arial", Font.PLAIN, 20), Color.WHITE, Color.BLACK, true);
		}
		
		costLabels = new Label[BasicWeapon.NUM_WEAPONS];
		for (int i=0; i<costLabels.length; i++)
			costLabels[i] = new Label(weaponCosts.get(listOfWeapons[i]).toString(), 350, 30*(i%weaponsPerPage)+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), Color.WHITE, Color.BLACK, true);
		
		selectButtons = new Button[BasicWeapon.NUM_WEAPONS];
		for (int i=0; i<selectButtons.length; i++) {
			selectButtons[i] = new Button("Select", 450, 30*(i%weaponsPerPage)+100, 70, 20,
					new Font("Arial", Font.BOLD, 15), Color.WHITE, Color.BLUE, false);
			if (players[currentPlayer].getWeaponInventory().get(listOfWeapons[i]) == 0)
				selectButtons[i].setAvailable(false);
		}
		
		purchaseButtons = new Button[BasicWeapon.NUM_WEAPONS];
		for (int i=0; i<purchaseButtons.length; i++) {
			purchaseButtons[i] = new Button("Buy", 550, 30*(i%weaponsPerPage)+100, 70, 20,
					new Font("Arial", Font.BOLD, 15), Color.WHITE, Color.GREEN, false);
			if (players[currentPlayer].getCredits() < weaponCosts.get(listOfWeapons[i]).intValue())
				purchaseButtons[i].setAvailable(false);
		}
		
		finishButton = new Button("Done", 273, MainGame.GAME_HEIGHT-50, 100, 40, new Font("Arial", Font.BOLD, 22), Color.WHITE, Color.RED, false);
		
		pageButtons = new Button[2];
		pageButtons[0] = new Button("Next Page", 530, MainGame.GAME_HEIGHT-50, 100, 40,
				new Font("Arial", Font.BOLD, 16),  Color.RED, Color.GRAY, false);
		pageButtons[1] = new Button("Last Page", 10, MainGame.GAME_HEIGHT-50, 100, 40,
				new Font("Arial", Font.BOLD, 16),  Color.RED, Color.GRAY, false);
		pageButtons[1].setVisible(false);
		
		numOfPages = BasicWeapon.NUM_WEAPONS / weaponsPerPage + 1;
		pageLabel = new Label("Page " + Integer.toString(currentPage) + "/" + Integer.toString(numOfPages),
				420, MainGame.GAME_HEIGHT-40, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
	}

	
	public void tick() {
		currentPlayer = MainGame.getCurrentPlayer();
		
		// Update name and credits available label
		infoLabels[0].setText(players[currentPlayer].getName() + "'s Inventory");
		infoLabels[1].setText("Credits: " + Integer.toString(players[currentPlayer].getCredits()));
		
		// Update the number of available column
		for (int i=0; i<numAvailableLabels.length; i++) {
			numAvailableLabels[i].setText(players[currentPlayer].getWeaponInventory().get(listOfWeapons[i]).toString());
		}
		
		// Update the name column to reflect selected weapon
		for (int i=0; i<nameLabels.length; i++)
			if (players[currentPlayer].getCurrentWeapon() == listOfWeapons[i])
				nameLabels[i].setText("**" + weaponNames.get(listOfWeapons[i]) + "**");
			else
				nameLabels[i].setText(weaponNames.get(listOfWeapons[i]));
		
		// Make select buttons available or not based on player inventory and check for a click
		for(int i=(currentPage-1)*weaponsPerPage; i<selectButtons.length && i<currentPage*weaponsPerPage; i++) {
			if (players[currentPlayer].getWeaponInventory().get(listOfWeapons[i]) == 0)
				selectButtons[i].setAvailable(false);
			else
				selectButtons[i].setAvailable(true);
			if (selectButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1)) && selectButtons[i].isAvailable()) {
				currentSelection = i;
				selectButtons[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					selectClick();
			} else {
				selectButtons[i].setSelected(false);
			}
		}
		
		// Make purchase buttons available or not based on player credits and check for a click
		for(int i=(currentPage-1)*weaponsPerPage; i<purchaseButtons.length && i<currentPage*weaponsPerPage; i++) {
			if (players[currentPlayer].getCredits() < weaponCosts.get(listOfWeapons[i]).intValue())
				purchaseButtons[i].setAvailable(false);
			else
				purchaseButtons[i].setAvailable(true);
			if (purchaseButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1)) && purchaseButtons[i].isAvailable()) {
				currentSelection = i;
				purchaseButtons[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					purchaseClick();
			} else {
				purchaseButtons[i].setSelected(false);
			}
		}
		
		// Switch to main game screen if finish button is clicked
		if (finishButton.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
			finishButton.setSelected(true);
			if (MouseInput.wasPressed(MouseEvent.BUTTON1))
				MainGame.gameState = STATE.PlayerInput;
		} else {
			finishButton.setSelected(false);
		}
		
		currentPage = players[currentPlayer].getCurrentInventoryPage();
		pageLabel.setText("Page " + Integer.toString(currentPage) + "/" + Integer.toString(numOfPages));
		
		// Decide when page buttons should be visible
		// Check for a click on page buttons when they are visible
		if (currentPage > 1)
			pageButtons[1].setVisible(true);
		else
			pageButtons[1].setVisible(false);
		if (currentPage < numOfPages)
			pageButtons[0].setVisible(true);
		else
			pageButtons[0].setVisible(false);
			
		for(int i=0; i<pageButtons.length; i++) {
			if (pageButtons[i].intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))
					&& pageButtons[i].isAvailable()
					&& pageButtons[i].isVisible()) {
				currentSelection = i;
				pageButtons[i].setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1))
					pageClick();
			} else {
				pageButtons[i].setSelected(false);
			}
		}
		
	}
	
	
	public void render(Graphics g) {
		
		for (int i=0; i<infoLabels.length; i++)
			infoLabels[i].render(g);
		
		for (int i=0; i<columnLabels.length; i++)
			columnLabels[i].render(g);
		
		for (int i=(currentPage-1)*weaponsPerPage; i<nameLabels.length && i<currentPage*weaponsPerPage; i++)
			nameLabels[i].render(g);
		
		for (int i=(currentPage-1)*weaponsPerPage; i<numAvailableLabels.length && i<currentPage*weaponsPerPage; i++)
			numAvailableLabels[i].render(g);
		
		for (int i=(currentPage-1)*weaponsPerPage; i<costLabels.length && i<currentPage*weaponsPerPage; i++)
			costLabels[i].render(g);
		
		for (int i=(currentPage-1)*weaponsPerPage; i<selectButtons.length && i<currentPage*weaponsPerPage; i++)
			selectButtons[i].render(g);
		
		for (int i=(currentPage-1)*weaponsPerPage; i<purchaseButtons.length && i<currentPage*weaponsPerPage; i++)
			purchaseButtons[i].render(g);
		
		finishButton.render(g);
		
		pageLabel.render(g);
		pageButtons[0].render(g);
		pageButtons[1].render(g);
	}
	
	
	private void selectClick() {
		// Update the text of the weapon name labels to reflect current selection
		for (int i=0; i<nameLabels.length; i++)
			nameLabels[i].setText(weaponNames.get(listOfWeapons[i]));
		// Chenge the current player's selected weapon
		players[currentPlayer].setCurrentWeapon(listOfWeapons[currentSelection]);
	}
	
	
	private void purchaseClick() {
		int quantity = players[currentPlayer].getWeaponInventory().get(listOfWeapons[currentSelection]).intValue();
		if (quantity < 99) { // Max of 99
			// Subtract the cost of the weapon
			players[currentPlayer].setCredits(players[currentPlayer].getCredits() - weaponCosts.get(listOfWeapons[currentSelection]).intValue());
			// Update the player's weapon inventory
			players[currentPlayer].getWeaponInventory().replace(listOfWeapons[currentSelection], ++quantity);
			// Update the text of the number available labels to reflect the new quantities
			numAvailableLabels[currentSelection].setText(Integer.toString(quantity));

		} else
		{} // Do nothing
		
	}
	
	
	private void pageClick() {
		if (currentSelection == 0)
			players[currentPlayer].setCurrentInventoryPage(players[currentPlayer].getCurrentInventoryPage()+1);
		else
			players[currentPlayer].setCurrentInventoryPage(players[currentPlayer].getCurrentInventoryPage()-1);;
	}
	
	
	public static HashMap <WeaponType, String> getTypetoNameMap() {
		return weaponNames;
	}
	
}
