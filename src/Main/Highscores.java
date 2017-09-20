package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The highscores class is used to update scores in the respective .hs file of
 * each level. At the end of the game, this class can update the master .hs
 * file.
 * @author Connor
 * @version 5
 */
public class Highscores
{
	// Names
	private String[] names;
	private String[] levelNames;

	// Scores
	private int[] scores;
	private int[] levelScores;

	// Times
	private int[] times;
	private int[] levelTimes;

	/**
	 * Sets the size of all of the arrays
	 */
	public Highscores()
	{
		names = new String[10];
		scores = new int[10];
		times = new int[10];
		levelNames = new String[3];
		levelScores = new int[3];
		levelTimes = new int[3];
	}

	/**
	 * Updates the high score file for the game based on scores and total time.
	 * Those at the top are the players with the higher score. When two scores
	 * are the same, whoever has the lower time is closer to the top.
	 * @param name The name of the player who got the score
	 * @param score The score the player has
	 * @param time The time the player took
	 * @throws IOException if the files could not be read from or written to
	 */
	public void updateTotalHighScore(String name, int score, int time)
			throws IOException
	{
		// Replace spaces in the name with underscores
		name = name.replace(' ', '_');

		BufferedReader reader = new BufferedReader(new FileReader(
				"HighScores/Highscores.hs"));
		// read in the player names and scores
		for (int player = 0; player < names.length; player++)
		{
			String line = reader.readLine();
			names[player] = line.split(" ")[0];
			scores[player] = Integer.valueOf(line.split(" ")[1]);
			times[player] = Integer.valueOf(line.split(" ")[2]);
		}
		reader.close();

		// check to see if the current score is higher than one of the other
		// scores (indexes toward 0 are the highest and indexes toward 9 are
		// lowest)
		int indexWhereScoreIsHigher = -1;
		for (int player = 0; player < names.length; player++)
		{
			if (score > scores[player])
			{
				indexWhereScoreIsHigher = player;
				// Exit the loop
				player = names.length;
			}
			else if (score == scores[player])
			{
				// When there is an equal score, compare the times to see who
				// has the higher score
				if (time < times[player])
				{
					indexWhereScoreIsHigher = player;
					// Exit the loop
					player = names.length;
				}
			}
		}

		// Print the scores to the highscore file in the new order only if there
		// is a new order
		if (indexWhereScoreIsHigher > -1)
		{
			PrintWriter writer = new PrintWriter("Highscores/Highscores.hs");
			for (int player = 0; player < indexWhereScoreIsHigher; player++)
			{
				writer.print(names[player] + " " + scores[player] + " "
						+ times[player] + "\n");
				writer.flush();
			}
			// print the new score
			writer.print(name + " " + score + " " + time + "\n");

			// Print the rest of the scores execpt the last 2(not last one bc it
			// is no longer top scores and not 2nd last bc that needs to be
			// printed with out a println so that there is not a new line added
			// to the level after every game played
			for (int player = indexWhereScoreIsHigher; player < names.length - 1; player++)
			{
				writer.println(names[player] + " " + scores[player] + " "
						+ times[player]);
				writer.flush();
			}
			writer.close();
		}

		// Load the new players and scores
		BufferedReader reReader = new BufferedReader(new FileReader(
				"HighScores/Highscores.hs"));
		for (int player = 0; player < names.length; player++)
		{
			String line = reReader.readLine();
			names[player] = line.split(" ")[0];
			scores[player] = Integer.valueOf(line.split(" ")[1]);
			times[player] = Integer.valueOf(line.split(" ")[2]);
		}
		reReader.close();

	}

	/**
	 * Updates the high score file for the level based on scores and total time.
	 * Those at the top are the players with the higher score. When two scores
	 * are the same, whoever has the lower time is closer to the top.
	 * @param name The name of the player who got the score
	 * @param score The score the player has
	 * @param time The time the player took
	 * @param level The level to update the highscores for
	 * @throws IOException if the files could not be read from or written to
	 */
	public void updateLevelHighScore(String name, int score, int time, int level)
			throws IOException
	{
		// Replace spaces in the name with underscores
		name = name.replace(' ', '_');

		BufferedReader reader = new BufferedReader(new FileReader(
				"Highscores/Level" + level
						+ ".hs"));
		// read in the player names and scores
		for (int player = 0; player < levelNames.length; player++)
		{
			String line = reader.readLine();
			levelNames[player] = line.split(" ")[0];
			levelScores[player] = Integer.valueOf(line.split(" ")[1]);
			levelTimes[player] = Integer.valueOf(line.split(" ")[2]);
		}
		reader.close();

		// check to see if the current score is higher than one of the other
		// scores (indexes toward 0 are the highest and indexes toward 2 are
		// lowest)
		int indexWhereScoreIsHigher = -1;
		for (int player = 0; player < levelNames.length; player++)
		{
			if (score > levelScores[player])
			{
				indexWhereScoreIsHigher = player;
				// Exit the loop
				player = levelNames.length;
			}
			else if (score == levelScores[player])
			{
				// When there is an equal score, compare the times to see who
				// has the higher score
				if (time < levelTimes[player])
				{
					indexWhereScoreIsHigher = player;
					// Exit the loop
					player = levelNames.length;
				}
			}
		}

		// Print the scores to the highscore file in the new order only if there
		// is a new order
		if (indexWhereScoreIsHigher > -1)
		{
			PrintWriter writer = new PrintWriter("Highscores/Level" + level
					+ ".hs");
			for (int player = 0; player < indexWhereScoreIsHigher; player++)
			{
				writer.print(levelNames[player] + " " + levelScores[player]
						+ " " + levelTimes[player] + "\n");
				writer.flush();
			}
			// print the new score
			writer.print(name + " " + score + " " + time + "\n");

			// Print the rest of the scores execpt the last 2(not last one bc it
			// is no longer top scores and not 2nd last bc that needs to be
			// printed with out a println so that there is not a new line added
			// to the level after every game played
			for (int player = indexWhereScoreIsHigher; player < levelNames.length - 1; player++)
			{
				writer.println(levelNames[player] + " " + levelScores[player]
						+ " " + levelTimes[player]);
				writer.flush();
			}
			writer.close();
		}

		// Load the new players and scores
		BufferedReader reReader = new BufferedReader(new FileReader(
				"Highscores/Level" + level + ".hs"));
		for (int player = 0; player < levelNames.length; player++)
		{
			String line = reReader.readLine();
			levelNames[player] = line.split(" ")[0];
			levelScores[player] = Integer.valueOf(line.split(" ")[1]);
			levelTimes[player] = Integer.valueOf(line.split(" ")[2]);
		}
		reReader.close();
	}
}