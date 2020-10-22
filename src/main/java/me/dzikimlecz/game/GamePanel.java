package me.dzikimlecz.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel  extends JPanel {
	private final String BOMB_EMOJI = "\uD83D\uDCA3";
	
	
	private final JButton uncoverButton;
	private final ActionListener generatePhaseListener = (event) -> {
		GameEventManager.getInstance().getGame().getFrame().generateFromCell(this);
		uncover();
	};
	private final ActionListener uncoverListener = (event) -> uncover();
	
	private boolean hasBomb;
	public boolean hasBomb() {
		return hasBomb;
	}
	
	private String content;
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
	
	public GamePanel(int x, int y) {
		super();
		this.isCovered = true;
		this.location = new Dimension(x, y);
		hasBomb = false;
		content = null;
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new BorderLayout());
		uncoverButton = new JButton(".");
		uncoverButton.addActionListener(generatePhaseListener);
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	public void fill(boolean hasBomb, String content) {
		this.uncoverButton.removeActionListener(generatePhaseListener);
		this.hasBomb = hasBomb;
		this.content = (content != null) ? content : ((hasBomb) ? BOMB_EMOJI : "");
		this.uncoverButton.addActionListener(uncoverListener);
	}
	
	public void uncover() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
		MineSweeper game = GameEventManager.getInstance().getGame();
		if (hasBomb) game.endGame(false);
		else if (game.getFrame().allClear()) game.endGame(true);
		else if (content.equals("")) game.getFrame().processNearbyCells(this);
	}
	
	public void uncoverSafe() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		if (hasBomb)
			throw new IllegalStateException("Tried to safely uncover bomb cell.");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
	}
	
}
