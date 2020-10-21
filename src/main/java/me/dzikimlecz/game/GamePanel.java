package me.dzikimlecz.game;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GamePanel  extends JPanel {
	private final JButton uncoverButton;
	
	private final boolean hasBomb;
	public boolean hasBomb() {
		return hasBomb;
	}
	
	private final String content;
	
	@Nullable
	public String getContent() {
		return content;
	}
	
	private boolean isCovered;
	public boolean isCovered() {
		return isCovered;
	}
	
	private final Dimension location;
	public Dimension getLoc() {
		return location;
	}
	
	public GamePanel(boolean hasBomb, String content, int x, int y) {
		super();
		this.hasBomb = hasBomb;
		this.content = content;
		this.isCovered = true;
		this.location = new Dimension(x, y);
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(Color.red));
		uncoverButton = new JButton("O");
		uncoverButton.addActionListener((event) -> uncover());
		this.setLayout(new BorderLayout());
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	public void uncover() {
		this.setVisible(false);
		this.remove(uncoverButton);
		String text = (hasBomb) ? "\uD83D\uDCA3" : content;
		this.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
		MineSweeper game = GameEventManager.getInstance().getGame();
		if (hasBomb) game.endGame(false);
		else if (game.getFrame().allClear()) game.endGame(true);
		else if (content.equals("")) game.getFrame().processNearbyCells(this);
	}
	
	public void uncoverSafe() {
		if (hasBomb) throw new IllegalStateException("Could not uncover safely.");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
	}
}
