package me.dzikimlecz.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameFrame extends JFrame {
	private final GamePanel[][] segmentPanels;
	Dimension frameSize;
	
	public GameFrame(Difficulty difficulty) {
		// FIXME: 20.10.2020 anomalies on non-extreme difficulties.
		super("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(987, 700);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		JPanel mainPanel = new JPanel();
		
		frameSize = new Dimension();
		final short bombsAmount;
		switch (difficulty) {
			case EASY -> {
				 bombsAmount = 15;
				 frameSize.width = 10;
				 frameSize.height = 16;
			}
			case MEDIUM -> {
				bombsAmount = 25;
				frameSize.width = 10;
				frameSize.height = 25;
			}
			case HARD -> {
				bombsAmount = 35;
				frameSize.width = 10;
				frameSize.height = 35;
			}
			case EXTREME -> {
				bombsAmount = 50;
				frameSize.width = 20;
				frameSize.height = 20;
			}
			default -> bombsAmount = 0;
		}
		this.segmentPanels = new GamePanel[frameSize.height][frameSize.width];
		
		mainPanel.setBackground(Color.lightGray);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Bombs: " + bombsAmount));
		mainPanel.setLayout(new GridLayout(frameSize.width, frameSize.height));
		
		List<Dimension> bombCells = new ArrayList<>(bombsAmount);
		for (int i = 0; i < bombsAmount; i++) {
			var cell = new Dimension();
			do {
				var rand = new Random();
				cell.width = rand.nextInt(frameSize.width);
				cell.height = rand.nextInt(frameSize.height);
			} while (bombCells.contains(cell));
			bombCells.add(cell);
		}
		
		for (int y = 0; y < frameSize.height; y++)
			for (int x = 0; x < frameSize.width; x++) {
				boolean hasBomb = bombCells.contains(new Dimension(x, y));
				String content = null;
				if (!hasBomb) {
					int nearBombs = 0;
					for (int x1 : new int[]{x - 1, x, x + 1})
						for (int y1 : new int[]{y - 1, y + 1})
							if (bombCells.contains(new Dimension(x1, y1))) nearBombs++;
					for (int x2 : new int[]{x - 1, x + 1})
						if (bombCells.contains(new Dimension(x2, y))) nearBombs++;
					content = (nearBombs == 0) ? "" : String.valueOf(nearBombs);
				}
				mainPanel.add(segmentPanels[y][x] = new GamePanel(hasBomb, content, x, y));
			}
		
		
		
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public void processNearbyCells(GamePanel cell) {
		int x = cell.getLoc().width;
		int y = cell.getLoc().height;
		for (int x1 : new int[]{x - 1, x, x + 1})
			for (int y1 : new int[]{y - 1, y + 1}) {
				GamePanel nextCell = segmentPanels[y1][x1];
				if ((nextCell.getContent() != null) && nextCell.getContent().equals(""))
					nextCell.uncoverSafe();
			}
		for (int x2 : new int[]{x - 1, x + 1}) {
			GamePanel nextCell = segmentPanels[y][x2];
			if ((nextCell.getContent() != null) && nextCell.getContent().equals(""))
				nextCell.uncoverSafe();
		}
		// TODO: 20.10.2020 uncovering all related cells.
		// FIXME: 20.10.2020 anomalies on non-extreme mode probably derived from constructor.
	}
	
	public boolean allClear() {
		return Arrays.stream(segmentPanels).allMatch(
				gamePanels -> Arrays.stream(gamePanels).allMatch(
						gamePanel -> !gamePanel.isCovered() || gamePanel.hasBomb()));
	}
	
}
