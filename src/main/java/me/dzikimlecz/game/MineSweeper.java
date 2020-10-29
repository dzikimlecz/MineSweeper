package me.dzikimlecz.game;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MineSweeper {
	private final GameFrame frame;
	
	public GameFrame getFrame() {
		return frame;
	}
	
	public MineSweeper(Difficulty difficulty) {
		GameEventManager.getInstance().registerGame(this);
		frame = new GameFrame(difficulty);
		frame.setAlwaysOnTop(true);
		frame.setAlwaysOnTop(false);
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
