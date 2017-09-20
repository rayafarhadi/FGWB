package TileMap;

import java.awt.image.BufferedImage;

/**
 * A tile is on space in the level that can be either empty, dirt/grass, a
 * treasure, a liquid or filled with an obstacle
 * @author Raya
 * @version 4
 */
public class Tile
{
	// Image
	private BufferedImage image;

	// Type
	private int type;

	// Tiles
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;

	// Treasures
	public static final int COIN = 2;
	public static final int GEM = 3;

	// Liquids
	public static final int LAVA = 4;
	public static final int WATER = 5;
	public static final int ACID = 6;

	/**
	 * Initializes the variables
	 * @param image
	 * @param type
	 */
	public Tile(BufferedImage image, int type)
	{
		this.image = image;
		this.type = type;
	}

	/**
	 * Returns the tile's image
	 * @return the tile's image
	 */
	public BufferedImage getImage()
	{
		return image;
	}

	/**
	 * Returns the tile's type
	 * @return the tile's type
	 */
	public int getType()
	{
		return type;
	}
}