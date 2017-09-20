package GameState;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * This state is used to tell the user how to play the game by showing a series
 * of screens that tell the user information about the game
 * @author Connor
 */
public class InstructionState extends GameState
{
	private GameStateManager gsm;
	private BufferedImage[] instructions;
	private BufferedImage currentScreen;
	public static int page;

	/**
	 * Creates a new instructions state to be used in the GameStateManager
	 * @param gsm a reference to the GameStateManager
	 * @throws IOException if the images in the init() method don't load properly
	 */
	public InstructionState(GameStateManager gsm) throws IOException
	{
		this.gsm = gsm;
		init();
	}

	/**
	 * Loads the instructional images to be used in instructions in the
	 * instructions screen ... INSTRUCTIONS Also initializes some variables
	 * @throws IOException if the images could not be loaded properly
	 */
	public void init() throws IOException
	{
		instructions = new BufferedImage[7];
		// Load Instruction screen images
		instructions[0] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 1.png"));
		instructions[1] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 2.png"));
		instructions[2] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 3.png"));
		instructions[3] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 4.png"));
		instructions[4] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 5.png"));
		instructions[5] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 6.png"));
		instructions[6] = ImageIO.read(getClass().getResourceAsStream(
				"/Menu/Instructions/Instructions 7.png"));

		page = 0;
		currentScreen = instructions[page];
	}

	/**
	 * Checks to see if the user wants to go to the next page, previous page or
	 * is done with the instructions. Changes the current page depending on the
	 * choice
	 * @throws IOException if the GameStateManager could not change the state
	 *             properly
	 */
	public void update() throws IOException
	{
		if (GamePanel.mouse.next() == true)
		{
			page++;
			currentScreen = instructions[page];
			GamePanel.mouse.setNextFalse();
		}
		else if (GamePanel.mouse.previous() == true)
		{
			page--;
			currentScreen = instructions[page];
			GamePanel.mouse.setPreviousFalse();
		}
		else if (GamePanel.mouse.done())
		{
			page = 0;
			currentScreen = instructions[page];
			GamePanel.mouse.resetDone();
			GamePanel.mouse.unPress();
			gsm.setState(GameStateManager.MENU_STATE);
		}
	}

	/**
	 * Draws the current screen centered
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(currentScreen, 128, 96, null);
	}

	/**
	 * Process events based on the key that was pressed. Right arrow key goes to
	 * the next page and, if it is the last page the game state goes back to the
	 * menu, left arrow key presses go to the previous page(if there is one)
	 */
	public void keyPressed(int key) throws IOException
	{
		if (key == KeyEvent.VK_RIGHT)
		{
			if (page != 6)
			{
				page++;
				currentScreen = instructions[page];
			}
			else
			{
				page = 0;
				currentScreen = instructions[page];
				gsm.setState(GameStateManager.MENU_STATE);
			}
		}
		else if (key == KeyEvent.VK_LEFT && page != 0)
		{
			page--;
			currentScreen = instructions[page];
		}
	}
	
	//Below is unused
	public void keyReleased(int key)
	{

	}
}