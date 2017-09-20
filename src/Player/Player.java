package Player;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import GameState.LevelState;
import Main.GamePanel;
import TileMap.Button;
import TileMap.Doors;
import TileMap.Switch;
import TileMap.Tile;
import TileMap.TileMap;
import TileMap.Treasures;

/**
 * The Player object
 * @author Raya and Connor
 * @version 11
 */
public class Player
{
	// Tile Map
	private TileMap tileMap;
	private int tileSize;

	// Position and movement
	// Doubles so the player can have more precise movements but cast to ints
	// for collisions
	private double x;
	private double y;
	private double xDirection;
	private double yDirection;

	// Dimensions
	private int tileWidth;
	private int tileHeight;

	// Collision Box
	private int width;
	private int height;

	// Collisions
	private int currentRow;
	private int currentColumn;
	private double xDest;
	private double yDest;

	// Tile Checks
	private boolean topLeftBlocked;
	private boolean bottomLeftBlocked;
	private boolean topRightBlocked;
	private boolean bottomRightBlocked;
	private boolean lowerLeftEmpty;
	private boolean lowerRightEmpty;

	// Tile locations relative to player
	private int leftTile;
	private int rightTile;
	private int topTile;
	private int bottomTile;
	private int lowerTile;

	// Tile types
	private int topLeftType;
	private int topRightType;
	private int bottomLeftType;
	private int bottomRightType;
	private int lowerLeftType;
	private int lowerRightType;
	private int currentTileType;

	// Movement
	private boolean left;
	private boolean right;
	private boolean jumping;
	private boolean falling;

	// Physics
	private double moveSpeed;
	private double maxSpeed;
	private double stopSpeed;
	private double fallSpeed;
	private double maxFallSpeed;
	private double jumpStartingSpeed;
	private double stopJumpSpeed;

	// Player
	public boolean dead;
	private int player;
	public static final int GRILL = 1;
	public static final int BUOY = -1;

	// Image
	BufferedImage image;

	/**
	 * Initializes the dimensions of the player and the movement speeds Sets the
	 * player to either a buoy or a grill Loads the player image
	 * @param tileMap The tilemap being used
	 * @param image The file location of the image
	 * @param width The width of the player
	 * @param height The height of the player
	 * @param player The type of player
	 * @throws IOException if the image file can not be found
	 */
	public Player(TileMap tileMap, String image, int width, int height,
			int player)
					throws IOException
	{
		this.tileMap = tileMap;
		this.tileSize = tileMap.getTileSize();

		this.width = width;
		this.height = height;

		this.player = player;

		moveSpeed = 1;
		maxSpeed = 3;
		stopSpeed = 1.1;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStartingSpeed = -5.5;
		stopJumpSpeed = 0.3;

		dead = false;

		// Load sprites
		this.image = ImageIO.read(getClass()
				.getResourceAsStream(image));
	}

	/**
	 * Moves the player
	 */
	private void move()
	{
		// If the key to move the player is pressed move and accelerate the
		// player until it reaches its max speed

		// Move left
		if (left)
		{
			xDirection -= moveSpeed;
			if (xDirection < -maxSpeed)
			{
				xDirection = -maxSpeed;
			}
		}
		// Move right
		else if (right)
		{
			xDirection += moveSpeed;
			if (xDirection > maxSpeed)
			{
				xDirection = maxSpeed;
			}
		}
		// If the key to move the player is released decelerate the player until
		// it stops completely
		else
		{
			// Facing right
			if (xDirection > 0)
			{
				xDirection -= stopSpeed;
				if (xDirection < 0)
				{
					xDirection = 0;
				}
			}
			// Facing left
			else if (xDirection < 0)
			{
				xDirection += stopSpeed;
				if (xDirection > 0)
				{
					xDirection = 0;
				}
			}
		}

		// Jump until the player needs to start falling
		if (jumping && !falling)
		{
			yDirection = jumpStartingSpeed;
			falling = true;
		}

		if (falling)
		{
			yDirection += fallSpeed;

			if (yDirection > 0)
				jumping = false;
			// Decelerate the players ascent until it stops
			if (yDirection < 0 && !jumping)
				yDirection += stopJumpSpeed;
			// then accelerate the players descent until it reaches the max
			// falling speed.
			if (yDirection > maxFallSpeed)
				yDirection = maxFallSpeed;
			// This creates a more realistic jumping animation
		}
	}

	/**
	 * Checks the types of the tiles around the player
	 * @param x The players x position
	 * @param y The players y position
	 * @return if the player is dead
	 */
	public boolean calculateCorners(double x, double y)
	{
		leftTile = (int) (x) / tileSize;
		rightTile = ((int) (x) + width - 1) / tileSize;
		topTile = (int) (y) / tileSize;
		bottomTile = ((int) (y) + height - 1) / tileSize;
		lowerTile = ((int) (y) + height) / tileSize;

		// Stop the player from going off the sides of the screen
		if (x < 0 || x >= GamePanel.WIDTH)
		{
			topLeftType = topRightType = bottomLeftType = bottomRightType = Tile.BLOCKED;
			return false;
		}

		// Stop the player from going off the top and bottom of the screen
		if (y >= GamePanel.HEIGHT)
		{
			topLeftType = topRightType = bottomLeftType = bottomRightType = Tile.BLOCKED;
			return false;
		}

		// Get the type of tile touching the corners of the player
		topLeftType = tileMap.getType(topTile, leftTile);
		topRightType = tileMap.getType(topTile, rightTile);
		bottomLeftType = tileMap.getType(bottomTile, leftTile);
		bottomRightType = tileMap.getType(bottomTile, rightTile);

		// Get the type of tiles under the player
		lowerLeftType = tileMap.getType(lowerTile, leftTile);
		lowerRightType = tileMap.getType(lowerTile, rightTile);

		// Get the type of tile the player is currently on
		currentTileType = tileMap.getType(currentRow, currentColumn);

		// If the corners are blocked or touching a liquid
		topLeftBlocked = (topLeftType == Tile.BLOCKED || topLeftType >= Tile.LAVA);
		topRightBlocked = (topRightType == Tile.BLOCKED || topRightType >= Tile.LAVA);
		bottomLeftBlocked = (bottomLeftType == Tile.BLOCKED || bottomLeftType >= Tile.LAVA);
		bottomRightBlocked = (bottomRightType == Tile.BLOCKED || bottomRightType >= Tile.LAVA);

		// If the tile under the player is empty
		lowerLeftEmpty = lowerLeftType == Tile.NORMAL;
		lowerRightEmpty = lowerRightType == Tile.NORMAL;

		// Return if the player is touching a deadly liquid
		if (bottomLeftBlocked || bottomRightBlocked)
		{
			if (player == BUOY && (topLeftType == Tile.LAVA || topLeftType == Tile.ACID
					|| currentTileType == Tile.LAVA || currentTileType == Tile.ACID))
				return true;
			else if (player == GRILL && (topLeftType == Tile.WATER || topLeftType == Tile.ACID
					|| currentTileType == Tile.WATER || currentTileType == Tile.ACID))
				return true;
			if (player == BUOY && (topRightType == Tile.LAVA || topRightType == Tile.ACID
					|| currentTileType == Tile.LAVA || currentTileType == Tile.ACID))
				return true;
			else if (player == GRILL && (topRightType == Tile.WATER || topRightType == Tile.ACID
					|| currentTileType == Tile.WATER || currentTileType == Tile.ACID))
				return true;
		}

		return false;
	}

	/**
	 * Check the tile and laser collisions
	 */
	public void checkCollision()
	{
		currentColumn = (int) x / tileSize;
		currentRow = (int) y / tileSize;

		xDest = x + xDirection;
		yDest = y + yDirection;

		// Check top collision
		dead = calculateCorners(x, yDest);
		if (yDirection < 0)
		{
			if (topLeftBlocked || topRightBlocked)
			{
				// If the top of the player hits a blocked tile stop
				if (topLeftType == Tile.BLOCKED || topRightType == Tile.BLOCKED)
				{
					yDirection = 0;
				}
				else if (topLeftType >= Tile.LAVA || topRightType >= Tile.LAVA)
				{
					// If the player is already in a liquid allow the top of the
					// player to go through liquid
					if (bottomLeftType >= Tile.LAVA
							|| bottomLeftType >= Tile.LAVA
							|| currentTileType >= Tile.LAVA)
					{
						y += yDirection;
					}
					// If the player isnt in liquid and hits liquid stop
					else
					{
						yDirection = 0;
					}
				}
			}
			else
			{
				y += yDirection;
			}
		}

		// Check bottom collision
		if (yDirection > 0)
		{
			if (bottomLeftBlocked || bottomRightBlocked)
			{
				// Stop falling if the player hits ground
				if (bottomLeftType == Tile.BLOCKED
						|| bottomRightType == Tile.BLOCKED)
				{
					yDirection = 0;
					falling = false;
				}
				else if (bottomLeftType >= Tile.LAVA
						|| bottomRightType >= Tile.LAVA)
				{
					// Stop falling if the player is at the bottom of the liquid
					if ((lowerLeftEmpty || lowerRightEmpty
							|| lowerRightType >= Tile.LAVA || lowerLeftType >= Tile.LAVA)
							&& currentTileType >= Tile.LAVA)
					{
						yDirection = 0;
						falling = false;
					}
					else
					{
						y += yDirection;
						falling = true;
					}
				}
			}
			else
			{
				// Dont let the player fall through liquid
				if (currentTileType >= Tile.LAVA)
				{
					yDirection = 0;
					falling = false;
				}
				else
				{
					y += yDirection;
					falling = true;
				}
			}
		}

		// Check left collision
		dead = calculateCorners(xDest, y);
		if (xDirection < 0)
		{
			// Stop if the player hits a wall
			if (topLeftType == Tile.BLOCKED || bottomLeftType == Tile.BLOCKED)
			{
				xDirection = 0;
			}
			else if (topLeftType >= Tile.LAVA && bottomLeftType < Tile.LAVA)
			{
				xDirection = 0;
			}
			else
			{
				x += xDirection;
			}
		}

		// Check right collision
		if (xDirection > 0)
		{
			// Stop if the player hits a wall
			if (topRightType == Tile.BLOCKED || bottomRightType == Tile.BLOCKED)
			{
				xDirection = 0;
			}
			else if (topRightType >= Tile.LAVA && bottomRightType < Tile.LAVA)
			{
				xDirection = 0;
			}
			else
			{
				x += xDirection;
			}
		}

		// Check if player should falls
		if (!falling)
		{
			dead = calculateCorners(x, yDest + 1);
			if (!bottomLeftBlocked && !bottomRightBlocked)
			{
				// Stop the player from falling if it is in liquid
				if (currentTileType >= Tile.LAVA)
				{
					falling = false;
				}
				else
				{
					falling = true;
				}
			}
			else if (bottomLeftBlocked && bottomRightBlocked)
			{
				if (bottomLeftType >= Tile.LAVA && bottomRightType >= Tile.LAVA)
				{
					if (lowerRightType >= Tile.LAVA
							|| lowerLeftType >= Tile.LAVA)
					{
						falling = true;

					}
					// Stop falling when the player reaches the bottom of the
					// liquid
					else if (lowerLeftEmpty || lowerRightEmpty)
					{
						falling = false;
					}
				}
			}
		}
		else if (falling)
		{
			dead = calculateCorners(x, yDest + 1);
			if (!bottomLeftBlocked && !bottomRightBlocked)
			{
				// Stop the player from falling if it is in liquid
				if (currentTileType >= Tile.LAVA)
				{
					falling = false;
				}
				else
				{
					falling = true;
				}
			}
			else if (bottomLeftBlocked && bottomRightBlocked)
			{
				if (bottomLeftType >= Tile.LAVA && bottomRightType >= Tile.LAVA)
				{
					if (!lowerLeftEmpty || !lowerRightEmpty)
					{
						falling = true;
					}
					// Stop falling when the player reaches the bottom of the
					// liquid
					else if (lowerLeftEmpty || lowerRightEmpty)
					{
						falling = false;
					}
				}
			}
		}

		// Check if the player hits a laser
		for (int buttonLaser = 0; buttonLaser < TileMap.buttons.size(); buttonLaser++)
		{
			Button button = TileMap.buttons.get(buttonLaser);
			if (button.hasPlayerInLaser((int) x, (int) y))
			{
				dead = true;
			}
		}
		for (int switchLaser = 0; switchLaser < TileMap.switches.size(); switchLaser++)
		{
			Switch aSwitch = TileMap.switches.get(switchLaser);
			if (aSwitch.hasPlayerInLaser((int) x, (int) y))
			{
				dead = true;
			}
		}
	}

	/**
	 * Get the players x coordinate
	 * @return x coordinate
	 */
	public int getX()
	{
		return (int) x + 16;
	}

	/**
	 * Get the players y coordinate
	 * @return y coordinate
	 */
	public int getY()
	{
		return (int) y + 16;
	}

	/**
	 * Set the players position
	 * @param x Players x coordinate
	 * @param y Players y coordinate
	 */
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Makes the player start or stop going left
	 * @param b True if the player should go left, false if the player should
	 *            not
	 */
	public void setLeft(boolean b)
	{
		left = b;
	}

	/**
	 * Makes the player start or stop going right
	 * @param b True if the player should go right, false if the player should
	 *            not
	 */
	public void setRight(boolean b)
	{
		right = b;
	}

	/**
	 * Makes the player start or stop jumping
	 * @param b True if the player should jump, false if the player should not
	 */
	public void setJumping(boolean b)
	{
		jumping = b;
		if (currentTileType >= Tile.LAVA)
		{
			falling = false;
		}
	}

	/**
	 * Returns if the player can take the treasure
	 * @param treasure the treasure in question
	 * @return if the player can take the treasure
	 */
	public boolean canTake(int treasure)
	{
		boolean take = false;

		// Any player can take a coin
		if (treasure == Treasures.COIN_BRONZE
				|| treasure == Treasures.COIN_SILVER
				|| treasure == Treasures.COIN_GOLD)
		{
			take = true;
		}

		// The buoy can take the blue diamond
		if (player == Player.BUOY)
		{
			if (treasure == Treasures.DIAMOND_BLUE)
			{
				take = true;
			}
		}
		// The grill can take the red diamond
		else
		{
			if (treasure == Treasures.DIMOND_RED)
			{
				take = true;
			}
		}

		return take;
	}

	/**
	 * Returns if the player can open the door
	 * @param door the door to be opened
	 * @return if the player can open the door
	 */
	public boolean canOpen(int door)
	{
		boolean open = false;

		// Buoy can open blue doors
		if (player == Player.BUOY)
		{
			if (door == Doors.BLUE)
			{
				open = true;
			}
		}
		// Grill can open red doors
		else
		{
			if (door == Doors.RED)
			{
				open = true;
			}
		}

		return open;
	}

	/**
	 * Update the players movements and positions and check for collisions
	 * @throws IOException if reset() can not find the levelState
	 */
	public void update() throws IOException
	{
		// If the player dies reset the level
		if (dead)
		{
			LevelState.reset();
			dead = false;
		}

		// update position
		move();
		checkCollision();
		setPosition(x, y);
	}

	/**
	 * Draw the player
	 * @param g The graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(image, (int) (x - tileWidth / 2),
				(int) (y - tileHeight / 2), null);
	}

}