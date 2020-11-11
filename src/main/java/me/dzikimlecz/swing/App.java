package me.dzikimlecz.swing;

import me.dzikimlecz.swing.game.control.ConfigFrame;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.swing.game.control.MineSweeper;

import javax.swing.SwingUtilities;

// TODO: 03.11.2020 Migrate from Swing to JavaFX. ASAP.
// TODO: 08.11.2020 nerf Hard difficulty

/**
 * Main Class, represents set up frame and starts game.
 * @see MineSweeper
 * @see ConfigFrame
 */
public class App {
	
	private static volatile Difficulty difficulty = null;
	public static void setDifficulty(Difficulty difficulty) {
		App.difficulty = difficulty;
	}
	
	/** Main method of the program
	 * Runs Configuration frame.
	 * Starts the game.
	 * @param ignored start arguments, not used in program
	 */
	public static void main(String[] ignored) {
		SwingUtilities.invokeLater(() -> {
			new ConfigFrame();
			new Thread(() -> {
				while (difficulty == null) {
					try {
						Thread.sleep(250);
					} catch (Exception ignore) {}
				}
				MineSweeper.getInstance().startGame(difficulty);
			}).start();
		});
	}
}
