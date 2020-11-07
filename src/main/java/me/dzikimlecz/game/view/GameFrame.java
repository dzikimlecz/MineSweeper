package me.dzikimlecz.game.view;

import me.dzikimlecz.game.control.MineSweeper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Main game window, contains all cells
 * @see GameCell
 * @see GameTimer
 */
public class GameFrame extends JFrame {
	/**
	 * width and height of {@code segmentPanels}
	 */
	private Dimension frameSize;
	/**
	 * Timer showing time passed from start of the game
	 */
	private final GameTimer timer;
	/**
	 * Button used to switch mode of interaction with cells.
	 */
	private JToggleButton toggleButton;
	/**
	 * Panel containing all components
	 */
	private JPanel mainPanel;
	
	
	public GameFrame() {
		super("MineSweeper");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.timer = new GameTimer();
	}
	
	public void setSize(int x, int y) {
		this.frameSize = new Dimension(x, y);
	}
	
	public void fill(GameCell[][] cells) {
		frameSize = new Dimension(cells[0].length, cells.length);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		//panel containing cells
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.lightGray);
		gamePanel.setLayout(new GridLayout(frameSize.height, frameSize.width));
		
		for (GameCell[] cellsRow : cells)
			for (GameCell cell : cellsRow)
				gamePanel.add(cell);
		
		//panel containing timer and toggle
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		
		toggleButton = new JToggleButton(MineSweeper.PICKAXE_EMOJI);
		toggleButton.addItemListener(event -> MineSweeper.getInstance().switchToggleMode());
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
		timer.start();
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 * stops measuring time in {@code timer}.
	 */
	public void stopTimer() {
		timer.stop();
	}
	/**
	 * gets formatted time from {@code timer}.
	 * @return time in format (m:SS).
	 */
	public String getTime() {
		return timer.toString();
	}
	
	public JToggleButton getToggleButton() {
		return toggleButton;
	}
	
	public void setTitle(String title) {
		((TitledBorder) mainPanel.getBorder()).setTitle(title);
	}
}
