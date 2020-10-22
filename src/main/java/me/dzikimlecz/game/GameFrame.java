package me.dzikimlecz.game;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {
	private final GamePanel[][] segmentPanels;
	private final float bombsAmountFactor;
	private final Dimension frameSize;
	
	public GameFrame(Difficulty difficulty) {
		super("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		
		switch (difficulty) {
			case EASY -> {
				frameSize = new Dimension(10, 14);
				bombsAmountFactor = 0.1f;
			}
			case MEDIUM -> {
				frameSize = new Dimension(12, 22);
				bombsAmountFactor = 0.2f;
			}
			case HARD -> {
				frameSize = new Dimension(27, 30);
				bombsAmountFactor = 0.3f;
			}
			case EXTREME -> {
				frameSize = new Dimension(40, 35);
				bombsAmountFactor = 0.4f;
			}
			default -> throw new IllegalStateException("Unexpected value: " + difficulty);
		}
		
		this.segmentPanels = new GamePanel[frameSize.height][frameSize.width];
		mainPanel.setBackground(Color.lightGray);
		mainPanel.setLayout(new GridLayout(frameSize.height, frameSize.width));
		
		for (int y = 0; y < segmentPanels.length; y++)
			for (int x = 0; x < segmentPanels[y].length; x++)
				mainPanel.add(segmentPanels[y][x] = new GamePanel(x, y));
		
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void generateFromCell(GamePanel cell) {
		GamePanel[][] nearbyCells = getNearbyCells(cell);
		int bombsAmount = (int) (frameSize.getHeight() *  frameSize.getWidth() * bombsAmountFactor);
		List<Dimension> bombCells = new ArrayList<>(bombsAmount);
		boolean invalidBomb;
		for (int i = 0; i < bombsAmount; i++) {
			var bomb = new Dimension();
			do {
				var rand = new Random();
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
		
		
	}
	
	public void processNearbyCells(GamePanel cell) {
		GamePanel[][] nearbyCells = getNearbyCells(cell);
		for (GamePanel[] row : nearbyCells) {
			for (GamePanel nearbyCell : row) {
				if (nearbyCell != null && nearbyCell.isCovered()) {
					nearbyCell.uncoverSafe();
					if ( nearbyCell.getContent().equals("")) processNearbyCells(nearbyCell);
				}
			}
		}
		if (allClear()) GameEventManager.getInstance().getGame().endGame(true);
	}
	
	public boolean allClear() {
		return Arrays.stream(segmentPanels).allMatch(
				gamePanels -> Arrays.stream(gamePanels).allMatch(
						gamePanel -> !gamePanel.isCovered() || gamePanel.hasBomb()));
	}
	
	private GamePanel[][] getNearbyCells(GamePanel cell) {
		int x = cell.getLoc().width;
		int y = cell.getLoc().height;
		var cells = new GamePanel[3][3];
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
