import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;


public class StatsScreen {

	private final Label roundLabel;
	private final Label gameStatsLabel;
	private final Label[] placeLabels;
	private final Label[] roundHeadings;
	private final Label[] gameHeadings;
	private final Label[] nameLabels;
	private final Label[] scoreLabels;
	private final Label[] roundScoreLabels;
	private final Label[] roundsWonLabels;
	private final Label[] thisRoundLabels;
	private final Label[] killsLabels;
	private final Label[] roundKillsLabels;
	private final Label[] creditLabels;
	private final Label[] randCreditLabels;
	private final Label[] gameScoreLabels;
	private final Label[] gameRoundsLabels;
	private final Label[] gameKillsLabels;
	private final Label randomLabel;
	private final Button nextButton;
	private final Button quitButton;
	private final Button playAgainButton;
	private boolean addedScore = false, addedCredits = false, finalSort = false;
	private boolean finalRound = false;
	@SuppressWarnings("unused")
	private Tank[] players;
	private Tank[] sortablePlayers; // A separate array to avoid resorting the main players array
	private MainGame game;
	
	public StatsScreen(Tank[] players, MainGame game) {
		this.game = game;
		
		this.players = players;
		sortablePlayers = new Tank[players.length];
		for (int i=0; i<sortablePlayers.length; i++)
			sortablePlayers[i] = players[i];
		Arrays.sort(sortablePlayers);
		
		roundLabel = new Label("Round " + Integer.toString(MainGame.getCurrentRound()), 273, 15, 100, 20,
				new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, true);
		
		randomLabel = new Label("Random", 490, 40, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		
		gameStatsLabel = new Label("Final Standings", 273, 15, 100, 20, new Font("Arial", Font.BOLD, 32), Color.WHITE, Color.BLACK, true);
		
		roundHeadings = new Label[9];
		roundHeadings[0] = new Label("Player", 50, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		roundHeadings[1] = new Label("Score", 140, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		roundHeadings[2] = new Label("Round Score", 240, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		roundHeadings[3] = new Label("Rounds Won", 140, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		roundHeadings[4] = new Label("Won This Round", 240, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		roundHeadings[5] = new Label("Kills", 140, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		roundHeadings[6] = new Label("Round Kills", 240, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		roundHeadings[7] = new Label("Credits", 380, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		roundHeadings[8] = new Label("Bonus Credits", 490, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		
		gameHeadings = new Label[4];
		gameHeadings[0] = new Label("Player", 60, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.WHITE, Color.BLACK, true);
		gameHeadings[1] = new Label("Score", 170, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.GREEN, Color.BLACK, true);
		gameHeadings[2] = new Label("Round's Won", 320, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.BLUE, Color.BLACK, true);
		gameHeadings[3] = new Label("Kills", 460, 60, 100, 20, new Font("Arial", Font.BOLD, 20), Color.RED, Color.BLACK, true);
		
		placeLabels = new Label[sortablePlayers.length];
		nameLabels = new Label[sortablePlayers.length];
		scoreLabels = new Label[sortablePlayers.length];
		roundScoreLabels = new Label[sortablePlayers.length];
		roundsWonLabels = new Label[sortablePlayers.length];
		thisRoundLabels = new Label[sortablePlayers.length];
		killsLabels = new Label[sortablePlayers.length];
		roundKillsLabels = new Label[sortablePlayers.length];
		creditLabels = new Label[sortablePlayers.length];
		randCreditLabels = new Label[sortablePlayers.length];
		
		gameScoreLabels = new Label[sortablePlayers.length];
		gameRoundsLabels = new Label[sortablePlayers.length];
		gameKillsLabels = new Label[sortablePlayers.length];
		
		for (int i=0; i<sortablePlayers.length; i++) {
			placeLabels[i] = new Label(Integer.toString(i+1) + ".", 20, i*40+100, 40, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			nameLabels[i] = new Label(sortablePlayers[i].getName(), 50, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			scoreLabels[i] = new Label(Integer.toString(sortablePlayers[i].getScore()), 140, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			roundScoreLabels[i] = new Label(Integer.toString(sortablePlayers[i].getRoundScore()), 240, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			roundsWonLabels[i] = new Label(Integer.toString(sortablePlayers[i].getRoundsWon()), 140, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			thisRoundLabels[i] = new Label(Integer.toString(sortablePlayers[i].getThisRoundWon()), 240, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			killsLabels[i] = new Label(Integer.toString(sortablePlayers[i].getKills()), 140, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			roundKillsLabels[i] = new Label(Integer.toString(sortablePlayers[i].getRoundKills()), 240, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			creditLabels[i] = new Label(Integer.toString(sortablePlayers[i].getCredits()), 380, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			randCreditLabels[i] = new Label(Integer.toString(sortablePlayers[i].getBonusCredits()), 490, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			
			gameScoreLabels[i] = new Label(Integer.toString(sortablePlayers[i].getScore()), 170, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			gameRoundsLabels[i] = new Label(Integer.toString(sortablePlayers[i].getRoundsWon()), 320, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
			gameKillsLabels[i] = new Label(Integer.toString(sortablePlayers[i].getKills()), 460, i*40+100, 100, 20,
					new Font("Arial", Font.PLAIN, 20), sortablePlayers[i].getColor(), Color.BLACK, true);
		}
				
		nextButton = new Button("Continue", 240, MainGame.GAME_HEIGHT-55, 165, 45, new Font("Arial", Font.BOLD, 28), Color.RED, Color.GRAY, false);
		playAgainButton = new Button("Play Again", 110, MainGame.GAME_HEIGHT-55, 165, 45, new Font("Arial", Font.BOLD, 28), Color.RED, Color.GRAY, false);
		quitButton = new Button("Quit", 360, MainGame.GAME_HEIGHT-55, 165, 45, new Font("Arial", Font.BOLD, 28), Color.RED, Color.GRAY, false);
		
	}
	
	public void tick() {
		
		if (MainGame.getCurrentRound() == MainGame.getNumRounds())
			finalRound = true;
		
		if (MainGame.gameState == STATE.PostRound) {
			
			if (finalRound)
				roundLabel.setText("Final Round");
			else
				roundLabel.setText("Round " + Integer.toString(MainGame.getCurrentRound()));
			
			for (int i=0; i<sortablePlayers.length; i++) {
				nameLabels[i].setText(sortablePlayers[i].getName());
				scoreLabels[i].setText(Integer.toString(sortablePlayers[i].getScore()));
				roundScoreLabels[i].setText(Integer.toString(sortablePlayers[i].getRoundScore()));
				roundsWonLabels[i].setText(Integer.toString(sortablePlayers[i].getRoundsWon()));
				thisRoundLabels[i].setText(Integer.toString(sortablePlayers[i].getThisRoundWon()));
				killsLabels[i].setText(Integer.toString(sortablePlayers[i].getKills()));
				roundKillsLabels[i].setText(Integer.toString(sortablePlayers[i].getRoundKills()));
				creditLabels[i].setText(Integer.toString(sortablePlayers[i].getCredits()));
				randCreditLabels[i].setText(Integer.toString(sortablePlayers[i].getBonusCredits()));
			}
			
			if (nextButton.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
				nextButton.setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
					nextClick();
				}
			} else {
				nextButton.setSelected(false);
			}
			
		} else if (MainGame.gameState == STATE.PostGame) {
			
			for (int i=0; i<sortablePlayers.length; i++) {
				nameLabels[i].setText(sortablePlayers[i].getName());
				gameScoreLabels[i].setText(Integer.toString(sortablePlayers[i].getScore()));
				gameRoundsLabels[i].setText(Integer.toString(sortablePlayers[i].getRoundsWon()));
				gameKillsLabels[i].setText(Integer.toString(sortablePlayers[i].getKills()));
			
			}
			
			if (playAgainButton.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
				playAgainButton.setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
					MainGame.gameState = STATE.PlayerInput;
					game.init(true);
					finalRound = false;
				}
			} else {
				playAgainButton.setSelected(false);
			}
			
			if (quitButton.intersects(new Rectangle(MouseInput.getX(), MouseInput.getY(), 1, 1))) {
				quitButton.setSelected(true);
				if (MouseInput.wasPressed(MouseEvent.BUTTON1)) {
					MainGame.gameState = STATE.MainMenu; // Could consider making a congratulations screen
				}
			} else {
				quitButton.setSelected(false);
			}
		}
		
	}
	
	public void render(Graphics g) {
		
		if (MainGame.gameState == STATE.PostRound) {
			
			roundLabel.render(g);
			
			if (!finalRound)
				randomLabel.render(g);
			
			roundHeadings[0].render(g);
			roundHeadings[7].render(g);
			if (!finalRound)
				roundHeadings[8].render(g);
			if (MainGame.getScoringType() == Scoring.Points) {
				roundHeadings[1].render(g);
				roundHeadings[2].render(g);
			} else if (MainGame.getScoringType() == Scoring.Rounds) {
				roundHeadings[3].render(g);
				roundHeadings[4].render(g);
			} else if (MainGame.getScoringType() == Scoring.Kills) {
				roundHeadings[5].render(g);
				roundHeadings[6].render(g);
			}
			
			for (int i=0; i<sortablePlayers.length; i++) {
				placeLabels[i].render(g);
				nameLabels[i].render(g);
				if (MainGame.getScoringType() == Scoring.Points) {
					scoreLabels[i].render(g);
					roundScoreLabels[i].render(g);
				} else if (MainGame.getScoringType() == Scoring.Rounds) {
					roundsWonLabels[i].render(g);
					thisRoundLabels[i].render(g);
				} else if (MainGame.getScoringType() == Scoring.Kills) {
					killsLabels[i].render(g);
					roundKillsLabels[i].render(g);
				}
				creditLabels[i].render(g);
				if (!finalRound)
					randCreditLabels[i].render(g);
			}
			
			nextButton.render(g);
			
		} else if (MainGame.gameState == STATE.PostGame) {
			
			gameStatsLabel.render(g);
			
			for (int i=0; i<gameHeadings.length; i++)
				gameHeadings[i].render(g);
			
			for (int i=0; i<sortablePlayers.length; i++) {
				placeLabels[i].render(g);
				nameLabels[i].render(g);
				gameScoreLabels[i].render(g);
				gameRoundsLabels[i].render(g);
				gameKillsLabels[i].render(g);
			}
			
			playAgainButton.render(g);
			quitButton.render(g);

		}
	}
	
	private void sortPlayersAndUpdateColors( ) {
		Arrays.sort(sortablePlayers);
		for (int i=0; i<sortablePlayers.length; i++) {
			placeLabels[i].setColor(sortablePlayers[i].getColor());
			nameLabels[i].setColor(sortablePlayers[i].getColor());
			scoreLabels[i].setColor(sortablePlayers[i].getColor());
			roundScoreLabels[i].setColor(sortablePlayers[i].getColor());
			roundsWonLabels[i].setColor(sortablePlayers[i].getColor());
			thisRoundLabels[i].setColor(sortablePlayers[i].getColor());
			killsLabels[i].setColor(sortablePlayers[i].getColor());
			roundKillsLabels[i].setColor(sortablePlayers[i].getColor());
			creditLabels[i].setColor(sortablePlayers[i].getColor());
			randCreditLabels[i].setColor(sortablePlayers[i].getColor());
			
			gameScoreLabels[i].setColor(sortablePlayers[i].getColor());
			gameRoundsLabels[i].setColor(sortablePlayers[i].getColor());
			gameKillsLabels[i].setColor(sortablePlayers[i].getColor());
		}
			
	}
	
	private void nextClick() {
		
		if (!addedScore) {
			for (int i=0; i<sortablePlayers.length; i++) {
				// Update scores, rounds, and kills
				sortablePlayers[i].setScore(sortablePlayers[i].getScore() + sortablePlayers[i].getRoundScore());
				sortablePlayers[i].setRoundsWon(sortablePlayers[i].getRoundsWon() + sortablePlayers[i].getThisRoundWon());
				sortablePlayers[i].setKills(sortablePlayers[i].getKills() + sortablePlayers[i].getRoundKills());
				scoreLabels[i].setText(Integer.toString(sortablePlayers[i].getScore()));
				gameScoreLabels[i].setText(Integer.toString(sortablePlayers[i].getScore()));
				gameRoundsLabels[i].setText(Integer.toString(sortablePlayers[i].getRoundsWon()));
				gameKillsLabels[i].setText(Integer.toString(sortablePlayers[i].getKills()));
				
				// Update credits
				sortablePlayers[i].setCredits(sortablePlayers[i].getCredits() + sortablePlayers[i].getRoundScore()*3);
				creditLabels[i].setText(Integer.toString(sortablePlayers[i].getCredits()));
				
				// Update round scores, this round won, and round kills
				sortablePlayers[i].setRoundScore(0);
				roundScoreLabels[i].setText(Integer.toString(0));
				sortablePlayers[i].setThisRoundWon(0);
				thisRoundLabels[i].setText(Integer.toString(0));
				sortablePlayers[i].setRoundKills(0);
				roundKillsLabels[i].setText(Integer.toString(0));
				addedScore = true;
				if (finalRound) {
					addedCredits = true; // To skip over adding bonus credits
					finalSort = true;
					nextButton.setText("Standings");
				}
			}
			
		} else if (!addedCredits) {
			for (int i=0; i<sortablePlayers.length; i++) {
				// Update credits
				sortablePlayers[i].setCredits(sortablePlayers[i].getCredits() + sortablePlayers[i].getBonusCredits());
				creditLabels[i].setText(Integer.toString(sortablePlayers[i].getCredits()));
				// Update round scores
				sortablePlayers[i].setBonusCredits(0);
				randCreditLabels[i].setText(Integer.toString(0));
				addedCredits = true;
			}
			
			// Rearrange based on points (or whatever the scoring type is)
		} else if (!finalSort) {
			sortPlayersAndUpdateColors();
			nextButton.setText("Next Round");
			finalSort = true;
			
		} else {
			addedScore = false;
			addedCredits = false;
			finalSort = false;
			nextButton.setText("Continue");
			if (!finalRound)
				game.nextRound();
			else {
				sortPlayersAndUpdateColors();
				MainGame.gameState = STATE.PostGame;
			}
		}
	}
	
	
}
