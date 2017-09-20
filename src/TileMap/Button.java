package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A button is an object that can be pressed and released. It keeps track of its
 * position and its state(on, off). It can be checked to see if something is on
 * it and reacts accordingly (if someone is on it, it can be on). It also keeps
 * track of what type of button it is based on the types defined in the
 * Obstacles class
 * 
 * ON == PRESSED AND BUTTON IS DOWN OFF == UNPRESSED AND BUTTON IS UP
 * @author Connor
 * @version 13
 */
public class Button
{
	// Coordinates
	private int buttonOneX, buttonOneY, buttonTwoX, buttonTwoY, laserX,
			laserY;

	// Images
	private BufferedImage buttonOneOn, buttonOneOff, buttonTwoOn, buttonTwoOff;
	private BufferedImage laserImage;
	private int laserSize;

	// Checks
	private boolean isButtonOneOn, isButtonTwoOn;
	private boolean laserOn;

	private int buttonOnePlayer, buttonTwoPlayer;

	private final int BUOY = -1;
	private final int BOTH = 0;
	private final int GRILL = 1;

	// For collision
	private Rectangle laser;
	private Rectangle buttonOne;
	private Rectangle buttonTwo;

	/**
	 * Creates a new obstacle with two buttons and a laser
	 * @param buttonType the type of button as defined in the obstacles class
	 * @param laserType the type of laser as defined in the obstacles class
	 * @param buttonOneX the x coordinate of the first button
	 * @param buttonOneY the y coordinate of the first button
	 * @param buttonTwoX the x coordinate of the second button
	 * @param buttonTwoY the y coordinate of the second button
	 * @param laserX the x coordinate of the laser
	 * @param laserY the x coordinate of the laser
	 */
	public Button(int buttonOneType, int buttonTwoType, int laserType,
			int laserSize, int buttonOneX,
			int buttonOneY, int buttonTwoX,
			int buttonTwoY, int laserX, int laserY)
	{
		this.buttonOneX = buttonOneX;
		this.buttonOneY = buttonOneY;
		this.buttonTwoX = buttonTwoX;
		this.buttonTwoY = buttonTwoY;
		this.laserX = laserX;
		this.laserY = laserY;
		this.laserSize = laserSize;
		buttonOneOn = Obstacles.getImage(buttonOneType);
		buttonOneOff = Obstacles.getImage(buttonOneType + 1);
		buttonTwoOn = Obstacles.getImage(buttonTwoType);
		buttonTwoOff = Obstacles.getImage(buttonTwoType + 1);
		laserImage = Obstacles.getImage(laserType);
		isButtonOneOn = false;
		isButtonTwoOn = false;
		laserOn = true;

		buttonOnePlayer = findPlayer(buttonOneType);
		buttonTwoPlayer = findPlayer(buttonTwoType);

		// Lasers take up laserSize tiles squares across
		laser = new Rectangle(laserX, laserY, laserSize * 32, 8);
		buttonOne = new Rectangle(buttonOneX, buttonOneY, 32, 32);
		buttonTwo = new Rectangle(buttonTwoX, buttonTwoY, 32, 32);
	}

	/**
	 * Draws the buttons and lasers in their current states(on or off)
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		if (isButtonOneOn)
			g.drawImage(buttonOneOn, buttonOneX, buttonOneY, null);
		else
			g.drawImage(buttonOneOff, buttonOneX, buttonOneY, null);

		if (isButtonTwoOn)
			g.drawImage(buttonTwoOn, buttonTwoX, buttonTwoY, null);
		else
			g.drawImage(buttonTwoOff, buttonTwoX, buttonTwoY, null);

		// Draw the laser
		if (laserOn)
			for (int laser = 0; laser < laserSize; laser++)
				g.drawImage(laserImage, laserX + (laser * 32), laserY, null);
	}

	/**
	 * Finds out if the given x and y coordinates are within the bounds of the
	 * laser
	 * @param x the x value to check if it is inside the laser
	 * @param y the x value to check if it is inside the laser
	 * @return if the given x and y coordinates are within the bounds of the
	 *         laser
	 */
	public boolean hasPlayerInLaser(int x, int y)
	{
		if (laserOn)
		{
			return laser.contains(x, y);
		}
		else
		{
			return false;
		}
	}

	/**
	 * Check which player the button is affected by
	 * @param button The button being checked
	 * @return which player the button is affected by
	 */
	private int findPlayer(int button)
	{
		if (button == Obstacles.BUTTON_BLUE_ON)
		{
			return BUOY;
		}
		else if (button == Obstacles.BUTTON_GREEN_ON)
		{
			return BOTH;
		}
		else if (button == Obstacles.BUTTON_RED_ON)
		{
			return GRILL;
		}
		// This should never happen but is here in case input was wrong in the
		// creation of this button
		else
		{
			throw new IllegalArgumentException(
					"There was an error finding the player to match this button");
		}
	}

	/**
	 * Updates the states of the buttons and the laser (on or off) based on
	 * whether the a player is in their button or not
	 * @param bx the x coordinate of the buoy
	 * @param by the y coordinate of the buoy
	 * @param gx the x coordinate of the grill
	 * @param gy the y coordinate of the grill
	 */
	public void update(int bx, int by, int gx, int gy)
	{
		boolean buttonOneContains = false, buttonTwoContains = false;

		// Find out if either button has their player on it
		if (buttonOnePlayer == BUOY)
		{
			buttonOneContains = buttonOne.contains(bx, by);
		}
		else if (buttonOnePlayer == GRILL)
		{
			buttonOneContains = buttonOne.contains(gx, gy);
		}
		else if (buttonOnePlayer == BOTH)
		{
			buttonOneContains = buttonOne.contains(bx, by)
					|| buttonOne.contains(gx, gy);
		}

		if (buttonTwoPlayer == BUOY)
		{
			buttonTwoContains = buttonTwo.contains(bx, by);
		}
		else if (buttonTwoPlayer == GRILL)
		{
			buttonTwoContains = buttonTwo.contains(gx, gy);
		}
		else if (buttonTwoPlayer == BOTH)
		{
			buttonTwoContains = buttonTwo.contains(bx, by)
					|| buttonTwo.contains(gx, gy);
		}

		// React to a press or release depending on if a player is on their
		// button or not
		if (buttonOneContains)
		{
			isButtonOneOn = true;
		}
		else
		{
			isButtonOneOn = false;
		}
		if (buttonTwoContains)
		{
			isButtonTwoOn = true;
		}
		else
		{
			isButtonTwoOn = false;
		}

		// Change the lasers according to how buttons are pressed
		if (isButtonOneOn)
		{
			laserOn = false;
		}
		else if (isButtonTwoOn)
		{
			laserOn = false;
		}
		else if (!isButtonOneOn && !isButtonTwoOn)
		{
			laserOn = true;
		}
	}
}