package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import GameState.GameStateManager;
import GameState.InstructionState;

/**
 * This class is used for the purpose of interpreting mouse movement
 * @author Connor
 * @version 10
 */
public class MouseInput implements MouseListener, MouseMotionListener
{

	private boolean next;
	private boolean previous;
	private boolean done;
	private boolean pressed;
	private boolean inArea;
	private boolean toMenu;
	private int choice;
	private int selectedLevel;
	private int pressedX, pressedY;
	private int movedX, movedY;

	/**
	 * Sets the value of the variables
	 */
	public MouseInput()
	{
		next = false;
		previous = false;
		done = false;
		pressed = false;
		inArea = false;
		toMenu = false;
		choice = 0;

		pressedX = pressedY = pressedX = pressedY = 0;

		selectedLevel = 0;
	}

	/**
	 * Processes events when the mouse is pressed differently depending on the
	 * state the game is in
	 */
	public void mousePressed(MouseEvent e)
	{
		pressedX = e.getX();
		pressedY = e.getY();
		pressed = true;

		// Process mouse presses in the instructions state
		if (GameStateManager.getState() == GameStateManager.INSTRUCTION_STATE)
		{
			if ((pressedX > 666 && pressedX < 864)
					&& (pressedY > 589 && pressedY < 650))
			{
				// The user clicked the next button
				if (InstructionState.page != 6)
					next = true;
				// The user clicked the finished button
				else
					done = true;
			}
			else if ((pressedX > 114 && pressedX < 340)
					&& (pressedY > 590 && pressedY < 649))
			{
				// The user clicked the previous button
				if (InstructionState.page > 0)
					previous = true;
			}
		}
		else if (GameStateManager.getState() == GameStateManager.LEVEL_SELECT_STATE)
		{
			int level = 0;
			for (int row = 0; row < 4; row++)
			{
				for (int col = 0; col < 5; col++)
				{
					level++;
					int x = col * 154 + 135;
					int y = row * 96 + 130;

					if (pressed)
					{
						// The user clicked a level
						if (pressedX > x && pressedX < x + 128 && pressedY > y
								&& pressedY < y + 64)
							selectedLevel = level;
					}
				}
			}

			if (pressed)
			{
				// The user clicked the back button
				if (pressedX > 430 && pressedX < 580 && pressedY > 685
						&& pressedY < 735)
				{
					toMenu = true;
				}
			}
		}
		else if (GameStateManager.getState() == GameStateManager.CONGRATS_STATE)
		{
			// The user clicked the back button
			if (pressedX > 0 && pressedY > 0)
			{
				toMenu = true;
			}
		}
	}

	/**
	 * Changes the value of pressed to false when the mouse is released
	 */
	public void mouseReleased(MouseEvent arg0)
	{
		pressed = false;
	}

	/**
	 * Processes events when the mouse is moved differently depending on the
	 * state the game is in
	 */
	public void mouseMoved(MouseEvent e)
	{
		movedX = e.getX();
		movedY = e.getY();

		// // Process mouse presses in the instructions state
		if (GameStateManager.getState() == GameStateManager.MENU_STATE)
		{
			if (movedX >= 384 && movedX <= 768 && movedY >= 160 && movedY < 688)
			{
				inArea = true;
				// Mouse hovered over play
				if (movedY >= 160 && movedY < 304)
				{
					choice = 0;
				}
				// Mouse hovered over instructions
				else if (movedY >= 304 && movedY < 400)
				{
					choice = 1;
				}
				// Mouse hovered over credits
				else if (movedY >= 400 && movedY < 496)
				{
					choice = 2;
				}
				// Mouse hovered over quit
				else if (movedY >= 496 && movedY < 592)
				{
					choice = 3;
				}
				else if (movedY >= 592 && movedY < 688)
				{
					choice = 4;
				}
			}
			else
			{
				inArea = false;
			}
		}
		else if (GameStateManager.getState() == GameStateManager.LEVEL_STATE)
		{
			if (movedX > 394 && movedX < 698 && movedY > 371 && movedY < 659)
			{
				inArea = true;
				// Mouse hovered over next level
				if (movedY >= 371 && movedY < 467)
				{
					choice = 0;
				}
				// Mouse hovered over main menu
				else if (movedY >= 467 && movedY < 563)
				{
					choice = 1;
				}
				// Mouse hovered over quit
				else if (movedY >= 563 && movedY < 659)
				{
					choice = 2;
				}
			}
			else
			{
				inArea = false;
			}
		}

	}

	/**
	 * Returns if the next button was pressed in the instructions screen
	 * @return if the next button was pressed in the instructions screen
	 */
	public boolean next()
	{
		return next;
	}

	/**
	 * Sets next to false :o
	 */
	public void setNextFalse()
	{
		next = false;
	}

	/**
	 * Returns if the previous button was pressed in the instructions screen
	 * @return if the previous button was pressed in the instructions screen
	 */
	public boolean previous()
	{
		return previous;
	}

	/**
	 * Sets previous to false :o
	 */
	public void setPreviousFalse()
	{
		previous = false;
	}

	/**
	 * Returns if the user clicked the finish button in the instructions state
	 * @return if the user clicked the finish button in the instructions state
	 */
	public boolean done()
	{
		return done;
	}

	/**
	 * Sets done to false
	 */
	public void resetDone()
	{
		done = false;
	}

	/**
	 * Returns if the mouse has been pressed
	 * @return if the mouse has been pressed
	 */
	public boolean pressed()
	{
		return pressed;
	}

	/**
	 * Sets pressed to false. It is used so that a single mouse press does not
	 * activate something in another state of the game. For example, when
	 * exiting the instructions screen, it dosen't click instructions again
	 */
	public void unPress()
	{
		pressed = false;
	}

	/**
	 * Returns if the mouse is in the selecting area in the main menu
	 * @return if the mouse is in the selecting area in the main menu
	 */
	public boolean isInArea()
	{
		return inArea;
	}

	/**
	 * Returns the numerical value of the menu selection chosen by the user
	 * @return the numerical value of the menu selection chosen by the user
	 */
	public int choice()
	{
		return choice;
	}

	/**
	 * Returns the x coordinate of the mouse press
	 * @return the x coordinate of the mouse press
	 */
	public int getPressedX()
	{
		return pressedX;
	}

	/**
	 * Returns the y coordinate of the mouse press
	 * @return the y coordinate of the mouse press
	 */
	public int getPressedY()
	{
		return pressedY;
	}

	/**
	 * Returns the x coordinate of the mouse move
	 * @return the x coordinate of the mouse move
	 */
	public int getMovedX()
	{
		return pressedX;
	}

	/**
	 * Returns the y coordinate of the mouse move
	 * @return the y coordinate of the mouse move
	 */
	public int getMovedY()
	{
		return pressedY;
	}

	/**
	 * Returns the selected level
	 * @return the selected level
	 */
	public int getSelectedLevel()
	{
		int level = selectedLevel;
		selectedLevel = 0;
		return level;
	}

	/**
	 * Returns backToMenu
	 * @return backToMenu
	 */
	public boolean goToMenu()
	{
		return toMenu;
	}

	public void resetGoToMenu()
	{
		toMenu = false;
		unPress();
	}

	// Below are unused mouseListener methods
	public void mouseClicked(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mouseDragged(MouseEvent e)
	{

	}

}