package me.dzikimlecz.swing.game.control;

import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.ToggleMode;
import me.dzikimlecz.swing.game.view.GameCell;
import me.dzikimlecz.swing.game.view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *Class representing the game.
 * @see GameFrame
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
	
	
	private static MineSweeper instance;
	
	private Dimension frameSize;
	/**
	 *Main Window of the game
	 */
	private GameFrame frame;
	/**
	 * All cells contained in the frame
	 */
	private GameCell[][] cells;
	/**
	 * Field representing state of toggleButton in {@code frame}, switch between digging and
	 * marking a cell
	 */
	private ToggleMode toggleMode;
	private int minesAmount;
	private boolean hasStarted;
	
	public static MineSweeper getInstance() {
		if (instance == null) instance = new MineSweeper();
		return instance;
	}
	
	private MineSweeper(){}
	
	public void startGame(Difficulty difficulty) {
		hasStarted = false;
		frame = new GameFrame();
		int x;
		int y;
		switch (difficulty) {
			//case DEBUG:
			case EASY:
				x = 10;
				y = 14;
				break;
			case MEDIUM:
				x = 12;
				y = 22;
				break;
			case HARD:
				x = 27;
				y = 30;
				break;
			case EXTREME:
				x = 40;
				y = 35;
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + difficulty);
		}
		frameSize = new Dimension(x, y);
		frame.setGridSize(x, y);
		cells = new GameCell[y][x];
		minesAmount = (int) (x * y * difficulty.getMinesFactor());
		for (int y1 = 0; y1 < y; y1++) {
			for (int x1 = 0; x1 < x; x1++) {
				cells[y1][x1] = new GameCell(x1, y1);
			}
		}
		frame.fill(cells);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.toFront();
		frame.setVisible(true);
	}
	
	/**
	 *Ends game, locks the frame and shows popup about time and the result.
	 * @param isGameWon sets result of the game and popup message
	 */
	public void endGame(boolean isGameWon) {
		if(!isGameWon)
			for (GameCell[] cellsRow : cells)
				for (GameCell cell : cellsRow)
					if (cell.hasMine()) cell.uncover();
		frame.repaint();
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
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameEndDialog.add(new JLabel("Time: " + frame.getTime(), SwingConstants.CENTER), gbc);
		gameEndDialog.setVisible(true);
		cells = null;
	}
	
	/**
	 * generates mines starting from selected cell
	 * @param cell cell to be not surrounded by any mines
	 */
	public void generateFromCell(GameCell cell) {
		GameCell[][] nearbyCells = getNearbyCells(cell);
		List<Dimension> mineCells = new ArrayList<>(minesAmount);
		boolean invalidMine;
		for (int i = 0; i < minesAmount; i++) {
			Dimension mineLoc = new Dimension();
			/*
			Tries to randomly generate mine until it founds a valid location.
			If generated location is already mined, is a cell passed as arg, or neighbours to
			that cell, it will retry. Otherwise it will be mined.
			*/
			do {
				mineLoc.width = ThreadLocalRandom.current().nextInt(frameSize.width);
				mineLoc.height = ThreadLocalRandom.current().nextInt(frameSize.height);
				invalidMine = mineCells.contains(mineLoc) || mineLoc.equals(cell.getLoc()) ||
						Arrays.stream(nearbyCells).anyMatch(
								cells -> Arrays.stream(cells).anyMatch(
										(cell1) -> cell1 != null && cell1.getLoc().equals(mineLoc)
								));
			} while (invalidMine);
			mineCells.add(mineLoc);
		}
		// Checks how many bombs surround every cell, and fills it.
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				boolean hasMine = mineCells.contains(new Dimension(x, y));
				String content = null;
				if (!hasMine) {
					int nearbyMines = 0;
					for (int x1 : new int[]{x - 1, x, x + 1}) {
						for (int y1 : new int[]{y-1, y, y+1}) {
							if (y1 == y && x1 == x) continue;
							if (mineCells.contains(new Dimension(x1, y1)))
								nearbyMines++;
						}
					}
					content = (nearbyMines != 0) ? String.valueOf(nearbyMines) : null;
				}
				//fills
				cells[y][x].fill(hasMine, content);
			}
		}
		toggleMode = ToggleMode.DIG;
		hasStarted = true;
		frame.repaint();
	}
	
	/**
	 * Recursively uncovers cells surrounded by ordered empty cell
	 * @param cell empty cell being starting point of uncovering.
	 */
	public void uncoverNearbyCells(GameCell cell) {
		GameCell[][] nearbyCells = getNearbyCells(cell);
		for (GameCell[] row : nearbyCells) {
			for (GameCell nearbyCell : row) {
				if (nearbyCell != null && nearbyCell.isCovered()) {
					nearbyCell.uncover();
					if ( nearbyCell.isClear()) uncoverNearbyCells(nearbyCell);
				}
			}
		}
		if (allClear()) endGame(true);
	}
	
	/**
	 * checks if all cells are cleared
	 * @return {@code true}, if all cells are uncovered or contain a mine, otherwise {@code false}
	 */
	public boolean allClear() {
		return Arrays.stream(cells).allMatch(
				gameCellPanels -> Arrays.stream(gameCellPanels).allMatch(
						gameCellPanel -> !gameCellPanel.isCovered() || gameCellPanel.hasMine()));
	}
	
	/**
	 * Gets all cells surrounding selected one.
	 * @param cell location of which surroundings are ordered
	 * @return 2 dimensional array of size 3x3 containing surrounding cells. If cell lies on
	 * border, non existing surroundings will be represented as {@code null} reference. Cell used as
	 * argument is also represented as {@code null} reference.
	 */
	private GameCell[][] getNearbyCells(GameCell cell) {
		int x = cell.getLoc().width;
		int y = cell.getLoc().height;
		GameCell[][] nearbyCells = new GameCell[3][3];
		int[] yCoords = new int[]{y - 1, y, y + 1};
		for (int yIndex = 0, yCoordsLength = yCoords.length; yIndex < yCoordsLength; yIndex++) {
			int y1 = yCoords[yIndex];
			int[] xCoords = new int[]{x - 1, x, x + 1};
			for (int xIndex = 0, xCoordsLength = xCoords.length; xIndex < xCoordsLength; xIndex++) {
				int x1 = xCoords[xIndex];
				/*
				if cell is non-existent in cells or is the one passed as an arg, fills
				 with null reference
				*/
				if ((y1 == y && x1 == x) ||
						(y1 < 0 || x1 < 0 || y1 >= cells.length || x1 >= cells[0].length)) {
					nearbyCells[yIndex][xIndex] = null;
					continue;
				}
				nearbyCells[yIndex][xIndex] = cells[y1][x1];
			}
		}
		return nearbyCells;
	}
	
	public void cellClicked(GameCell cell) {
		if (!hasStarted) {
			generateFromCell(cell);
			uncoverNearbyCells(cell);
			frame.startTimer();
		}
		switch (toggleMode) {
			case DIG:
				if (cell.isNotMarked()) {
					cell.uncover();
					if (cell.hasMine()) endGame(false);
					else if (cell.isClear()) uncoverNearbyCells(cell);
					else if (allClear()) endGame(true);
				}
				break;
			case MARK:
				cell.setMark(cell.isNotMarked());
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + toggleMode);
		}
	}
	
	public void switchToggleMode() {
		JToggleButton toggleButton = frame.getToggleButton();
		if (hasStarted) {
			toggleMode = (toggleMode == ToggleMode.DIG) ? ToggleMode.MARK : ToggleMode.DIG;
			toggleButton.setText((toggleMode == ToggleMode.DIG) ? PICKAXE_EMOJI : FLAG_EMOJI);
		}
		else if (toggleButton.isSelected()) toggleButton.doClick();
	}
}






















