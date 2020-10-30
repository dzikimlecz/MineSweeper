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

public class GameCellPanel extends JPanel {
	private final MineSweeper game = GameEventManager.getInstance().getGame();
	
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
	
	private final JButton uncoverButton;
	private final ActionListener generatePhaseListener = event -> {
		game.getFrame().generateFromCell(this);
		uncover();
	};
	private final ActionListener gamePhaseListener = event -> {
		switch (game.getToggleMode()) {
			case DIG:
				uncover();
				break;
			case MARK:
				mark();
				break;
		}
	};
	private final ActionListener markedListener = event -> {
		switch (game.getToggleMode()) {
			case DIG:
				break;
			case MARK:
				removeMark();
				break;
		}
	};
	
	protected GameCellPanel(int x, int y) {
		super();
		this.isCovered = true;
		this.location = new Dimension(x, y);
		hasBomb = false;
		content = null;
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(new Color(0xFFE189)));
		this.setLayout(new BorderLayout());
		uncoverButton = new JButton("     ");
		uncoverButton.setBackground(Color.black);
		uncoverButton.setForeground(Color.white);
		uncoverButton.addActionListener(generatePhaseListener);
		this.add(uncoverButton, BorderLayout.CENTER);
	}
	
	protected void fill(boolean hasBomb, @Nullable String content) {
		this.uncoverButton.removeActionListener(generatePhaseListener);
		this.hasBomb = hasBomb;
		this.content = (content != null) ? content : ((hasBomb) ? MineSweeper.BOMB_EMOJI : "");
		this.uncoverButton.addActionListener(gamePhaseListener);
	}
	
	private void uncover() {
		uncoverSafe();
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
	
	private void mark() {
		uncoverButton.removeActionListener(gamePhaseListener);
		GameFrame frame = game.getFrame();
		frame.getBorder().setTitle("Bombs left: " + (frame.bombsAmount - ++frame.flags));
		frame.repaint();
		uncoverButton.setText(MineSweeper.FLAG_EMOJI);
		uncoverButton.addActionListener(markedListener);
	}
	
	private void removeMark() {
		uncoverButton.removeActionListener(markedListener);
		GameFrame frame = game.getFrame();
		frame.getBorder().setTitle("Bombs left: " + (frame.bombsAmount - --frame.flags));
		frame.repaint();
		uncoverButton.setText("     ");
		uncoverButton.addActionListener(gamePhaseListener);
	}
}
