package me.dzikimlecz.game;

import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

/**
 * Class representing a single cell in game.
 */
public class GameCellPanel extends JPanel {
	/**
	 * Instance of game used to access frame and end the game.
	 */
	private final MineSweeper game = GameManager.getInstance().getGame();
	
	private boolean hasMine;
	/**
	 * Gets if there is a mine in a cell
	 * @return {@code true} if the cell contains a mine, {@code false} if not
	 */
	protected boolean hasMine() {
		return hasMine;
	}
	
	
	private String content;
	/**
	 * Gets text content of the cell.
	 * @return A bomb emoji if the cell is mined, a digit if any neighbouring field is mined, and
	 * otherwise an empty string.
	 */
	protected String getContent() {
		return content;
	}
	
	private boolean isCovered;
	/**
	 * Gets if the field is covered by a button
	 * @return {@code true} if he field is covered, {@code false} otherwise.
	 */
	protected boolean isCovered() {
		return isCovered;
	}
	
	/**
	 * Coordinates of the cell in the frame.
	 */
	private final Dimension location;
	/**
	 * Gets location of the cell in the frame.
	 * @return Dimension object with coordinates of the cell in the frame.
	 */
	protected Dimension getLoc() {
		return location;
	}
	
	/**
	 *Button covering a cell. Deleted or marked after a click.
	 */
	private final JButton uncoverButton;
	/**
	 *Listener used before the generation of the field. After a click, bombs are generated.
	 */
	private final ActionListener generatePhaseListener = event -> {
		game.getFrame().generateFromCell(this);
		uncover();
	};
	/**
	 *Listener used after generation and before any click. Uncovers or marks a button.
	 */
	private final ActionListener gamePhaseListener = event -> {
		switch (game.getToggleMode()) {
			case DIG:
				uncover();
				break;
			case MARK:
				mark();
				break;
		}
	};
	/**
	 *Listener used after marking of the cell. Removes mark it or stays idle depending on the
	 * state of the toggle.
	 */
	private final ActionListener markedListener = event -> {
		switch (game.getToggleMode()) {
			case DIG:
				break;
			case MARK:
				removeMark();
				break;
		}
	};
	
	/**
	 * Constructs new empty object on the selected location.
	 * @param x x coordinate of the cell.
	 * @param y y coordinate of the cell.
	 */
	protected GameCellPanel(int x, int y) {
		super();
		this.isCovered = true;
		this.location = new Dimension(x, y);
		hasMine = false;
		content = null;
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(new Color(0xFFE189)));
		this.setLayout(new BorderLayout());
		uncoverButton = new JButton("     ");
		uncoverButton.setBackground(Color.black);
		uncoverButton.setForeground(Color.white);
		uncoverButton.addActionListener(generatePhaseListener);
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	/**
	 * Fills cell with an passed arguments.
	 * @param hasMine indicates if the cell is mined.
	 * @param content if an object is passed, content field is set to its reference.
	 * If {@code null} reference is passed content is  assigned is bomb emoji or an empty string if
	 * cell is mined.
	 */
	protected void fill(boolean hasMine, @Nullable String content) {
		this.uncoverButton.removeActionListener(generatePhaseListener);
		this.hasMine = hasMine;
		this.content = (content != null) ? content : ((hasMine) ? MineSweeper.BOMB_EMOJI : "");
		this.uncoverButton.addActionListener(gamePhaseListener);
	}
	
	/**
	 * Uncovers a cell and checks if the game is ended or the nearby cells should be uncovered.
	 */
	private void uncover() {
		uncoverSafe();
		if (hasMine) game.endGame(false);
		else if (game.getFrame().allClear()) game.endGame(true);
		else if (content.equals("")) game.getFrame().uncoverNearbyCells(this);
	}
	
	/**
	 * uncovers a cell
	 */
	protected void uncoverSafe() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
	}
	
	/**
	 * Prevents a cell from digging.
	 */
	private void mark() {
		uncoverButton.removeActionListener(gamePhaseListener);
		GameFrame frame = game.getFrame();
		frame.getBorder().setTitle("Mines left: " + (frame.minesAmount - ++frame.flags));
		frame.repaint();
		uncoverButton.setText(MineSweeper.FLAG_EMOJI);
		uncoverButton.addActionListener(markedListener);
	}
	
	/**
	 * Allows to dig a cell.
	 */
	private void removeMark() {
		uncoverButton.removeActionListener(markedListener);
		GameFrame frame = game.getFrame();
		frame.getBorder().setTitle("Mines left: " + (frame.minesAmount - --frame.flags));
		frame.repaint();
		uncoverButton.setText("     ");
		uncoverButton.addActionListener(gamePhaseListener);
	}
}
