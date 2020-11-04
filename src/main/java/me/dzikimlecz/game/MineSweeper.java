package me.dzikimlecz.game;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.enums.ToggleMode;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *Class representing the game.
 * @see GameFrame
 * @see GameManager
 */
public class MineSweeper {
	/**
	 * Emoji used as icon on buttons. (&#xD83D;&#xDEA9;)
	 */
	public static final String FLAG_EMOJI = "\uD83D\uDEA9";
	/**
	 * Emoji used as icon on {@code toggleMode} button. (&#x26CF;)
	 */
	public static final String PICKAXE_EMOJI = "\u26CF";
	/**
	 * Emoji used as icon on labels (&#xD83D;&#xDCA3;)
	 */
	public static final String BOMB_EMOJI = "\uD83D\uDCA3";
	
	/**
	 *Main Window of the game
	 */
	private final GameFrame frame;
	/**
	 * Getter of the instance of {@code GameFrame} used as main window of the game.
	 * @return frame containing the game.
	 */
	public GameFrame getFrame() {
		return frame;
	}
	
	/**
	 * Field representing state of toggleButton in {@code frame}, switch between digging and
	 * marking a cell
	 */
	private ToggleMode toggleMode;
	/**
	 *Gets state of switch between digging and marking a cell
	 * @return state of toggleButton in {@code frame}
	 */
	public ToggleMode getToggleMode() {
		return toggleMode;
	}
	/**
	 * Sets mode of interaction with a cell (dig or mark)
	 * @param toggleMode new mode
	 */
	public void setToggleMode(ToggleMode toggleMode) {
		this.toggleMode = toggleMode;
	}
	
	/**
	 * Constructor creating new Game
	 * @param difficulty ordered difficulty
	 */
	public MineSweeper(Difficulty difficulty) {
		GameManager.getInstance().registerGame(this);
		frame = new GameFrame(difficulty);
		frame.setLocationRelativeTo(null);
		frame.toFront();
		frame.setVisible(true);
	}
	
	/**
	 *Ends game, locks the frame and shows popup about time and the result.
	 * @param isGameWon sets result of the game and popup message
	 */
	public void endGame(boolean isGameWon) {
		//unregisters the game
		GameManager.clear();
		frame.stopTimer();
		String title = (isGameWon) ? "All Clear!" : "Boom";
		//the pop up
		JDialog gameEndDialog = new JDialog(frame, title, true);
		gameEndDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		gameEndDialog.setSize(250, 130);
		gameEndDialog.setResizable(false);
		gameEndDialog.setLocationRelativeTo(frame);
		gameEndDialog.setLayout(new GridBagLayout());
		//close all windows and exit on popup closed.
		gameEndDialog.addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				frame.dispose();
			}
			public void windowOpened(WindowEvent ignore) {}
			public void windowClosing(WindowEvent ignore) {}
			public void windowIconified(WindowEvent ignore) {}
			public void windowDeiconified(WindowEvent ignore) {}
			public void windowActivated(WindowEvent ignore) {}
			public void windowDeactivated(WindowEvent ignore) {}
		});
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.ipadx = 100;
		gbc.anchor = GridBagConstraints.CENTER;
		String text = (isGameWon) ? "Congrats! You made it! " : "You've lost :(";
		gameEndDialog.add(new JLabel(text, SwingConstants.CENTER), gbc);
		gbc.gridy = 1;
		gbc.ipady = 10;
		gameEndDialog.add(new JLabel("Time: " + frame.getTime(), SwingConstants.CENTER), gbc);
		gameEndDialog.setVisible(true);
		//clear memory.
		frame.clear();
	}
}
