package TileMap;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class defines a set of obstacles to be used in the game as numbers while
 * maintaining an array of images that those numbers correspond to in the array.
 * A specific reference to an image can be obtained using the getImage method
 * @author Connor
 * @version 8
 */
public final class Obstacles
{
	// Buttons
	public static final int BUTTON_BLUE_ON = 0;
	public static final int BUTTON_BLUE_OFF = 1;
	public static final int BUTTON_GREEN_ON = 2;
	public static final int BUTTON_GREEN_OFF = 3;
	public static final int BUTTON_RED_ON = 4;
	public static final int BUTTON_RED_OFF = 5;

	// Switches
	public static final int SWITCH_BLUE_ON = 6;
	public static final int SWITCH_BLUE_OFF = 7;
	public static final int SWITCH_GREEN_ON = 8;
	public static final int SWITCH_GREEN_OFF = 9;
	public static final int SWITCH_RED_ON = 10;
	public static final int SWITCH_RED_OFF = 11;

	// Lasers
	public static final int LASER_BLUE = 12;
	public static final int LASER_GREEN = 13;
	public static final int LASER_RED = 14;
	public static final int LASER_PURPLE = 15;

	// Images
	private static BufferedImage[] obstacles;

	/**
	 * Load the images
	 * @throws IOException if the images could not be successfully loaded
	 */
	public static void load() throws IOException
	{
		obstacles = new BufferedImage[16];
		// load the obstacles
		// buttons
		obstacles[0] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonBlueOff.png"));
		obstacles[1] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonBlueOn.png"));
		obstacles[2] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonGreenOff.png"));
		obstacles[3] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonGreenOn.png"));
		obstacles[4] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonRedOff.png"));
		obstacles[5] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/buttonRedOn.png"));
		// switches
		obstacles[6] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchBlueOn.png"));
		obstacles[7] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchBlueOff.png"));
		obstacles[8] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchGreenOn.png"));
		obstacles[9] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchGreenOff.png"));
		obstacles[10] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchRedOn.png"));
		obstacles[11] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserSwitchRedOff.png"));
		// lasers
		obstacles[12] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserBlueHorizontal.png"));
		obstacles[13] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserGreenHorizontal.png"));
		obstacles[14] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserRedHorizontal.png"));
		obstacles[15] = ImageIO.read(Obstacles.class
				.getResourceAsStream("/Obstacles/laserPurpleHorizontal.png"));
	}

	/**
	 * Return a reference to the image desired
	 * @param type the specified image to get
	 * @throws IllegalArgumentException if the given type is less than 0 or
	 *             greater than 15
	 * @return a reference to the image desired
	 */
	public static BufferedImage getImage(int type)
	{
		if (type >= 0 && type <= 15)
			return obstacles[type];
		else
			throw new IllegalArgumentException(
					"type must be a defined Obstacles (0 - 15)");
	}
}