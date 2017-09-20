package GameState;

import java.awt.Graphics2D;
import java.io.IOException;

/**
 * An abstract class used to define what a game state is and has
 * @author Raya
 * @version 2
 */
public abstract class GameState
{
	protected GameStateManager gsm;

	/**
	 * Initializes the game state
	 * @throws IOException if resources being initialized are not found
	 */
	public abstract void init() throws IOException;

	/**
	 * Updates the game state
	 * @throws IOException if gameStateManager can not change states properly
	 */
	public abstract void update() throws IOException;

	/**
	 * Draws the game state
	 * @param g The graphics to draw with
	 */
	public abstract void draw(Graphics2D g);

	/**
	 * Checks for key pressed events
	 * @param key The keycode of the key being pressed
	 * @throws IOException if gameStateManager can not change states properly
	 */
	public abstract void keyPressed(int key) throws IOException;

	/**
	 * Checks for key release events
	 * @param key The keycode of the key being released
	 */
	public abstract void keyReleased(int key);
}