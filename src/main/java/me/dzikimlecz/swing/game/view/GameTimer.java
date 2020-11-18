package me.dzikimlecz.swing.game.view;

import javax.swing.JLabel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JLabel subclass representing a Stopper counting minutes and seconds in separate thread
 */
public class GameTimer extends JLabel {
	
	private byte seconds;
	private byte minutes;
	
	/**
	 * The Thread in which Stopper is running
	 */
	private final Thread stopperThread;
	/**
	 * Condition required by stopper to run.
	 * If it's value is false, stopper stops itself
	 */
	private final AtomicBoolean isRunning = new AtomicBoolean(true);
	
	/**
	 * Creates new GameTimer instance
	 */
	public GameTimer() {
		super("  0:00");
		seconds = 0;
		minutes = 0;
		// Every 1000ms increments seconds and repaints. Every 60s increments minutes
		stopperThread = new Thread(() -> {
			while (isRunning.get()) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(isRunning.get() && ++seconds == 60) {
					seconds = 0;
					minutes++;
				}
				this.repaint();
			}
		});
		
	}
	
	/**
	 * Starts measuring the time
	 */
	public synchronized void start() {
		stopperThread.start();
	}
	
	/**
	 * Stops measuring the time and repaints.
	 */
	public synchronized void stop() {
		isRunning.set(false);
		this.repaint();
	}
	
	/**
	 * Sets text to formatted time string and calls method of superclass
	 */
	@Override
	public void repaint() {
		setText(this.toString());
		super.repaint();
	}
	
	/**
	 * Formats seconds and minutes to (m:ss) (max 2 digits for m)
	 * @return string of formatted seconds and minutes
	 */
	@Override
	public String toString() {
		return String.format("%2d:%02d", minutes, seconds);
	}
	
	public GameTimer clone() {
		try {
			return (GameTimer) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
