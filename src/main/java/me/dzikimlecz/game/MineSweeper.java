package me.dzikimlecz.game;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.enums.ToggleMode;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MineSweeper {
	public static final String FLAG_EMOJI = "\uD83D\uDEA9";
	public static final String PICKAXE_EMOJI = "\u26CF";
	public static final String BOMB_EMOJI = "\uD83D\uDCA3";
	
	private final GameFrame frame;
	public GameFrame getFrame() {
		return frame;
	}
	
	private ToggleMode toggleMode;
	public ToggleMode getToggleMode() {
		return toggleMode;
	}
	public void setToggleMode(ToggleMode toggleMode) {
		this.toggleMode = toggleMode;
	}
	
	public MineSweeper(Difficulty difficulty) {
		GameEventManager.getInstance().registerGame(this);
		frame = new GameFrame(difficulty);
		frame.setLocationRelativeTo(null);
		frame.toFront();
		frame.setVisible(true);
	}
	
	
	
	public void endGame(boolean wasGameWon) {
		String title = (wasGameWon) ? "All Clear!" : "Boom";
		String text = (wasGameWon) ? "Congrats! You made it!" : "You've lost :(";
		JDialog gameEndDialog = new JDialog(frame, title, true);
		gameEndDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		gameEndDialog.setSize(250, 130);
		gameEndDialog.setResizable(false);
		gameEndDialog.setLocationRelativeTo(frame);
		gameEndDialog.setLayout(new BorderLayout());
		
		gameEndDialog.addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				frame.dispose();
			}
			public void windowOpened(WindowEvent ignore) {}
			public void windowClosing(WindowEvent ignore) {}
			public void windowIconified(WindowEvent ignore) {}
			public void windowDeiconified(WindowEvent ignore) {}
			public void windowActivated(WindowEvent ignore) {}
			public void windowDeactivated(WindowEvent ignore) {}
		});
		
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		gameEndDialog.add(label, BorderLayout.CENTER);
		
		gameEndDialog.setVisible(true);
	}
}
