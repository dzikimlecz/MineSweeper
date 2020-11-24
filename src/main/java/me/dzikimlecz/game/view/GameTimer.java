package me.dzikimlecz.game.view;

import javafx.scene.control.Label;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Label representing a Stopper counting minutes and seconds in separate thread
 */
public class GameTimer extends Label {
	
	private byte seconds;
	private byte minutes;
	
	/**
	 * The Thread in which Stopper is running
	 */
	private final Runnable stopper;
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
		stopper = () -> {
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
				repaint();
			}
		};
		
	}
	
	/**
	 * Starts measuring the time.
	 */
	public synchronized void start() {
		//Platform.runLater(stopper);
	}
	
	/**
	 * Stops measuring the time.
	 */
	public synchronized void stop() {
		isRunning.set(false);
		repaint();
	}
	
	/**
	 * Sets text to formatted time string
	 */
	public void repaint() {
		setText(this.toString());
	}
	
	/**
	 * Formats seconds and minutes to (m:ss) (max 2 digits for m)
	 * @return string of formatted seconds and minutes
	 */
	@Override
	public String toString() {
		return String.format("%2d:%02d", minutes, seconds);
	}
}
