package me.dzikimlecz.game;

import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	protected boolean hasBomb() {
		return hasBomb;
	}
	
	private String content;
	protected String getContent() {
		return content;
	}
	
	private boolean isCovered;
	protected boolean isCovered() {
		return isCovered;
	}
	
	private final Dimension location;
	protected Dimension getLoc() {
		return location;
	}
	
	protected GamePanel(int x, int y) {
		super();
		this.isCovered = true;
		this.location = new Dimension(x, y);
		hasBomb = false;
		content = null;
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(new Color(0xFFE189)));
		this.setLayout(new BorderLayout());
		uncoverButton = new JButton("\u0000");
		uncoverButton.setBackground(Color.black);
		uncoverButton.addActionListener(generatePhaseListener);
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	protected void fill(boolean hasBomb, @Nullable String content) {
		this.uncoverButton.removeActionListener(generatePhaseListener);
		this.hasBomb = hasBomb;
		this.content = (content != null) ? content : ((hasBomb) ? BOMB_EMOJI : "");
		this.uncoverButton.addActionListener(uncoverListener);
	}
	
	private void uncover() {
		uncoverSafe();
		MineSweeper game = GameEventManager.getInstance().getGame();
		if (hasBomb) game.endGame(false);
		else if (game.getFrame().allClear()) game.endGame(true);
		else if (content.equals("")) game.getFrame().processNearbyCells(this);
	}
	
	protected void uncoverSafe() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		this.setVisible(false);
		this.remove(uncoverButton);
		this.add(new JLabel(content, SwingConstants.CENTER), BorderLayout.CENTER);
		this.setVisible(true);
		this.isCovered = false;
	}
	
}
