package me.dzikimlecz.game;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.enums.ToggleMode;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameFrame extends JFrame {
	private final GameCellPanel[][] segmentPanels;
	protected final int bombsAmount;
	private final Dimension frameSize;
	protected int flags;
	public TitledBorder getBorder() {
		return border;
	}
	
	private final TitledBorder border;
	private boolean generated;
	
	protected GameFrame(Difficulty difficulty) {
		super("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		switch (difficulty) {
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
		
		bombsAmount = (int) (difficulty.getBombsFactor() * frameSize.width * frameSize.height);
		flags = 0;
		
		this.segmentPanels = new GameCellPanel[frameSize.height][frameSize.width];
		
		JPanel mainPanel = new JPanel();
		border = BorderFactory.createTitledBorder("Bombs: " + bombsAmount);
		mainPanel.setBorder(border);
		mainPanel.setLayout(new GridBagLayout());
		
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.lightGray);
		gamePanel.setLayout(new GridLayout(frameSize.height, frameSize.width));
		for (int y = 0; y < segmentPanels.length; y++)
			for (int x = 0; x < segmentPanels[y].length; x++)
				gamePanel.add(segmentPanels[y][x] = new GameCellPanel(x, y));
		
		generated = false;
		
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.setBackground(Color.lightGray);
		JToggleButton toggleButton = new JToggleButton(MineSweeper.PICKAXE_EMOJI);
		toggleButton.addItemListener(event -> {
			if (generated) {
				MineSweeper game = GameEventManager.getInstance().getGame();
				if (toggleButton.isSelected()) {
					toggleButton.setText(MineSweeper.FLAG_EMOJI);
					game.setToggleMode(ToggleMode.MARK);
				} else {
					toggleButton.setText(MineSweeper.PICKAXE_EMOJI);
					game.setToggleMode(ToggleMode.DIG);
				}
			}
		});
		toggleButton.setBackground(Color.lightGray);
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
	
	protected void generateFromCell(GameCellPanel cell) {
		GameEventManager.getInstance().getGame().setToggleMode(ToggleMode.DIG);
		GameCellPanel[][] nearbyCells = getNearbyCells(cell);
		List<Dimension> bombCells = new ArrayList<>(bombsAmount);
		boolean invalidBomb;
		for (int i = 0; i < bombsAmount; i++) {
			Dimension bomb = new Dimension();
			do {
				Random rand = new Random();
				bomb.width = rand.nextInt(frameSize.width);
				bomb.height = rand.nextInt(frameSize.height);
				invalidBomb = bombCells.contains(bomb) || bomb.equals(cell.getLoc()) ||
						Arrays.stream(nearbyCells).anyMatch(
								cells -> Arrays.stream(cells).anyMatch(
										(cell1) -> cell1 != null && cell1.getLoc().equals(bomb)
								));
			} while (invalidBomb);
			bombCells.add(bomb);
		}
		
		for (int y = 0; y < segmentPanels.length; y++) {
			for (int x = 0; x < segmentPanels[y].length; x++) {
				boolean hasBomb = bombCells.contains(new Dimension(x, y));
				String content = null;
				if (!hasBomb) {
					int nearbyBombs = 0;
					for (int x1 : new int[]{x - 1, x, x + 1}) {
						for (int y1 : new int[]{y-1, y, y+1}) {
							if (y1 == y && x1 == x) continue;
							if (bombCells.contains(new Dimension(x1, y1)))
								nearbyBombs++;
						}
					}
					content = (nearbyBombs != 0) ? String.valueOf(nearbyBombs) : null;
				}
				
				segmentPanels[y][x].fill(hasBomb, content);
			}
		}
		generated = true;
		
	}
	
	protected void processNearbyCells(GameCellPanel cell) {
		GameCellPanel[][] nearbyCells = getNearbyCells(cell);
		for (GameCellPanel[] row : nearbyCells) {
			for (GameCellPanel nearbyCell : row) {
				if (nearbyCell != null && nearbyCell.isCovered()) {
					nearbyCell.uncoverSafe();
					if ( nearbyCell.getContent().equals("")) processNearbyCells(nearbyCell);
				}
			}
		}
		if (allClear()) GameEventManager.getInstance().getGame().endGame(true);
	}
	
	protected boolean allClear() {
		return Arrays.stream(segmentPanels).allMatch(
				gameCellPanels -> Arrays.stream(gameCellPanels).allMatch(
						gameCellPanel -> !gameCellPanel.isCovered() || gameCellPanel.hasBomb()));
	}
	
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
				if ((y1 == y && x1 == x) ||
						(y1 < 0 || x1 < 0 || y1 >= frameSize.height || x1 >= frameSize.width)) {
					cells[yIndex][xIndex] = null;
					continue;
				}
				try {
					cells[yIndex][xIndex] = segmentPanels[y1][x1];
				} catch (Exception e) {
					System.exit(34234);
				}
			}
		}
		return cells;
	}
	
}
