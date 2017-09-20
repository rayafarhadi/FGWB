package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A door is the exit of the game. It is not a tile because it takes up more
 * than one tile space
 * @author Connor
 * @version 4
 */
public class Door
{
	// Coordinates
	private int x, y;
	private Rectangle door;

	// Colour
	private int doorColour;

	// Images
	private BufferedImage doorOpenImage, doorClosedImage;

	// Checks
	private boolean open;

	/**
	 * Makes a new door object that players go through to end the level
	 * @param x the x coordinate of the door
	 * @param y the y coordinate of the door
	 * @param colour 1 for red -1 for blue
	 * @throws IOException if the images cannot be properly loaded
	 */
	public Door(int x, int y, int colour) throws IOException
	{
		this.x = x;
		this.y = y;
		doorColour = colour;
		open = false;
		doorOpenImage = Doors.getImage(doorColour);
		doorClosedImage = Doors.getImage(Doors.CLOSED);

		door = new Rectangle(x, y + 14, 32, 50);
	}

	/**
	 * Draws the door at its x and y coordinates
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		if (!open)
			g.drawImage(doorOpenImage, x, y, null);
		else
			g.drawImage(doorClosedImage, x, y, null);
	}

	/**
	 * Checks if the given x and y coordinates are in this door and updates the
	 * door's state accordingly (open if they are in the door, closed if not)
	 * @param x the given x coordinate
	 * @param y the given y coordinate
	 */
	public void update(int x, int y)
	{
		if (door.contains(x, y))
		{
			open();
		}
		else
		{
			close();
		}
	}

	/**
	 * Opens the door
	 */
	private void open()
	{
		open = true;
	}

	/**
	 * Closes the door
	 */
	private void close()
	{
		open = false;
	}

	/**
	 * Returns if this door is open
	 * @return if this door is open
	 */
	public boolean isOpen()
	{
		return open;
	}

}