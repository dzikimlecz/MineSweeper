package me.dzikimlecz.javafx.game.Control;

import javafx.geometry.Dimension2D;
import javafx.scene.control.ToggleButton;
import me.dzikimlecz.javafx.AppFX;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.ToggleMode;
import me.dzikimlecz.javafx.game.view.GameCell;
import me.dzikimlecz.javafx.game.view.GameScene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class EventListeners {
	
	private EventListeners(){}
	
	private static EventListeners instance;
	
	public static EventListeners getInstance() {
		if (instance == null) {
			instance = new EventListeners();
		}
		return instance;
	}
	
	private Dimension2D cellsGridSize;
	
	private GameCell[][] cells;
	
	private ToggleMode toggleMode;
	
	private int minesAmount;
	
	private boolean isGenerated;
	
	private GameScene gameScene;
	
	public void initListeners(GameCell[][] cells, GameScene gameScene) {
		this.cells = cells;
		this.gameScene = gameScene;
		isGenerated = false;
		int y = cells.length;
		int x = cells[0].length;
		Difficulty difficulty = (Difficulty) GameProperties.get("difficulty");
		minesAmount = (int)(x * y * difficulty.getMinesFactor());
		cellsGridSize = new Dimension2D(x, y);
	}
	
	public void cellClicked(GameCell cell) {
		if (!isGenerated) {
			generateFromCell(cell);
			uncoverNearbyCells(cell);
		}
		switch (toggleMode) {
			case DIG:
				if (cell.isNotMarked()) {
					cell.uncover();
					if (cell.isMined()) endGame(false);
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
	
	/**
	 *Ends game, locks the frame and shows popup about time and the result.
	 * @param isGameWon sets result of the game and popup message
	 */
	public void endGame(boolean isGameWon) {
		if(!isGameWon)
			for (GameCell[] cellsRow : cells)
				for (GameCell cell : cellsRow)
					if (cell.isMined()) cell.uncover();
		AppFX.getInstance().endGame(isGameWon);
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
						gameCellPanel -> !gameCellPanel.isCovered() || gameCellPanel.isMined()));
	}
	
	public void switchToggleMode() {
		ToggleButton toggleButton = gameScene.getToggleButton();
		if (isGenerated) {
			toggleMode = (toggleMode == ToggleMode.DIG) ? ToggleMode.MARK : ToggleMode.DIG;
			toggleButton.setText((toggleMode == ToggleMode.DIG) ? AppFX.PICKAXE_EMOJI :
					                     AppFX.FLAG_EMOJI);
		}
		else toggleButton.setSelected(false);
	}
	
	/**
	 * generates mines starting from selected cell
	 * @param cell cell to be not surrounded by any mines
	 */
	public void generateFromCell(GameCell cell) {
		List<Dimension2D> mineCells = new ArrayList<>(minesAmount);
		boolean invalidMine;
		for (int i = 0; i < minesAmount; i++) {
			int mineLocWidth;
			int mineLocHeight;
			AtomicReference<Dimension2D> mineLoc = new AtomicReference<>();
			/*
			Tries to randomly generate mine until it founds a valid location.
			If generated location is already mined, is a cell passed as arg, or neighbours to
			that cell, it will retry. Otherwise it will be mined.
			*/
			do {
				mineLocWidth = ThreadLocalRandom.current().nextInt((int) cellsGridSize.getWidth());
				mineLocHeight = ThreadLocalRandom.current().nextInt((int) cellsGridSize.getHeight());
				mineLoc.set(new Dimension2D(mineLocWidth, mineLocHeight));
				final Dimension2D loc = mineLoc.get();
				invalidMine = mineCells.contains(loc) || loc.equals(cell.getLocation()) ||
								Arrays.stream(getNearbyCells(cell)).anyMatch(
										cells -> Arrays.stream(cells).anyMatch(
												cell1 -> cell1 != null &&
														cell1.getLocation().equals(loc)
								));
				
			} while (invalidMine);
			mineCells.add(mineLoc.get());
		}
		// Checks how many bombs surround every cell, and fills it.
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				GameCell cell1 = cells[y][x];
				boolean hasMine = mineCells.contains(new Dimension2D(x, y));
				String content = null;
				if (!hasMine) {
					int nearbyMines = 0;
					for(GameCell[] row : getNearbyCells(cell1))
						for(GameCell cell2 : row)
							if (cell2 != null && mineCells.contains(cell2.getLocation()))
								nearbyMines++;
					content = (nearbyMines != 0) ? String.valueOf(nearbyMines) : null;
				}
				//fills
				cell1.fill(hasMine, content);
			}
		}
		toggleMode = ToggleMode.DIG;
		isGenerated = true;
	}
	
	/**
	 * Gets all cells surrounding selected one.
	 * @param cell location of which surroundings are ordered
	 * @return 2 dimensional array of size 3x3 containing surrounding cells. If cell lies on
	 * border, non existing surroundings will be represented as {@code null} reference. Cell used as
	 * argument is also represented as {@code null} reference.
	 */
	private GameCell[][] getNearbyCells(GameCell cell) {
		int x = (int) cell.getLocation().getWidth();
		int y = (int) cell.getLocation().getHeight();
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

}
