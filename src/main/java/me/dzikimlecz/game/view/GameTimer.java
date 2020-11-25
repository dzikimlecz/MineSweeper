package me.dzikimlecz.game.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Label representing a Stopper counting minutes and seconds in separate thread
 */
public class GameTimer extends Label {
	
	private final Timer stopper;
	private final AtomicInteger seconds;
	private final AtomicInteger minutes;
	
	/**
	 * Every second increments seconds and repaints. Every 60s increments minutes
	 */
	private static class StopperTask extends TimerTask {
		private final GameTimer parent;
		
		public StopperTask(GameTimer parent) {
			this.parent = parent;
		}
		
		@Override
		public void run() {
			if(parent.seconds.incrementAndGet() == 60) {
				parent.seconds.set(0);
				parent.minutes.incrementAndGet();
			}
			Platform.runLater(parent::repaint);
		}
	}
	
	/**
	 * Condition required by stopper to run.
	 * If it's value is false, stopper stops itself
	 */
	private boolean isRunning = false;
	
	/**
	 * Creates new GameTimer instance
	 */
	public GameTimer() {
		super(" 0:00");
		seconds = new AtomicInteger(0);
		minutes = new AtomicInteger(0);
		stopper = new Timer("StopperD", true);
		this.getStyleClass().add("timer");
	}
	
	/**
	 * Starts measuring the time.
	 */
	public void start() {
		if (isRunning)
			throw new IllegalStateException("Tried to start already running Stopper instance");
		isRunning = true;
		stopper.scheduleAtFixedRate(new StopperTask(this), 500, 1000);
	}
	
	/**
	 * Stops measuring the time.
	 */
	public void stop() {
		if (!isRunning)
			throw new IllegalStateException("Tried to stop not started Stopper instance");
		stopper.cancel();
		isRunning = false;
		repaint();
	}
	
	public void reset() {
		seconds.set(0);
		minutes.set(0);
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
		return String.format("%2d:%02d", minutes.get(), seconds.get());
	}
}
