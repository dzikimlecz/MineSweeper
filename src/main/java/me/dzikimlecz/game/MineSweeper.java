package me.dzikimlecz.game;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

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
		var gameLostFrame = new JFrame("Boom");
		gameLostFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameLostFrame.setSize(250, 130);
		gameLostFrame.setResizable(false);
		gameLostFrame.setLayout(new BorderLayout());
		String text = (wasGameWon) ? "Congrats! You made it!" : "You've lost :(";
		var label = new JLabel(text, SwingConstants.CENTER);
		gameLostFrame.add(label, BorderLayout.CENTER);
		gameLostFrame.setLocationRelativeTo(null);
		gameLostFrame.setVisible(true);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				frame.setVisible(false);
			}
		}, 750);
		gameLostFrame.setAlwaysOnTop(true);
	}
}
