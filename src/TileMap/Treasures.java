package TileMap;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class defines a set of treasures to be used in the game as numbers while
 * maintaining an array of images that those numbers correspond to in the array.
 * A specific reference to an image can be obtained using the getImage method
 * @author Connor
 * @version 5
 */
public final class Treasures
{
	// Coins
	public static final int COIN_BRONZE = 0;
	public static final int COIN_SILVER = 1;
	public static final int COIN_GOLD = 2;

	// Diamonds
	public static final int DIMOND_RED = 3;
	public static final int DIAMOND_BLUE = 4;

	// Images
	public static BufferedImage[] treasures;

	/**
	 * Load the images
	 * @throws IOException if the images could not be successfully loaded
	 */
	public static void load() throws IOException
	{
		treasures = new BufferedImage[5];

		treasures[0] = ImageIO.read(Treasures.class
				.getResourceAsStream("/Items/coinBronze.png"));
		treasures[1] = ImageIO.read(Treasures.class
				.getResourceAsStream("/Items/coinSilver.png"));
		treasures[2] = ImageIO.read(Treasures.class
				.getResourceAsStream("/Items/coinGold.png"));
		treasures[3] = ImageIO.read(Treasures.class
				.getResourceAsStream("/Items/gemRed.png"));
		treasures[4] = ImageIO.read(Treasures.class
				.getResourceAsStream("/Items/gemBlue.png"));
	}

	/**
	 * Returns the number of points this treasure is worth
	 * @throws IllegalArgumentException if the given type is less than 0 or
	 *             greater than 4
	 * @return the number of points this treasure is worth
	 */
	public static int findPoints(int type)
	{
		int value;
		if (type >= 0 && type <= 4)
		{
			if (type == Treasures.COIN_BRONZE)
			{
				value = 1;
			}
			else if (type == Treasures.COIN_SILVER)
			{
				value = 5;
			}
			else if (type == Treasures.COIN_GOLD)
			{
				value = 10;
			}
			// It is a diamond
			else
			{
				value = 50;
			}
			return value;
		}
		else
			throw new IllegalArgumentException(
					"type must be a defined Treasures (0 - 4)");
	}

	/**
	 * Return a reference to the image desired
	 * @param type the specified image to get
	 * @throws IllegalArgumentException if the given type is less than 0 or
	 *             greater than 4
	 * @return a reference to the image desired
	 */
	public static BufferedImage getImage(int type)
	{
		if (type >= 0 && type <= 4)
			return treasures[type];
		else
			throw new IllegalArgumentException(
					"type must be a defined Treasures (0 - 4)");
	}
}