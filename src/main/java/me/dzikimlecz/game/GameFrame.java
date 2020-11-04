package me.dzikimlecz.game;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.enums.ToggleMode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main game window, contains all cells
 * @see GameCellPanel
 * @see GameTimer
 */
public class GameFrame extends JFrame {
	/**
	 * All cells contained in the frame
	 */
	private GameCellPanel[][] segmentPanels;
	/**
	 * Amount of mined cells in {@code segmentPanels}
	 */
	protected final int minesAmount;
	/**
	 * width and height of {@code segmentPanels}
	 */
	private Dimension frameSize;
	/**
	 * amount of flagged cells in {@code segmentPanels}
	 */
	protected int flags;
	
	/**
	 * Timer showing time passed from start of the game
	 */
	private GameTimer timer;
	/**
	 * stops measuring time in {@code timer}.
	 */
	protected void stopTimer() {
		timer.stop();
	}
	/**
	 * gets formatted time from {@code timer}.
	 * @return time in format (m:SS).
	 */
	protected String getTime() {
		return timer.toString();
	}
	
	/**
	 * Border of the frame
	 */
	private TitledBorder border;
	/**
	 * Gets border of the frame.
	 * @return TitledBorder instance being current border of frame.
	 */
	protected TitledBorder getBorder() {
		return border;
	}
	
	/**
	 * Indicates if mines were already generated.
	 */
	private boolean generated;
	
	/**
	 * creates new GameFrame with amount of cells and mines based on selected difficulty
	 * @param difficulty base for generating cells details.
	 */
	protected GameFrame(Difficulty difficulty) {
		super("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		switch (difficulty) {
			//case DEBUG:
			case EASY:
				frameSize = new Dimension(10, 14);
				break;
			case MEDIUM:
				frameSize = new Dimension(12, 22);
				break;
			case HARD:
				frameSize = new Dimension(27, 30);
				break;
			case EXTREME:
				frameSize = new Dimension(40, 35);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + difficulty);
		}
		
		this.minesAmount = (int) (difficulty.getMinesFactor() * frameSize.width * frameSize.height);
		this.flags = 0;
		this.timer = new GameTimer();
		this.generated = false;
		this.segmentPanels = new GameCellPanel[frameSize.height][frameSize.width];
		
		//panel containing all components
		JPanel mainPanel = new JPanel();
		border = BorderFactory.createTitledBorder("Mines: " + minesAmount);
		mainPanel.setBorder(border);
		mainPanel.setLayout(new GridBagLayout());
		
		//panel containing cells
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.lightGray);
		gamePanel.setLayout(new GridLayout(frameSize.height, frameSize.width));
		for (int y = 0; y < segmentPanels.length; y++)
			for (int x = 0; x < segmentPanels[y].length; x++)
				gamePanel.add(segmentPanels[y][x] = new GameCellPanel(x, y));
		//panel containing timer and toggle
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		//button used to switch mode of interaction with cells.
		JToggleButton toggleButton = new JToggleButton(MineSweeper.PICKAXE_EMOJI);
		toggleButton.addItemListener(event -> {
			if (generated) {
				MineSweeper game = GameManager.getInstance().getGame();
				if (toggleButton.isSelected()) {
					toggleButton.setText(MineSweeper.FLAG_EMOJI);
					game.setToggleMode(ToggleMode.MARK);
				} else {
					toggleButton.setText(MineSweeper.PICKAXE_EMOJI);
					game.setToggleMode(ToggleMode.DIG);
				}
			} else if(toggleButton.isSelected()) toggleButton.doClick();
		});
		toggleButton.setBackground(Color.lightGray);
		menuPanel.add(timer, BorderLayout.LINE_START);
		menuPanel.add(toggleButton, BorderLayout.CENTER);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 5;
		mainPanel.add(menuPanel, gbc);
		
		gbc.weighty = 95;
		gbc.gridy = 1;
		mainPanel.add(gamePanel, gbc);
		
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setResizable(false);
	}
	
	/**
	 * generates mines starting from selected cell
	 * @param cell cell to be not surrounded by any mines
	 */
	protected void generateFromCell(GameCellPanel cell) {
		GameManager.getInstance().getGame().setToggleMode(ToggleMode.DIG);
		GameCellPanel[][] nearbyCells = getNearbyCells(cell);
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
		for (int y = 0; y < segmentPanels.length; y++) {
			for (int x = 0; x < segmentPanels[y].length; x++) {
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
				segmentPanels[y][x].fill(hasMine, content);
			}
		}
		generated = true;
		timer.start();
	}
	
	/**
	 * Recursively uncovers cells surrounded by ordered empty cell
	 * @param cell empty cell being starting point of uncovering.
	 */
	protected void uncoverNearbyCells(GameCellPanel cell) {
		GameCellPanel[][] nearbyCells = getNearbyCells(cell);
		for (GameCellPanel[] row : nearbyCells) {
			for (GameCellPanel nearbyCell : row) {
				if (nearbyCell != null && nearbyCell.isCovered()) {
					nearbyCell.uncoverSafe();
					if ( nearbyCell.getContent().equals("")) uncoverNearbyCells(nearbyCell);
				}
			}
		}
		if (allClear()) GameManager.getInstance().getGame().endGame(true);
	}
	
	/**
	 * checks if all cells are cleared
	 * @return {@code true}, if all cells are uncovered or contain a mine, otherwise {@code false}
	 */
	protected boolean allClear() {
		return Arrays.stream(segmentPanels).allMatch(
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
	private GameCellPanel[][] getNearbyCells(GameCellPanel cell) {
		int x = cell.getLoc().width;
		int y = cell.getLoc().height;
		GameCellPanel[][] cells = new GameCellPanel[3][3];
		int[] yCoords = new int[]{y - 1, y, y + 1};
		for (int yIndex = 0, yCoordsLength = yCoords.length; yIndex < yCoordsLength; yIndex++) {
			int y1 = yCoords[yIndex];
			int[] xCoords = new int[]{x - 1, x, x + 1};
			for (int xIndex = 0, xCoordsLength = xCoords.length; xIndex < xCoordsLength; xIndex++) {
				int x1 = xCoords[xIndex];
				/*
				if cell is non-existent in segmentPanels or is the one passed as an arg, fills
				 with null reference
				*/
				if ((y1 == y && x1 == x) ||
						(y1 < 0 || x1 < 0 || y1 >= frameSize.height || x1 >= frameSize.width)) {
					cells[yIndex][xIndex] = null;
					continue;
				}
				cells[yIndex][xIndex] = segmentPanels[y1][x1];
			}
		}
		return cells;
	}
	
	/**
	 * clears memory
	 */
	protected void clear() {
		segmentPanels = null;
	}
}
