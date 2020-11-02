package me.dzikimlecz.game;

import javax.swing.JLabel;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameTimer extends JLabel {
	private byte seconds;
	private byte minutes;
	private Thread stopperThread;
	private AtomicBoolean isRunning = new AtomicBoolean(true);
	
	public GameTimer() {
		super("  0:00");
		seconds = 0;
		minutes = 0;
		stopperThread = new Thread(() -> {
			while (isRunning.get()) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(++seconds > 60) {
					seconds = 0;
					if (++minutes > 60)
						minutes = 0;
				}
				this.setText(this.toString());
				this.repaint();
			}
		});
		
	}
	
	public void start() {
		stopperThread.start();
	}
	
	public void stop() {
		isRunning.set(false);
		this.setText(this.toString());
		this.repaint();
	}
	
	@Override
	public String toString() {
		return String.format("%2d:%02d", minutes, seconds);
	}
}
