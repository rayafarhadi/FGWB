package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Player.Player;
import TileMap.TileMap;
import TileMap.Treasure;

/**
 * The level state is the main state the player is in. It runs the level which
 * involves tasks such as checking collisions, updating player positions and
 * drawing.
 * @author Raya and Connor
 * @version 29
 */
public class LevelState extends GameState
{
	// Tilemap
	private static TileMap tileMap;

	// Player
	private static Player grill;
	private static Player buoy;
	private static Point buoyStart, grillStart;

	// Background
	private BufferedImage bg;

	private static int level;

	// hud
	private Font hudFont;
	private BufferedImage redDiamondEmpty, redDiamondFull;
	private BufferedImage blueDiamondEmpty, blueDiamondFull;
	private long levelTime, pauseStartTime, pauseEndTime;
	private static boolean redDiamond, blueDiamond;
	private static long totalPauseTime;
	private static long time;
	private static int levelScore;

	// menu screen
	private boolean end, paused, pauseTimeStarted;
	private BufferedImage menuScreen;
	private String[][] options = { { "Next Level", "Main Menu", "    Quit" },
			{ "  Resume", "Main Menu", "    Quit" } };
	private int currentChoice;
	private Font font, shadowFont;
	private boolean drawScores;
	private boolean updateScores;

	// menus
	private final int END = 0;
	private final int PAUSE = 1;

	// choices
	private final int RESUME = 0;
	private final int MAIN_MENU = 1;
	private final int QUIT = 2;
	private final int NEXT_LEVEL = 0;

	/**
	 * Initializes some variables and calls init()
	 * @param gsm a reference to the GameStateManager
	 * @param level the level to be used
	 * @throws IOException if calls in init() do not load properly
	 */
	public LevelState(GameStateManager gsm, int level) throws IOException
	{
		this.gsm = gsm;
		LevelState.level = level;
		init();
	}

	/**
	 * Initializes the level by loading the images and placing Buttons,
	 * Switches, Treasures, Tiles and Players where they need to go based on the
	 * .lvl file
	 */
	public void init() throws IOException
	{
		System.out.println("load");
		bg = ImageIO.read(getClass().getResourceAsStream(
				"/Backgrounds/CastleBackground.png"));

		tileMap = new TileMap(32);
		tileMap.loadTiles("/Tilesets/GrassTileSet.png");
		tileMap.loadMap("/Levels/Level" + level + ".lvl");
		tileMap.setPosition(0, 0);

		buoyStart = tileMap.getBuoyStart();
		grillStart = tileMap.getGrillStart();

		redDiamond = false;
		blueDiamond = false;

		buoy = new Player(tileMap, "/Players/Buoy.png", 28, 28, Player.BUOY);
		buoy.setPosition(buoyStart.x, buoyStart.y);

		grill = new Player(tileMap, "/Players/Grill.png", 28, 28,
				Player.GRILL);
		grill.setPosition(grillStart.x, grillStart.y);

		// Load the font
		try
		{
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass()
					.getResourceAsStream(
							"/Fonts/MICKEY.TTF")));

			hudFont = new Font("Mickey", Font.BOLD, 50);
		}
		catch (IOException | FontFormatException e)
		{
			System.err.println("Mickey font could not be used!");
			hudFont = new Font("Comic Sans MS", Font.BOLD, 50);
		}

		// Load the diamonds
		redDiamondEmpty = ImageIO.read(getClass().getResourceAsStream(
				"/Items/hudGemRedEmpty.png"));
		blueDiamondEmpty = ImageIO.read(getClass().getResourceAsStream(
				"/Items/hudGemBlueEmpty.png"));
		redDiamondFull = ImageIO.read(getClass().getResourceAsStream(
				"/Items/hudGemRedFull.png"));
		blueDiamondFull = ImageIO.read(getClass().getResourceAsStream(
				"/Items/hudGemBlueFull.png"));

		// Load pause/end level screen
		menuScreen = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/MenuScreen.png"));

		// Reset variables
		time = System.currentTimeMillis();
		pauseStartTime = 0;
		pauseEndTime = 0;
		totalPauseTime = 0;
		levelTime = 0;
		levelScore = 0;
		end = false;
		paused = false;
		pauseTimeStarted = false;
		currentChoice = 0;

		// Set the fonts
		// Try to use the ringbearer font for the menu options
		try
		{
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass()
					.getResourceAsStream(
							"/Fonts/RINGBEARER.TTF")));

			font = new Font("RingBearer", Font.BOLD, 35);
			shadowFont = new Font("RingBearer", Font.BOLD, 37);
		}
		catch (IOException | FontFormatException e)
		{
			System.err.println("RingBearer font could not be used!");
			font = new Font("Arial", Font.BOLD, 35);
			shadowFont = new Font("Arial", Font.BOLD, 37);
		}

		drawScores = true;
		updateScores = true;
	}

	/**
	 * Sets the state selected by the user
	 * @throws IOException if the GameStateManager can't set the state
	 */
	private void select() throws IOException
	{
		GamePanel.mouse.unPress();

		if (end)
		{
			if (currentChoice == NEXT_LEVEL)
			{
				drawScores = false;
				GamePanel.endLevel(level, levelScore, (int) levelTime);
				pauseTimeStarted = false;
			}
			else if (currentChoice == MAIN_MENU)
			{
				gsm.setState(GameStateManager.MENU_STATE);
			}
			else if (currentChoice == QUIT)
			{
				System.exit(0);
			}
		}
		else if (paused)
		{
			if (currentChoice == RESUME)
			{
				pauseEndTime = System.currentTimeMillis() - pauseStartTime;
				totalPauseTime += pauseEndTime;
				paused = false;
				pauseTimeStarted = false;
			}
			else if (currentChoice == MAIN_MENU)
			{
				gsm.setState(GameStateManager.MENU_STATE);
			}
			else if (currentChoice == QUIT)
			{
				System.exit(0);
			}
		}
	}

	/**
	 * Resets the level to what it was like when the level started
	 * @throws IOException if the tile map could not reload properly
	 */
	public static void reset() throws IOException
	{
		buoy.setPosition(buoyStart.getX(), buoyStart.getY());
		grill.setPosition(grillStart.getX(), grillStart.getY());
		levelScore = 0;
		redDiamond = false;
		blueDiamond = false;

		// Clear previous treasures and obstacles
		TileMap.treasures.clear();
		TileMap.buttons.clear();
		TileMap.switches.clear();

		tileMap.loadMap("/Levels/Level" + level + ".lvl");
		tileMap.setPosition(0, 0);
	}

	/**
	 * Updates the level by changing player positions, checking for obstacle and
	 * treasure collisions and incrementing time and score
	 * @throws IOException if an I/O exception of some sort has occurred
	 */
	public void update() throws IOException
	{
		if (!end && !paused)
		{
			// Update buoy and grill
			buoy.update();
			tileMap.setPosition(buoy.getX(), buoy.getY());

			grill.update();
			tileMap.setPosition(grill.getX(), grill.getY());

			// Check for collisions
			// Check for collisions with switches
			for (int aSwitch = 0; aSwitch < TileMap.switches.size(); aSwitch++)
			{
				TileMap.switches.get(aSwitch).update(buoy.getX(), buoy.getY(),
						grill.getX(), grill.getY());

			}

			// /Check for collisions with buttons
			for (int aButton = 0; aButton < TileMap.buttons.size(); aButton++)
			{
				TileMap.buttons.get(aButton).update(buoy.getX(), buoy.getY(),
						grill.getX(), grill.getY());
			}

			// Check for collisions with doors
			TileMap.blueDoor.update(buoy.getX(), buoy.getY());
			TileMap.redDoor.update(grill.getX(), grill.getY());

			// Check for collisions with treasures
			for (int aTreasure = 0; aTreasure < TileMap.treasures.size(); aTreasure++)
			{
				Treasure treasure = TileMap.treasures.get(aTreasure);
				if (treasure.contains(buoy.getX(), buoy.getY()))
				{
					// Buoy got a treasure
					if (buoy.canTake(treasure.getType()))
					{
						levelScore += treasure.take();
						if (treasure.isDiamond())
						{
							blueDiamond = true;
						}
					}
				}
				if (treasure.contains(grill.getX(), grill.getY()))
				{
					// Grill got a treasure
					if (grill.canTake(treasure.getType()))
					{
						levelScore += treasure.take();
						if (treasure.isDiamond())
						{
							redDiamond = true;
						}
					}
				}
			}

			// Check for the end of the level (when both players are in front of
			// their respective doors)
			{
				if (TileMap.redDoor.isOpen() && TileMap.blueDoor.isOpen())
				{
					// end the level
					end = true;
				}
			}

			// Update the time
			levelTime = (System.currentTimeMillis() - time - totalPauseTime) / 1000;
		}
		else if (paused)
		{
			// Check options
			if (GamePanel.mouse.isInArea())
			{
				if (GamePanel.mouse.choice() == RESUME)
					currentChoice = RESUME;
				else if (GamePanel.mouse.choice() == MAIN_MENU)
					currentChoice = MAIN_MENU;
				else if (GamePanel.mouse.choice() == QUIT)
					currentChoice = QUIT;

				if (GamePanel.mouse.pressed())
					select();
			}

			// Check how long the game has been paused so that it doesnt get
			// added to the total game time
			if (!pauseTimeStarted)
			{
				pauseStartTime = System.currentTimeMillis();
				pauseTimeStarted = true;
			}
		}
		else
		{
			if (updateScores)
			{
				drawScores = false;
				GamePanel.updateLevelStats(level, levelScore, (int) levelTime);
				drawScores = true;
				updateScores = false;
			}

			if (level <= GamePanel.TOTAL_LEVELS)
			{
				if (GamePanel.mouse.isInArea())
				{
					if (GamePanel.mouse.choice() == RESUME)
						currentChoice = RESUME;
					else if (GamePanel.mouse.choice() == MAIN_MENU)
						currentChoice = MAIN_MENU;
					else if (GamePanel.mouse.choice() == QUIT)
						currentChoice = QUIT;

					if (GamePanel.mouse.pressed())
						select();
				}
			}
			else
			{
				GamePanel.endLevel(level, levelScore, (int) levelTime);
			}
		}
	}

	/**
	 * Draws everything in the level including players, backgrounds, tiles,
	 * obstacles, treasures and HUD
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		// Draw background
		g.drawImage(bg, 0, 0, null);

		// Draw tileMap
		tileMap.draw(g);

		// Draw player
		buoy.draw(g);
		grill.draw(g);

		// Draw items
		/*
		 * for (int i = 0; i < items.size(); i++) { items.get(i).draw(g); }
		 */

		// HUD STUFF!
		// Draw the score
		g.setFont(hudFont);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(levelScore), 10, 50);

		// Draw the diamonds
		if (redDiamond)
			g.drawImage(redDiamondFull, 950, 20, null);
		else
			g.drawImage(redDiamondEmpty, 950, 20, null);
		if (blueDiamond)
			g.drawImage(blueDiamondFull, 900, 20, null);
		else
			g.drawImage(blueDiamondEmpty, 900, 20, null);

		// Draw time in seconds
		g.drawString(String.valueOf(levelTime), 512, 50);

		// Draw the end screen when the level is over
		if (end)
		{
			// Draw the end screen background
			g.drawImage(menuScreen, 128, 96, 768, 576, null);

			// Draw level over information like score and time
			g.setFont(hudFont);
			g.setColor(Color.BLACK);
			g.drawString(String.format("%-12s%4d", "Level Time:", levelTime),
					250,
					160);
			g.drawString(String.format("%-12s%5d", "Level Score:", levelScore),
					207, 240);

			// Draw menu options
			for (int index = 0; index < options[END].length; index++)
			{
				g.setColor(Color.BLACK);
				g.setFont(shadowFont);
				g.drawString(options[END][index], GamePanel.WIDTH / 2 - 96,
						425 + (index * 96));

				g.setFont(font);
				if (index == currentChoice)
				{
					g.setColor(Color.BLUE);
				}
				else
				{
					g.setColor(Color.RED);
				}
				g.drawString(options[END][index], GamePanel.WIDTH / 2 - 92,
						425 + (index * 96));
			}

			// Draw high scores
			if (drawScores)
			{
				String[] names = new String[3];
				int[] scores = new int[3];
				int[] times = new int[3];

				BufferedReader scoreReader = null;
				try
				{
					scoreReader = new BufferedReader(new FileReader(
							"Highscores/Level" + level + ".hs"));
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}

				for (int player = 0; player < 3; player++)
				{
					String line = null;
					try
					{
						line = scoreReader.readLine();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					names[player] = line.split(" ")[0].replace('_', ' ');
					if (names[player].length() > 9)
					{
						names[player] = names[player].substring(0, 8);
					}
					scores[player] = Integer.parseInt(line.split(" ")[1]);
					times[player] = Integer.parseInt(line.split(" ")[2]);
				}
				g.setFont(shadowFont);
				g.setColor(Color.BLACK);
				g.drawString("Highscores", 396, 300);

				for (int highScore = 0; highScore < 3; highScore++)
				{
					g.drawString(names[highScore], 150 + (275 * highScore), 350);
					g.drawString(String.valueOf(scores[highScore]),
							150 + (275 * highScore),
							382);
					g.drawString(String.valueOf(times[highScore]),
							240 + (275 * highScore),
							382);
				}

				g.setFont(font);
				g.setColor(Color.ORANGE);

				g.drawString("Highscores", 400, 300);
				for (int highScore = 0; highScore < 3; highScore++)
				{
					g.drawString(names[highScore], 154 + (275 * highScore), 350);
					g.drawString(String.valueOf(scores[highScore]),
							154 + (275 * highScore),
							382);
					g.drawString(String.valueOf(times[highScore]),
							242 + (275 * highScore),
							382);
				}
			}
		}
		else if (paused)
		{
			// Draw the end screen background
			g.drawImage(menuScreen, 128, 96, 768, 576, null);

			// Draw level over information like score and time
			g.setFont(hudFont);
			g.setColor(Color.BLACK);
			g.drawString(String.format("%-12s%4d", "Level Time:", levelTime),
					250,
					160);
			g.drawString(String.format("%-12s%5d", "Level Score:", levelScore),
					207, 240);

			// Draw menu options
			for (int index = 0; index < options[PAUSE].length; index++)
			{
				g.setColor(Color.BLACK);
				g.setFont(shadowFont);
				g.drawString(options[PAUSE][index], GamePanel.WIDTH / 2 - 96,
						425 + (index * 96));

				g.setFont(font);
				if (index == currentChoice)
				{
					g.setColor(Color.BLUE);
				}
				else
				{
					g.setColor(Color.RED);
				}
				g.drawString(options[PAUSE][index], GamePanel.WIDTH / 2 - 92,
						425 + (index * 96));
			}
		}

	}

	/**
	 * Processes events based on the keys pressed. WAD moves the grill and up,
	 * left and right arrow keys move the buoy. R resets the level
	 * @param key the key pressed
	 * @throws IOException if the level could not be reset properly
	 */
	public void keyPressed(int key) throws IOException
	{
		if (!end && !paused)
		{
			if (key == KeyEvent.VK_LEFT)
			{
				buoy.setLeft(true);
			}

			if (key == KeyEvent.VK_RIGHT)
			{
				buoy.setRight(true);
			}

			if (key == KeyEvent.VK_UP)
			{
				buoy.setJumping(true);
			}

			if (key == KeyEvent.VK_A)
			{
				grill.setLeft(true);
			}

			if (key == KeyEvent.VK_D)
			{
				grill.setRight(true);
			}

			if (key == KeyEvent.VK_W)
			{
				grill.setJumping(true);
			}

			if (key == KeyEvent.VK_R)
			{
				reset();
			}

			if (key == KeyEvent.VK_P)
			{
				paused = true;
			}
		}
		else
		{
			// Selects the current choice when the user pressed the enter key
			if (key == KeyEvent.VK_ENTER)
			{
				try
				{
					select();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			// Moves the selected menu choice up one when the user presses the
			// up arrow key. If there are no more choices above the current
			// choice it wraps to the first choice at the bottom of the menu
			else if (key == KeyEvent.VK_UP)
			{
				currentChoice--;
				if (currentChoice == -1)
				{
					currentChoice = options[END].length - 1;
				}
			}
			// Moves the selected menu choice down one when the user presses the
			// down arrow key. If there are no more choices below the current
			// choice it wraps to the first choice at the top of the menu
			else if (key == KeyEvent.VK_DOWN)
			{
				currentChoice++;
				if (currentChoice == options[END].length)
				{
					currentChoice = 0;
				}
			}
		}
	}

	/**
	 * Processes events based on the keys released. Mostly stops Players when
	 * the user stops pressing their respective move keys
	 */
	public void keyReleased(int key)
	{
		if (key == KeyEvent.VK_LEFT)
		{
			buoy.setLeft(false);
		}

		if (key == KeyEvent.VK_RIGHT)
		{
			buoy.setRight(false);
		}

		if (key == KeyEvent.VK_A)
		{
			grill.setLeft(false);
		}

		if (key == KeyEvent.VK_D)
		{
			grill.setRight(false);
		}
	}
}