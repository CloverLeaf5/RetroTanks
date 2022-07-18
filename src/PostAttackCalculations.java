import java.util.Random;

public class PostAttackCalculations {
	private static Tank[] players;
	private static boolean roundOver;

	
	public static void runUpdates(Tank[] players) {
		PostAttackCalculations.players = players;
		roundOver = false;
		
		checkForKills();
		checkForRoundVictor(); // Will change roundOver if there's a winner
		if (!roundOver) {
			markDead();
			nextPlayer();
			MainGame.gameState = STATE.PlayerInput;
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			giveRandomBonusCredits();
			MainGame.gameState = STATE.PostRound;
		}
	}
	
	
	
	private static void checkForKills() {
		for (int i=0; i<players.length; i++)
			if (players[i].isAlive() && players[i].getHealth() <= 0)
				if(i != MainGame.getCurrentPlayer())
					players[MainGame.getCurrentPlayer()].setRoundKills(players[MainGame.getCurrentPlayer()].getRoundKills()+1);
	}
	
	private static void checkForRoundVictor() {
		boolean firstAlivePlayer = false;
		boolean anotherAlivePlayer = false;
		int possibleWinner = 0;
		
		for (int i=0; i<players.length; i++) {
			if (players[i].isAlive() && players[i].getHealth()>0 && !firstAlivePlayer) {
				firstAlivePlayer = true;
				possibleWinner = i;
			} else if (players[i].isAlive() && players[i].getHealth()>0 && !anotherAlivePlayer)
				anotherAlivePlayer = true;
		}
		
		// At least two people alive -> continue
		if(firstAlivePlayer && anotherAlivePlayer)
			roundOver = false;
		
		// Only one person alive -> possible winner is the round winner
		else if (firstAlivePlayer && !anotherAlivePlayer) {
			roundOver = true;
			players[possibleWinner].setThisRoundWon(1);
			
		} else { // Everyone is dead -> find the highest health, if tie then no one wins the round
			roundOver = true;
			int winningHealth = Integer.MIN_VALUE;
			boolean tiedPlayer = false;
			possibleWinner = -1;
			// Find who has the highest health and if there is a winner
			for (int i=0; i<players.length; i++) {
				if (players[i].isAlive()) {
					if (players[i].getHealth() > winningHealth) {
						winningHealth = players[i].getHealth();
						possibleWinner = i;
						tiedPlayer = false;
					} else if (players[i].getHealth() > winningHealth) {
						tiedPlayer = true;
					}
				}
			}
			if (!tiedPlayer) // No tie, possibleWinner wins the round
				players[possibleWinner].setThisRoundWon(1);
		}
	}
	
	
	
	
	private static void markDead() {
		for (int i=0; i<players.length; i++) {
			if (players[i].getHealth() <= 0) {
				players[i].setAlive(false);
			}
		}
	}
	
	//Should only be called if there are at least 2 alive players
	private static void nextPlayer() {
		if (MainGame.getCurrentPlayer() == MainGame.getNumberOfPlayers()-1) // On the highest number player
			MainGame.setCurrentPlayer(0);
		else
			MainGame.setCurrentPlayer(MainGame.getCurrentPlayer()+1);
		
		if (!players[MainGame.getCurrentPlayer()].isAlive())
			nextPlayer();
	}
	
	private static void giveRandomBonusCredits() {
		Random rand = new Random();
		for (int i=0; i<players.length; i++)
			players[i].setBonusCredits(rand.nextInt(1001));
	}
}
