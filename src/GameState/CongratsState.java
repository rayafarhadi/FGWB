package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * A state that displays a congratulations message and the credits after the
 * player beats all of the levels
 * @author Raya
 * @version 1
 */
public class CongratsState extends GameState
{
	BufferedImage image;

	/**
	 * The constructor. Gets the GameStateManager that is being used and runs
	 * the init method
	 * @param gsm The GameStateManager being used to change states
	 * @throws IOException if the image the init method is reading isnt found
	 */
	public CongratsState(GameStateManager gsm) throws IOException
	{
		this.gsm = gsm;
		init();
	}

	/**
	 * Initializes the state by loading the congratulations screen
	 */
	public void init() throws IOException
	{
		image = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/CongratsScreen.png"));
	}

	/**
	 * Updates the state by checking for the user clicking on the back button
	 */
	public void update() throws IOException
	{
		boolean backToMenu = GamePanel.mouse.goToMenu();
		if (backToMenu)
		{
			GamePanel.mouse.resetGoToMenu();
			gsm.setState(GameStateManager.MENU_STATE);
		}
	}

	/**
	 * Draws the congratulations screen
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(image, 0, 0, null);
	}

	/**
	 * Takes the user back to the main menu when they press enter
	 */
	public void keyPressed(int key) throws IOException
	{
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