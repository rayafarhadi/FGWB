package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A treasure is a coin or diamond which grants the player points when picking
 * them up. Once picked up, a treasure disappears from the map. A treasure can
 * be defined from the Treasures class
 * @author Connor
 * @version 6
 */
public class Treasure
{

	// Coordinates
	private int x, y;

	// Type (diamond or coin)
	private int type;

	// Amount of points
	private int points;

	// Image
	private BufferedImage treasureImage;

	// Collision box
	private Rectangle treasure;

	/**
	 * Creates a new treasure object of the specified type at the given x and y
	 * coordinates
	 * @param type the type of treasure as defined by the Treasures class
	 * @param x the x coordinate of this treasure
	 * @param y the y coordinate of this treasure
	 * @throws IOException if the image could not be successfully passed from
	 *             the Treasures class
	 */
	public Treasure(int type, int x, int y) throws IOException
	{
		this.type = type;
		this.x = x;
		this.y = y;
		treasure = new Rectangle(x, y, 32, 32);
		treasureImage = Treasures.getImage(type);
		points = Treasures.findPoints(type);
	}

	/**
	 * Checks to see if the object at the given x and y values intersects with
	 * this treasure
	 * @param x the x position of the object that is checking to see if it has
	 *            collided
	 * @param y the y position of the object that is checking to see if it has
	 *            collided
	 * @return if the given x and y values has are in this treasure
	 */
	public boolean contains(int x, int y)
	{
		return treasure.contains(x, y);
	}

	/**
	 * Called when the player takes this treasure
	 * @return the number of points this treasure is worth
	 */
	public int take()
	{
		TileMap.treasures.remove(this);
		return points;
	}

	/**
	 * Returns the type of treasure this treasure is
	 * @return the type of treasure this treasure is
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * Returns if this treasure is a diamond or not
	 * @return if this treasure is a diamond or not
	 */
	public boolean isDiamond()
	{
		if (type == Treasures.DIAMOND_BLUE || type == Treasures.DIMOND_RED)
			return true;
		else
			return false;
	}

	/**
	 * Draws the treasure at its x and y coordinates
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(treasureImage, x, y, null);
	}

}