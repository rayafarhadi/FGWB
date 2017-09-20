package TileMap;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Loads the door images from the resource folder
 * @author Connor
 * @version 2
 */
public final class Doors
{
	public static final int RED = 0;
	public static final int BLUE = 1;
	public static final int CLOSED = 2;

	// Images
	private static BufferedImage[] doors;

	/**
	 * Load the images
	 * @throws IOException if the images could not be successfully loaded
	 */
	public static void load() throws IOException
	{
		doors = new BufferedImage[3];

		doors[0] = ImageIO.read(Doors.class
				.getResourceAsStream("/Obstacles/doorClosedRed.png"));
		doors[1] = ImageIO.read(Doors.class
				.getResourceAsStream("/Obstacles/doorClosedBlue.png"));
		doors[2] = ImageIO.read(Doors.class
				.getResourceAsStream("/Obstacles/doorOpen.png"));
	}

	/**
	 * Return a reference to the image desired
	 * @param type the specified image to get
	 * @throws IllegalArgumentException if the given type is less than 0 or
	 *             greater than 2
	 * @return a reference to the image desired
	 */
	public static BufferedImage getImage(int type)
	{
		if (type >= 0 && type <= 2)
			return doors[type];
		else
			throw new IllegalArgumentException(
					"type must be a defined Obstacles (0 - 2)");
	}

}