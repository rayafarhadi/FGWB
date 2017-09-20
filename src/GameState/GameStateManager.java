package GameState;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The GameStateManager manages the various states of the game. Each state is
 * kept in an array and is used when various sub classes call this class'
 * setState method. The GameStateManager only updates and draws the current
 * state
 * @author Raya and Connor
 * @version 9
 */
public class GameStateManager
{
	private ArrayList<GameState> gameStates;
	private static int currentState;
	public static final int MENU_STATE = 0;
	public static final int LEVEL_STATE = 1;
	public static final int INSTRUCTION_STATE = 2;
	public static final int CREDITS_STATE = 3;
	public static final int LEVEL_SELECT_STATE = 4;
	public static final int CONGRATS_STATE = 5;

	/**
	 * Loads all the game states and passes them a copy of this GameStateManager
	 * @param level the level file used for the LevelState
	 * @throws IOException if a state fails to be added to the array of game
	 *             states
	 */
	public GameStateManager(int level) throws IOException
	{
		gameStates = new ArrayList<GameState>();
		currentState = MENU_STATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LevelState(this, level));
		gameStates.add(new InstructionState(this));
		gameStates.add(new CreditsState(this));
		gameStates.add(new LevelSelectState(this));
		gameStates.add(new CongratsState(this));

	}

	/**
	 * Changes the current game state to the given one
	 * @param state the given state
	 * @throws IOException if the GameStateManager could not change the state
	 *             properly
	 * @throws IllegalArgumentException if the given type is less than 0 or
	 *             greater than 5
	 */
	public void setState(int state) throws IOException
	{
		if (state >= 0 && state <= 5)
		{
			currentState = state;
			gameStates.get(currentState).init();
		}
		else
			throw new IllegalArgumentException(
					"type must be a defined GameState (0 - 5)");
	}

	/**
	 * Returns the numerical value of the current state which is also that
	 * state's index in the array of game states
	 * @return the numerical value of the current state
	 */
	public static int getState()
	{
		return currentState;
	}

	/**
	 * Removes the old level state to be replaced by the new level state using
	 * the specified level
	 * @param level the level to change to
	 * @throws IOException if the GameState could not change or load the state
	 *             properly
	 */
	public void setLevel(int level) throws IOException
	{
		gameStates.add(LEVEL_STATE, new LevelState(this, level));
		gameStates.remove(LEVEL_STATE + 1);
	}

	/**
	 * Updates the current state
	 * @throws IOException if the the current state could not be properly
	 *             updated
	 */
	public void update() throws IOException
	{
		gameStates.get(currentState).update();
	}

	/**
	 * Draws the current state
	 * @param g the graphics to draw with
	 */
	public void draw(Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}

	/**
	 * Passes the key pressed to the current state
	 * @param key the key pressed
	 * @throws IOException if currentState can not be found
	 */
	public void keyPressed(int key) throws IOException
	{
		gameStates.get(currentState).keyPressed(key);
	}

	/**
	 * Passes the key released to the current state
	 * @param key the key released
	 * @throws IOException if currentState can not be found
	 */
	public void keyReleased(int key)
	{
		gameStates.get(currentState).keyReleased(key);
	}
}