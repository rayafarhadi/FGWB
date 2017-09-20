package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GameState.GameStateManager;
import Input.MouseInput;

/**
 * The class that creates the JPanel for the game Gets all of the key events
 * Runs the thread
 * @author Raya and Connor
 * @version 15
 */
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;

	// dimensions
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	// Game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	// Image
	private BufferedImage image;
	private Graphics2D g;

	// GameStateManager
	private static GameStateManager gsm;
	private static int level;

	// Mouse input
	public static MouseInput mouse = new MouseInput();

	// User
	public static int totalScore, totalTime;
	public static boolean[] levelsCompleted;
	private static Highscores highScores;
	private static String name;

	// Levels
	public final static int TOTAL_LEVELS = 20;

	/**
	 * Sets the size and focus of the game panel Adds the mouse listener Prompts
	 * user for their name
	 */
	public GamePanel()
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();

		totalScore = 0;
		levelsCompleted = new boolean[TOTAL_LEVELS];
		level = 1;

		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		highScores = new Highscores();

		String input = JOptionPane.showInputDialog("Please Enter your name");
		if (input != null && input.length() >= 1)
			name = input;
		else
			name = "Player_1";
	}

	/**
	 * Calls the super classes addNotify and initializes the thread if it has
	 * not been done already
	 */
	public void addNotify()
	{
		super.addNotify();
		if (thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	/**
	 * Initializes the graphics and the GameStateManager
	 * @throws IOException if image is equal to null
	 */
	private void init() throws IOException
	{
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager(level);
	}

	/**
	 * Runs the init, update and draw methods. Starts the thread and keeps the
	 * max frames per second at 60
	 */
	public void run()
	{
		try
		{
			init();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		long start;
		long elapsed;
		long wait;

		try
		{
			update();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		while (running)
		{
			start = System.nanoTime();

			try
			{
				update();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			draw(g);

			elapsed = System.nanoTime() - start;
			wait = targetTime - (elapsed / 1000000);
			if (wait < 0)
			{
				wait = 5;
			}

			try
			{
				Thread.sleep(wait);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Calls the GameStateManagers update method
	 * @throws IOException
	 */
	private void update() throws IOException
	{
		gsm.update();
	}

	/**
	 * Draws the screen and calls the GameStateManagers draw method
	 * @param g The graphics to draw with
	 */
	private void draw(Graphics2D g)
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		g2.dispose();

		gsm.draw(g);
	}

	/**
	 * updates the total score and the completed levels then loads the next
	 * level
	 * @param levelCompleted the level that was completed
	 * @throws IOException if the GameStateManager could not set up the next
	 *             level
	 */
	public static void endLevel(int levelCompleted, int score, int time)
			throws IOException
	{
		if (levelCompleted <= TOTAL_LEVELS)
		{
			totalScore += score;
			totalTime += time;
			levelsCompleted[levelCompleted - 1] = true;
			if (completeGame())
			{
				gsm.setState(GameStateManager.CONGRATS_STATE);
			}
			else
			{
				if (levelCompleted == TOTAL_LEVELS)
				{
					gsm.setState(GameStateManager.MENU_STATE);
				}
				else
				{
					gsm.setLevel(levelCompleted + 1);
				}
			}
		}
	}

	/**
	 * Updates the score and time of the level. Also checks if the level has
	 * been completed
	 * @param levelCompleted The level being played
	 * @param score The current score from the level
	 * @param time The current time from the level
	 * @throws IOException
	 */
	public static void updateLevelStats(int levelCompleted, int score, int time)
			throws IOException
	{
		highScores.updateLevelHighScore(name, score, time,
				levelCompleted);
	}

	/**
	 * Checks if the game has been completed. If it has it updates the total
	 * high scores list
	 * @throws IOException
	 */
	private static boolean completeGame() throws IOException
	{
		for (int level = 0; level < levelsCompleted.length; level++)
		{
			if (!levelsCompleted[level])
				return false;
		}

		return true;
	}

	/**
	 * Listens for key pressed events
	 */
	public void keyPressed(KeyEvent key)
	{

		try
		{
			gsm.keyPressed(key.getKeyCode());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Listens for key released events
	 */
	public void keyReleased(KeyEvent key)
	{
		gsm.keyReleased(key.getKeyCode());
	}

	// Below is unused
	public void keyTyped(KeyEvent key)
	{

	}
}