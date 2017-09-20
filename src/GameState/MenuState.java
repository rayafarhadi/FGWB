package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * A state the user first sees when launching the game. There are options to
 * start the game, read instructions, see credits and quit the game
 * @author Raya and Connor
 * @version 11
 */
public class MenuState extends GameState
{
	// Background
	private BufferedImage menuScreen;

	// Options
	private int currentChoice = 0;
	private String[] options = { "Start", "Level Select", "Instructions",
			"Credits", "Quit" };
	private final int START = 0;
	private final int LEVEL_SELECT = 1;
	private final int INSTRUCTIONS = 2;
	private final int CREDITS = 3;
	private final int QUIT = 4;

	// Fonts
	private Color titleColour, shadowColour;
	private Font titleFont, titleShadowFont, font, shadowFont;

	/**
	 * Creates a new menu state to be used in the GameStateManager
	 * @param gsm a reference to the GameStateManager
	 * @throws IOException if the image in the init() method doesn't load
	 *             properly
	 */
	public MenuState(GameStateManager gsm) throws IOException
	{
		this.gsm = gsm;
		init();
	}

	/**
	 * Initializes the state by loading the background and the fonts
	 */
	public void init() throws IOException
	{
		menuScreen = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/MenuScreen.png"));

		titleColour = Color.ORANGE;
		shadowColour = Color.GRAY;

		// Try to use the ringbearer font for the title and menu options
		try
		{
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass()
					.getResourceAsStream(
							"/Fonts/RINGBEARER.TTF")));

			titleFont = new Font("RingBearer", Font.PLAIN, 50);
			titleShadowFont = new Font("RingBearer", Font.PLAIN, 52);
			font = new Font("RingBearer", Font.BOLD, 50);
			shadowFont = new Font("RingBearer", Font.BOLD, 52);
		}
		catch (IOException | FontFormatException e)
		{
			System.err.println("RingBearer font could not be used!");
			font = new Font("Arial", Font.PLAIN, 45);
			titleFont = new Font("Arial", Font.PLAIN, 50);
		}
	}

	/**
	 * Changes the game state based on what menu option was selected
	 * @throws IOException if the GameStateManager could not change states
	 *             properly
	 */
	private void select() throws IOException
	{
		GamePanel.mouse.unPress();
		if (currentChoice == START)
		{
			gsm.setLevel(1);
			gsm.setState(GameStateManager.LEVEL_STATE);
		}
		else if (currentChoice == LEVEL_SELECT)
		{
			gsm.setState(GameStateManager.LEVEL_SELECT_STATE);
		}
		else if (currentChoice == INSTRUCTIONS)
		{
			gsm.setState(GameStateManager.INSTRUCTION_STATE);
		}
		else if (currentChoice == CREDITS)
		{
			gsm.setState(GameStateManager.CREDITS_STATE);
		}
		else if (currentChoice == QUIT)
		{
			System.exit(0);
		}
	}

	/**
	 * If the mouse in within the area of the menu options, checks to see which
	 * menu choice is selected by the user
	 * @throws IOException if select() could not change the game state
	 */
	public void update() throws IOException
	{
		if (GamePanel.mouse.isInArea())
		{
			currentChoice = GamePanel.mouse.choice();
			if (GamePanel.mouse.pressed())
				select();
		}
	}

	/**
	 * Draws the menu screen consisting of the background, title and
	 * (un)highlighted menu options
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		// Draw Background
		g.drawImage(menuScreen, 0, 0, 1024, 768, null);

		// Draw Title
		g.setColor(shadowColour);
		g.setFont(titleShadowFont);
		g.drawString("Fire Grill and Water Buoy", GamePanel.WIDTH / 4 - 34, 64);
		g.setColor(titleColour);
		g.setFont(titleFont);
		g.drawString("Fire Grill and Water Buoy", GamePanel.WIDTH / 4 - 32, 64);

		// Draw menu options
		for (int index = 0; index < options.length; index++)
		{
			g.setFont(shadowFont);
			g.setColor(shadowColour);
			g.drawString(options[index], GamePanel.WIDTH / 2 - 130,
					256 + (index * 96));

			if (index == currentChoice)
			{
				g.setFont(font);
				g.setColor(Color.BLUE);
			}
			else
			{
				g.setFont(font);
				g.setColor(Color.RED);
			}
			g.drawString(options[index], GamePanel.WIDTH / 2 - 128,
					256 + (index * 96));
		}

	}

	/**
	 * Navigates the menu options based on the keys pressed
	 * @param key the key pressed
	 * @throws IOException if the GameStateManager could not change states
	 *             properly
	 */
	public void keyPressed(int key) throws IOException
	{
		try
		{
			// Selects the current choice when the user pressed the enter key
			if (key == KeyEvent.VK_ENTER)
			{
				select();
			}
			// Moves the selected menu choice up one when the user presses the
			// up
			// arrow key. If there are no more choices above the current choice,
			// it wraps to the first choice at the bottom of the menu
			else if (key == KeyEvent.VK_UP)
			{
				currentChoice--;
				if (currentChoice == -1)
				{
					currentChoice = options.length - 1;
				}
			}
			// Moves the selected menu choice down one when the user presses the
			// down arrow key. If there are no more choices below the current
			// choice it wraps to the first choice at the top of the menu
			else if (key == KeyEvent.VK_DOWN)
			{
				currentChoice++;
				if (currentChoice == options.length)
				{
					currentChoice = 0;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	// Below is unused
	public void keyReleased(int key)
	{

	}
}