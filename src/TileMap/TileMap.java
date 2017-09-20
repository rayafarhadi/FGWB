package TileMap;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.GamePanel;

/**
 * A tile map is an array of tiles used for drawing the level maps, treasures,
 * obstacles and doors and for collisions
 * @author Raya and Connor
 * @version 12
 */
public class TileMap
{
	// Position
	private double x;
	private double y;
	private Point buoyStart;
	private Point grillStart;

	// Map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numColumns;

	// Tile Set
	private BufferedImage tileset;
	private int tilesAcross;
	private Tile[][] tiles;

	// Coins and diamonds
	public static ArrayList<Treasure> treasures;

	// Obstacles
	public static ArrayList<Switch> switches;
	public static ArrayList<Button> buttons;
	private int noObstacles;
	public static Door redDoor, blueDoor;

	/**
	 * Initialize the tileSize and treasures array and load the obstacle,
	 * treasure and door images
	 * @param tileSize
	 * @throws IOException
	 */
	public TileMap(int tileSize) throws IOException
	{
		this.tileSize = tileSize;
		treasures = new ArrayList<Treasure>();
		Obstacles.load();
		Treasures.load();
		Doors.load();
	}

	/**
	 * Loads the tileset and gets the subimages from the tileset for each tile
	 * @param tileSetFile The file name of the tileset being used
	 * @throws IOException if the tileset image can not be found
	 */
	public void loadTiles(String tileSetFile) throws IOException
	{
		tileset = ImageIO.read(getClass().getResourceAsStream(tileSetFile));
		tilesAcross = tileset.getWidth() / tileSize;
		tiles = new Tile[3][tilesAcross];

		// Empty
		tiles[0][0] = new Tile(tileset.getSubimage(0, 0, 32, 32), Tile.NORMAL);

		// Liquid
		tiles[0][1] = new Tile(tileset.getSubimage(32, 0, 32, 32), Tile.LAVA);
		tiles[0][2] = new Tile(tileset.getSubimage(64, 0, 32, 32), Tile.WATER);
		tiles[0][3] = new Tile(tileset.getSubimage(96, 0, 32, 32), Tile.ACID);

		// Blocked
		tiles[1][0] = new Tile(tileset.getSubimage(0, 32, 32, 32), Tile.BLOCKED);
		tiles[1][1] = new Tile(tileset.getSubimage(32, 32, 32, 32), Tile.BLOCKED);
		tiles[1][2] = new Tile(tileset.getSubimage(64, 32, 32, 32), Tile.BLOCKED);
		tiles[1][3] = new Tile(tileset.getSubimage(96, 32, 32, 32), Tile.BLOCKED);
		tiles[1][4] = new Tile(tileset.getSubimage(128, 32, 32, 32), Tile.BLOCKED);
		tiles[1][5] = new Tile(tileset.getSubimage(160, 32, 32, 32), Tile.BLOCKED);
		tiles[1][6] = new Tile(tileset.getSubimage(192, 32, 32, 32), Tile.BLOCKED);
		tiles[1][7] = new Tile(tileset.getSubimage(224, 32, 32, 32), Tile.BLOCKED);
		tiles[1][8] = new Tile(tileset.getSubimage(256, 32, 32, 32), Tile.BLOCKED);
		tiles[1][9] = new Tile(tileset.getSubimage(288, 32, 32, 32), Tile.BLOCKED);

		// Treasures
		tiles[2][0] = new Tile(tileset.getSubimage(0, 64, 32, 32), Tile.GEM);
		tiles[2][1] = new Tile(tileset.getSubimage(32, 64, 32, 32), Tile.GEM);
		tiles[2][3] = new Tile(tileset.getSubimage(64, 64, 32, 32), Tile.COIN);
		tiles[2][4] = new Tile(tileset.getSubimage(96, 64, 32, 32), Tile.COIN);
		tiles[2][5] = new Tile(tileset.getSubimage(128, 64, 32, 32), Tile.COIN);
	}

	/**
	 * Reads the level file, gets the number of rows and columns of the level
	 * and fills in the map with the tile images that correspond to the numbers
	 * in the level file
	 * @param levelFileName The name of the level file
	 * @throws NumberFormatException if parseInt() doesn't find an integer
	 * @throws IOException if the level file can not be found
	 */
	public void loadMap(String levelFileName) throws NumberFormatException,
			IOException
	{
		// Load level file
		BufferedReader read = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(levelFileName)));

		// Get number of rows and columns
		numColumns = Integer.parseInt(read.readLine());
		numRows = Integer.parseInt(read.readLine());

		// load the map
		map = new int[numRows][numColumns];

		String delims = "\\s+";
		for (int row = 0; row < numRows; row++)
		{
			String line = read.readLine();
			String[] tokens = line.split(delims);
			for (int column = 0; column < numColumns; column++)
			{
				int tileType = Integer.parseInt(tokens[column]);

				// Tile types under 100 are regular tiles like blocked tiles,
				// liquids, etc.
				if (tileType < 100)
				{
					map[row][column] = tileType;
				}

				// Tile types between 100 and 200 are buoy and grill start
				// points
				else if (tileType < 200)
				{
					if (tileType == 100)
					{
						buoyStart = new Point(column * tileSize, row * tileSize);
						map[row][column] = 0;
					}
					else
					{
						grillStart = new Point(column * tileSize, row
								* tileSize);
						map[row][column] = 0;
					}
				}

				// Tile types between 200 and 300 are treasures
				else if (tileType < 300)
				{
					map[row][column] = 0;
					treasures.add(new Treasure(tileType - 200, column
							* tileSize, row * tileSize));
				}

				// Tile types between 300 and 400 are doors
				else if (tileType < 400)
				{
					if (tileType == 300)
					{
						map[row][column] = 0;
						redDoor = new Door(column * tileSize, row * tileSize,
								Doors.RED);
					}
					else
					{
						map[row][column] = 0;
						blueDoor = new Door(column * tileSize, row * tileSize,
								Doors.BLUE);
					}
				}
			}
		}
		// Create the obstacles where they are needed
		noObstacles = Integer.parseInt(read.readLine());
		switches = new ArrayList<Switch>();
		buttons = new ArrayList<Button>();
		for (int obstacle = 0; obstacle < noObstacles; obstacle++)
		{
			String line = read.readLine();
			String[] paramaters = line.split(delims);
			if (paramaters[0].equalsIgnoreCase("Switch"))
			{
				// Parameters in comments in all .lvl files
				switches.add(new Switch(
						Integer.parseInt(paramaters[1]),
						Integer.parseInt(paramaters[2]),
						Integer.parseInt(paramaters[3]),
						Integer.parseInt(paramaters[4]) * tileSize,
						Integer.parseInt(paramaters[5]) * tileSize,
						Integer.parseInt(paramaters[6]) * tileSize,
						Integer.parseInt(paramaters[7]) * tileSize));
			}
			else if (paramaters[0].equalsIgnoreCase("Button"))
			{
				// Parameters in comments in all .lvl files
				buttons.add(new Button(
						Integer.parseInt(paramaters[1]),
						Integer.parseInt(paramaters[2]),
						Integer.parseInt(paramaters[3]),
						Integer.parseInt(paramaters[4]),
						Integer.parseInt(paramaters[5]) * tileSize,
						Integer.parseInt(paramaters[6]) * tileSize,
						Integer.parseInt(paramaters[7]) * tileSize,
						Integer.parseInt(paramaters[8]) * tileSize,
						Integer.parseInt(paramaters[9]) * tileSize,
						Integer.parseInt(paramaters[10]) * tileSize));
			}
		}
	}

	/**
	 * Returns the tile size
	 * @return the tile size
	 */
	public int getTileSize()
	{
		return tileSize;
	}

	/**
	 * Returns the x position of the tile
	 * @return the x position of the tile
	 */
	public double getx()
	{
		return x;
	}

	/**
	 * Returns the y position of the tile
	 * @return the y position of the tile
	 */
	public double gety()
	{
		return y;
	}

	/**
	 * Returns the type of tile
	 * @param row The row of the tile
	 * @param column The column of the tile
	 * @return the type of tile
	 */
	public int getType(int row, int column)
	{
		try
		{
			int tileLocation = map[row][column];
			int tileRow = tileLocation / tilesAcross;
			int tileColumn = tileLocation % tilesAcross;
			return tiles[tileRow][tileColumn].getType();
		}
		// Makes all tiles outside of the map blocked
		catch (IndexOutOfBoundsException e)
		{
			return Tile.BLOCKED;
		}
	}

	/**
	 * Sets the position of the tile (mostly used for the buoy and grill
	 * starting positions)
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;

		fixBounds();
	}

	public void fixBounds()
	{
		double xMin = GamePanel.WIDTH;
		double xMax = 0;
		double yMin = GamePanel.HEIGHT;
		double yMax = 0;

		if (x < xMin)
		{
			x = xMin;
		}

		if (y < yMin)
		{
			y = yMin;
		}

		if (x > xMax)
		{
			x = xMax;
		}

		if (y > yMax)
		{
			y = yMax;
		}
	}

	/**
	 * Returns the buoys starting location
	 * @return the buoys starting location
	 */
	public Point getBuoyStart()
	{
		return buoyStart;
	}

	/**
	 * Returns the grills starting location
	 * @return the grills starting location
	 */
	public Point getGrillStart()
	{
		return grillStart;
	}

	/**
	 * Draws all of the tiles in the correct locations on the map
	 * @param g The graphics used for drawing
	 */
	public void draw(Graphics2D g)
	{
		for (int row = 0; row < numRows; row++)
		{
			for (int column = 0; column < numColumns; column++)
			{
				int tileLocation = map[row][column];
				int tileRow = tileLocation / tilesAcross;
				int tileColumn = tileLocation % tilesAcross;

				// Draw regular tiles
				g.drawImage(tiles[tileRow][tileColumn].getImage(),
						(int) x + column * tileSize, (int) y + row * tileSize,
						null);
			}
		}

		// Draw the treasures
		for (int treasure = 0; treasure < treasures.size(); treasure++)
		{
			treasures.get(treasure).draw(g);
		}

		// Draw the obstacles
		for (int theSwitch = 0; theSwitch < switches.size(); theSwitch++)
		{
			switches.get(theSwitch).draw(g);
		}
		for (int theButton = 0; theButton < buttons.size(); theButton++)
		{
			buttons.get(theButton).draw(g);
		}

		// Draw the doors
		redDoor.draw(g);
		blueDoor.draw(g);
	}
}