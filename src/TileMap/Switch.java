package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * A switch is an object that can be turned on and off. It keeps track of its
 * position and its state(on, off). It can be checked to see if something is
 * flipping it and reacts accordingly (if someone is flipping it while it is
 * off, it can be turned on). It also keeps track of what type of switch it is
 * based on the types defined in the Obstacles class
 * @author Connor
 * @version 10
 */
public class Switch
{
	// Coordinates
	private int switchX, switchY, laserX, laserY;

	// Images
	private BufferedImage switchOn, switchOff;
	private BufferedImage laserImage;

	// Checks
	private boolean isSwitchOn;
	private boolean laserOn;

	// Used to make sure that the switch does not get flipped more than once
	private boolean changed;

	// Collision
	private Rectangle laser;
	private Rectangle theSwitch;
	private int laserSize;

	private int switchPlayer;

	private final int BUOY = -1;
	private final int BOTH = 0;
	private final int GRILL = 1;

	/**
	 * Makes a new obstacle with a switch and a laser
	 * @param switchType the type of switch as defined by the Obstacles class
	 * @param laserType the type of laser as defined by the Obstacles class
	 * @param switchX the x coordinate of the switch
	 * @param switchY the y coordinate of the switch
	 * @param laserX the x coordinate of the laser
	 * @param laserY the y coordinate of the laser
	 */
	public Switch(int switchType, int laserType, int laserSize,
			int switchX, int switchY, int laserX, int laserY)
	{
		// For drawing
		this.switchX = switchX;
		this.switchY = switchY;
		this.laserX = laserX;
		this.laserY = laserY;
		this.laserSize = laserSize;
		switchOn = Obstacles.getImage(switchType);
		switchOff = Obstacles.getImage(switchType + 1);
		laserImage = Obstacles.getImage(laserType);

		// For drawing and checking collisions
		isSwitchOn = true;
		laserOn = true;

		// For collision
		laser = new Rectangle(laserX, laserY, laserSize * 32, 8);
		theSwitch = new Rectangle(switchX, switchY, 32, 32);

		// Figure out which player affects the switch
		switchPlayer = findPlayer(switchType);

		changed = false;
	}

	/**
	 * Draws the switch and lasers in their current states(on or off)
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		// Draw the switch in its respective state
		if (isSwitchOn)
			g.drawImage(switchOn, switchX, switchY, null);
		else
			g.drawImage(switchOff, switchX, switchY, null);

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
	 * Checks if a player is in the collision box of the switch. If someone is
	 * in the collision box check if the player can use that switch. If they can
	 * use the switch activate/deactivate it
	 * @param bx The buoys x coordinate
	 * @param by The buoys y coordinate
	 * @param gx The grills x coordinate
	 * @param gy The grills y coordinate
	 */
	public void update(int bx, int by, int gx, int gy)
	{
		boolean switchContains = false;

		// Find out if the switch has its player on it
		if (switchPlayer == BUOY)
		{
			switchContains = theSwitch.contains(bx, by);
		}
		else if (switchPlayer == GRILL)
		{
			switchContains = theSwitch.contains(gx, gy);
		}
		else if (switchPlayer == BOTH)
		{
			switchContains = theSwitch.contains(bx, by)
					|| theSwitch.contains(gx, gy);
		}

		// React to a switch depending on if a player is on their switch or not
		if (switchContains)
		{
			// Only flip the switch if it hasn't been flipped already
			if (!changed)
			{
				if (isSwitchOn)
				{
					isSwitchOn = false;
				}
				else
				{
					isSwitchOn = true;
				}
				changed = true;
			}
		}
		else
		{
			// Once the player leaves the switch, they can flip it again
			changed = false;
		}

		// Change the lasers according to how buttons are pressed
		if (isSwitchOn)
		{
			laserOn = true;
		}
		else
		{
			laserOn = false;
		}
	}

	/**
	 * Returns which player the switch is affected by
	 * @param switch the switch in question
	 * @return which player the switch is affected by
	 */
	private int findPlayer(int aSwitch)
	{
		if (aSwitch == Obstacles.SWITCH_BLUE_ON)
		{
			return BUOY;
		}
		else if (aSwitch == Obstacles.SWITCH_GREEN_ON)
		{
			return BOTH;
		}
		else if (aSwitch == Obstacles.SWITCH_RED_ON)
		{
			return GRILL;
		}
		// This should never happen but is here in case input was wrong in the
		// creation of this switch and to satisfy eclipse's nazi warning system
		else
		{
			throw new IllegalArgumentException(
					"There was an error finding the player to match this switch");
		}
	}
}