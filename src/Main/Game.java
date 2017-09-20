package Main;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The main class that runs the main program
 * @author Raya and Connor
 * @version 3
 */
public class Game extends JFrame
{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException
	{
		// Set up the Frame
		JFrame frame = new JFrame("Fire Grill and Water Buoy");
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setIconImage(ImageIO.read(Game.class
				.getResourceAsStream("/Players/Grill.png")));
		frame.setVisible(true);
		frame.toFront();
	}
}