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
 * This state is accessed through the main menu and is used to select which
 * level the user would like to play.
 * @author Connor and Raya
 * @version 7
 */
public class LevelSelectState extends GameState
{
	private GameStateManager gsm;

	// Images
	private BufferedImage bg, button;

	// Levels
	private int currentLevel;
	private boolean[] levelsCompleted;

	private Font shadowFont, font;

	public LevelSelectState(GameStateManager gsm) throws IOException
	{
		this.gsm = gsm;
		init();
	}

	/**
	 * Initializes the state by loading the fonts, background and button images
	 * and initializing the levelsCompleted array
	 */
	public void init() throws IOException
	{
		bg = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/MenuScreen.png"));
		button = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/LevelSelectButton.png"));

		levelsCompleted = GamePanel.levelsCompleted;
		currentLevel = 0;

		// Try to use the ringbearer font for the menu options
		try
		{
			GraphicsEnvironment ge =
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass()
					.getResourceAsStream("/Fonts/RINGBEARER.TTF")));

			shadowFont = new Font("RingBearer", Font.BOLD, 52);
			font = new Font("RingBearer", Font.BOLD, 50);
		}
		catch (IOException | FontFormatException e)
		{
			System.err.println("RingBearer font could not be used!");
			shadowFont = new Font("Arial", Font.BOLD, 47);
			font = new Font("Arial", Font.BOLD, 45);
		}

	}

	/**
	 * Updates the level state by loading the selected level if the user clicks
	 * on a level or loading the menu if they click on the back button
	 */
	public void update() throws IOException
	{
		int selectedLevelMouse = GamePanel.mouse.getSelectedLevel();
		if (selectedLevelMouse > 0)
		{
			gsm.setLevel(selectedLevelMouse);
			gsm.setState(GameStateManager.LEVEL_STATE);
		}
		boolean backToMenu = GamePanel.mouse.goToMenu();
		if (backToMenu)
		{
			GamePanel.mouse.resetGoToMenu();
			gsm.setState(GameStateManager.MENU_STATE);
		}
	}

	/**
	 * Draws the background, level buttons and writes the title and back button
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(bg, 0, 0, 1024, 768, null);
		g.setFont(shadowFont);
		g.setColor(Color.GRAY);
		g.drawString("Level Select", 348, 100);

		g.setFont(font);
		g.setColor(Color.ORANGE);
		g.drawString("Level Select", 350, 100);

		// Draw 6 rows by 5 columns of level select
		int level = 1;
		for (int row = 0; row < 4; row++)
		{
			for (int col = 0; col < 5; col++)
			{
				g.drawImage(button, col * 154 + 135, row * 96 + 130, 128, 64,
						null);

				if (currentLevel == col + (row * 5))
				{
					g.setColor(Color.RED);
				}
				else
				{
					g.setColor(Color.BLACK);
				}

				g.drawString(String.valueOf(level++), col * 154 + 170,
						row * 96 + 175);

				// Draw a marker for not completed levels
				if (levelsCompleted[(row * 5) + col] == false)
				{
					g.setColor(new Color(160, 160, 160, 150));
					g.fillRect(col * 154 + 135, row * 96 + 130, 128, 64);
				}

				g.setFont(shadowFont);
				g.setColor(Color.GRAY);
				g.drawString("Back", 428, 735);

				g.setFont(font);
				if (currentLevel < 0)
				{
					g.setColor(Color.RED);
				}
				else
				{
					g.setColor(Color.ORANGE);
				}
				g.drawString("Back", 430, 735);

			}
		}

	}

	/**
	 * Checks for the arrow keys to scroll through the possible levels and back
	 * button and checks for enter to select a level or to go back to main menu
	 */
	public void keyPressed(int key) throws IOException
	{
		if (key == KeyEvent.VK_DOWN)
		{
			if (currentLevel > -1)
			{
				currentLevel += 5;
				if (currentLevel > GamePanel.TOTAL_LEVELS - 1)
				{
					currentLevel = -1;
				}
			}
			else if (currentLevel <= -1)
			{
				currentLevel = 0;
			}

			System.out.println(currentLevel);
		}

		if (key == KeyEvent.VK_UP)
		{
			if (currentLevel >= 0)
			{
				currentLevel -= 5;
				if (currentLevel < 0)
				{
					currentLevel = -1;
				}
			}
			else
			{
				currentLevel = GamePanel.TOTAL_LEVELS - 1;
			}

			System.out.println(currentLevel);
		}

		if (key == KeyEvent.VK_RIGHT)
		{
			currentLevel += 1;
			if (currentLevel > GamePanel.TOTAL_LEVELS - 1)
			{
				currentLevel = -1;
			}
			else if (currentLevel < -1)
			{
				currentLevel = 0;
			}

			System.out.println(currentLevel);
		}

		if (key == KeyEvent.VK_LEFT)
		{
			currentLevel -= 1;
			if (currentLevel < -1)
			{
				currentLevel = GamePanel.TOTAL_LEVELS - 1;
			}

			System.out.println(currentLevel);
		}

		if (key == KeyEvent.VK_ENTER)
		{
			if (currentLevel > -1)
			{
				gsm.setLevel(currentLevel + 1);
				gsm.setState(GameStateManager.LEVEL_STATE);
			}
			else
			{
				gsm.setState(GameStateManager.MENU_STATE);
			}
		}
	}

	// Below is unused
	public void keyReleased(int key)
	{

	}

}