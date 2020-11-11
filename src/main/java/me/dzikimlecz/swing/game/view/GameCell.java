package me.dzikimlecz.swing.game.view;

import me.dzikimlecz.swing.game.control.MineSweeper;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Class representing a single cell in game.
 */
public class GameCell extends JPanel {
	private boolean hasMine;
	private boolean isCovered;
	private boolean isMarked;
	private String content;
	
	/**
	 * Coordinates of the cell in the frame.
	 */
	private final Dimension location;
	/**
	 *Button covering a cell. Deleted or marked after a click.
	 */
	private final JButton uncoverButton;
	
	/*
	/**
	*Listener used before the generation of the field. After a click, bombs are generated.
	* /
	private final ActionListener generatePhaseListener = event -> {
		game.generateFromCell(this);
		uncover();
	};
	/**
	 *Listener used after generation and before any click. Uncovers or marks a button.
	 * /
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
	 * /
	private final ActionListener markedListener = event -> {
		switch (game.getToggleMode()) {
			case DIG:
				break;
			case MARK:
				removeMark();
				break;
		}
	};
	*/
	/**
	 * Gets if there is a mine in a cell
	 * @return {@code true} if the cell contains a mine, {@code false} if not
	 */
	public boolean hasMine() {
		return hasMine;
	}
	/**
	 * Gets location of the cell in the frame.
	 * @return Dimension object with coordinates of the cell in the frame.
	 */
	public Dimension getLoc() {
		return location;
	}
	/**
	 * Gets if the field is covered by a button
	 * @return {@code true} if he field is covered, {@code false} otherwise.
	 */
	public boolean isCovered() {
		return isCovered;
	}
	
	/**
	 * Gets text content of the cell.
	 * @return A bomb emoji if the cell is mined, a digit if any neighbouring field is mined, and
	 * otherwise an empty string.
	 */
	public String getContent() {
		return content;
	}
	
	public boolean isEmpty() {
		return content.equals("");
	}
	
	public boolean isNotMarked() {
		return !isMarked;
	}
	/**
	 * Constructs new empty object on the selected location.
	 * @param x x coordinate of the cell.
	 * @param y y coordinate of the cell.
	 */
	public GameCell(int x, int y) {
		super();
		this.isCovered = true;
		this.location = new Dimension(x, y);
		content = null;
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(new Color(0xFFE189)));
		this.setLayout(new BorderLayout());
		uncoverButton = new JButton("     ");
		uncoverButton.setBackground(Color.black);
		uncoverButton.setForeground(Color.white);
		uncoverButton.addActionListener(e -> MineSweeper.getInstance().cellClicked(this));
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	/**
	 * Fills cell with an passed arguments.
	 * @param hasMine indicates if the cell is mined.
	 * @param content if an object is passed, content field is set to its reference.
	 * If {@code null} reference is passed content is  assigned is bomb emoji or an empty string if
	 * cell is mined.
	 */
	public void fill(boolean hasMine, @Nullable String content) {
		this.hasMine = hasMine;
		this.content = (content != null) ? content : ((hasMine) ? MineSweeper.BOMB_EMOJI : "");
	}
	
	/**
	 * uncovers a cell
	 */
	public void uncover() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
	}
	
	public void setMark(boolean marked) {
		isMarked = marked;
		uncoverButton.setText((isMarked) ? MineSweeper.FLAG_EMOJI : "     ");
	}
}
