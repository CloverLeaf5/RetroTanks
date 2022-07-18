import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class MainGame extends Canvas implements Runnable {

	private static final long serialVersionUID = -8788727496633556920L;

	public static final int GAME_WIDTH = 640, GAME_HEIGHT = GAME_WIDTH / 4*3;
	public static final String TITLE = "Retro Tanks";
	
	private Thread gameThread;
	private boolean gameRunning = false;
	
	private Terrain terrain;
	private DirtLayer dirtLayer;
	private Tank[] players;
	private HUD hud;
	private AttackHandler attackHandler;
	private Inventory inventory;
	private MainMenu mainMenu;
	private OptionsMenu optionsMenu;
	private StatsScreen statsScreen;
	
	MouseInput mi;

	public static STATE gameState = STATE.MainMenu;
	public static float gravity = 0.08f;
	
	private static int numPlayers = 2;
	private static int currentPlayer = 0;
	private static Scoring scoringType = Scoring.Points;
	private static int numRounds = 3;
	private static int currentRound = 1;
	

	
	// Constructor
	public MainGame() {
		new Window(GAME_WIDTH, GAME_HEIGHT, TITLE, this);
		mainMenu = new MainMenu();
		optionsMenu = new OptionsMenu(this);
	}
	
	public void init() {
		
		currentPlayer = (new Random().nextInt(numPlayers));
		currentRound = 1;
		
		terrain = new Terrain(GAME_WIDTH, GAME_HEIGHT);
		dirtLayer = new DirtLayer(GAME_WIDTH, GAME_HEIGHT, terrain);
		players = new Tank[numPlayers];
		attackHandler = new AttackHandler(terrain, dirtLayer, players);
		
		for (int i=0; i<players.length; i++) {
			float currentX = ((i*2.0f+1.0f)/(players.length*2.0f)) * (float)GAME_WIDTH;
			players[i] = new Tank(i, currentX, attackHandler, terrain);
		}
		
		// Players need the attackHandler, and the attackHandler needs the players
		attackHandler.updatePlayers(players);
		
		mi = new MouseInput(players);
		this.addMouseListener(mi);
		this.addMouseMotionListener(mi);
		
		inventory = new Inventory(players);
		statsScreen = new StatsScreen(players, this);
		hud = new HUD(players, this);
	}
	
	
	// Called by the options menu after new options selected
	public void reinit() {
		
		currentPlayer = (new Random().nextInt(numPlayers));
		currentRound = 1;
		
		terrain = new Terrain(GAME_WIDTH, GAME_HEIGHT);
		dirtLayer = new DirtLayer(GAME_WIDTH, GAME_HEIGHT, terrain);
		players = new Tank[numPlayers];
		attackHandler = new AttackHandler(terrain, dirtLayer, players);
		
		for (int i=0; i<players.length; i++) {
			float currentX = ((i*2.0f+1.0f)/(players.length*2.0f)) * (float)GAME_WIDTH;
			players[i] = new Tank(i, currentX, attackHandler, terrain);
		}
		
		// Players need the attackHandler, and the attackHandler needs the players
		attackHandler.updatePlayers(players);
		mi.updatePlayers(players); // Needed when the options menu calls init() again
		
		
		inventory = new Inventory(players);
		statsScreen = new StatsScreen(players, this);
		hud = new HUD(players, this);
	}
	
	
	// Called when the play again option is clicked
	public void init(boolean replay) {
		
		currentPlayer = (new Random().nextInt(numPlayers));
		currentRound = 1;
		terrain = new Terrain(GAME_WIDTH, GAME_HEIGHT);
		dirtLayer = new DirtLayer(GAME_WIDTH, GAME_HEIGHT, terrain);
		
		String[] names = new String[players.length];
		float[] xLocations = new float[players.length];
		for (int i=0; i<players.length; i++) {
			names[i] = new String(players[i].getName().toCharArray()); // Written this way to get a new allocation
			xLocations[i] = players[i].getLocationX();
			players[i] = new Tank(i, xLocations[i], attackHandler, terrain);
			players[i].setName(names[i]);
		}

		attackHandler = new AttackHandler(terrain, dirtLayer, players);
		
		mi.updatePlayers(players); // Needed when the options menu calls init() again
		
		inventory = new Inventory(players);
		statsScreen = new StatsScreen(players, this);
		hud = new HUD(players, this);
	}
	
	// Started from the Window class after the window initializes
	public synchronized void start() {
		if(gameRunning)
			return;
		
		gameRunning = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	// Kill the thread
	public synchronized void stop() {
		try {
			gameRunning = false;
			gameThread.join();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Game loop borrowed from Teivodov
		// Runs when game thread is started because the game implements Runnable
		public void run() {
			init();
			this.requestFocus();
			double target = 60.0;
			double nsPerTick = 1000000000.0 / target;
			long lastTime = System.nanoTime();
			long timer = System.currentTimeMillis();
			double unprocessed = 0.0;
			int fps = 0;
			int tps = 0;
			boolean canRender = false;
			
			while(gameRunning) {
				long now = System.nanoTime();
				unprocessed += (now - lastTime)/ nsPerTick;
				lastTime = now;
				
				if (unprocessed >= 1) {
					tick();
					//KeyInput.update(); // Must be after tick
					MouseInput.update(); // Must be after tick
					tps++;
					unprocessed--;
					canRender = true;
				} else canRender = false;
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				
				if(canRender) {
					render();
					fps++;
				}
				
				if(System.currentTimeMillis() - 1000 > timer) {
					timer += 1000;
					//System.out.printf("FPS: %d | TPS: %d\n ", fps, tps);
					fps = 0;
					tps = 0;
				}
			}
			System.exit(0);
		}
	
	private void tick() {
		if (gameState == STATE.MainMenu)
			mainMenu.tick();
		
		if (gameState == STATE.OptionsMenu || gameState == STATE.NameInput)
			optionsMenu.tick();
		
		if (gameState == STATE.PlayerInput || gameState == STATE.Firing || gameState == STATE.SureQuit) {
			attackHandler.tick();
			hud.tick();
			for (int i=0; i<players.length; i++)
				players[i].tick();
		}
		
		if (gameState == STATE.Inventory)
			inventory.tick();
		
		if (gameState == STATE.PostGame || gameState == STATE.PostRound)
			statsScreen.tick();
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		
		if (gameState==STATE.MainMenu)
			mainMenu.render(g);
		
		if (gameState == STATE.OptionsMenu || gameState == STATE.NameInput)
			optionsMenu.render(g);
		
		if (gameState==STATE.Inventory)
			inventory.render(g);
		
		if (gameState == STATE.PostGame || gameState == STATE.PostRound)
			statsScreen.render(g);
		
		if (gameState==STATE.Firing || gameState==STATE.PlayerInput || gameState == STATE.SureQuit) {
			for (int i=0; i<players.length; i++)
				players[i].render(g);

			terrain.render(g);
			dirtLayer.render(g);
			attackHandler.render(g);
			hud.render(g);
		}
			
		
		g.dispose();
		bs.show();
	}
	
	public void nextRound() {
		currentRound++;
		
		terrain = new Terrain(GAME_WIDTH, GAME_HEIGHT);
		attackHandler.updateTerrain(terrain);
		for (int i=0; i<players.length; i++)
			players[i].nextRound(terrain);
		gameState = STATE.PlayerInput;
	}
	
	
	public static float clamp(float value, float min, float max) {
		if (value>=max) return max;
		else if (value<=min) return min;
		else return value;
	}
	
	public static int getCurrentPlayer() {
		return currentPlayer;
	}
	
	public static void setCurrentPlayer(int i) {
		currentPlayer = i;
	}
	
	
	public static int getNumberOfPlayers() {
		return numPlayers;
	}
	
	public static void setNumPlayers(int numPlayers) {
		MainGame.numPlayers = numPlayers;
	}

	public static Scoring getScoringType() {
		return scoringType;
	}

	public static void setScoringType(Scoring scoringType) {
		MainGame.scoringType = scoringType;
	}

	public static int getNumRounds() {
		return numRounds;
	}

	public static void setNumRounds(int numRounds) {
		MainGame.numRounds = numRounds;
	}
	
	public static int getCurrentRound() {
		return currentRound;
	}
	
	public static void setCurrentRound(int currentRound) {
		MainGame.currentRound = currentRound;
	}

	public Tank[] getPlayerArray() {
		return players;
	}
	
	public static void main(String[] args) {
		new MainGame(); // First starts the Window which then starts the game loop
	}

}
