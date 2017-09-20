package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * A state in the game where the user finds out who made the game and other
 * exciting adventures of the like
 * @author Connor
 * @version 2
 */
public class CreditsState extends GameState
{
	private GameStateManager gsm;
	private BufferedImage creditsScreen;

	/**
	 * Creates a new credits state to be used in the GameStateManager
	 * @param gsm a reference to the GameStateManager
	 * @throws IOException if the images in the init() method don't load properly
	 */
	public CreditsState(GameStateManager gsm) throws IOException
	{
		this.gsm = gsm;
		init();
	}

	/**
	 * Loads the credits screen image and makes sure the mouse isn't clicked so
	 * the user can take a nice long view of the credits instead of immediately
	 * and unintentionally clicking away from this glorious state
	 * @throws IOException if the images do not load properly
	 */
	public void init() throws IOException
	{
		creditsScreen = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Credits.png"));
		GamePanel.mouse.unPress();
	}

	/**
	 * Checks to see if the user pressed the mouse. If they did, the credits
	 * state is replaced by the menu state
	 * @throws IOException if the GameStateManager cannot change state properly
	 */
	public void update() throws IOException
	{
		if (GamePanel.mouse.pressed())
		{
			GamePanel.mouse.unPress();
			gsm.setState(GameStateManager.MENU_STATE);
		}
	}

	/**
	 * Draw the credits image in the centre of the screen
	 * @param g the graphics used to draw
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(creditsScreen, 128, 96, null);
	}

	/**
	 * Checks to see if the right arrow key or the enter key was pressed and if
	 * it was, sets the state back to the menu state
	 * @param key the keycode for the key being pressed
	 * @throws IOExeption if the GameStateManager cannot change state properly
	 */
	public void keyPressed(int key) throws IOException
	{
		if (key == KeyEvent.VK_RIGHT)
		{
			gsm.setState(GameStateManager.MENU_STATE);
		}
		if (key == KeyEvent.VK_ENTER)
		{
			gsm.setState(GameStateManager.MENU_STATE);
		}
	}
	
	//Below is unused
	public void keyReleased(int key)
	{

	}
}