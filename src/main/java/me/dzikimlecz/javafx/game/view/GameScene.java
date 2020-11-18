package me.dzikimlecz.javafx.game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import me.dzikimlecz.javafx.AppFX;

public class GameScene extends Scene{
	
	private EventHandler<ActionEvent> eventListeners;
	
	private final BorderPane root;
	
	private final Button toggleButton;
	
	private final GameTimer timer;
	
	public Button getToggleButton() {
		return toggleButton;
	}
	
	public String getToggleButtonId() {
		return "toggle";
	}
	
	public GameTimer getTimer() {
		return timer;
	}
	
	public GameScene() {
		super(new BorderPane());
		root = (BorderPane) this.getRoot();
		toggleButton = new Button(AppFX.PICKAXE_EMOJI);
		toggleButton.setId(getToggleButtonId());
		timer = new GameTimer();
	}
	
	public void fill(GameCell[][] cells) {
		
		//pane containing cells
		GridPane gamePane = new GridPane();
		gamePane.setGridLinesVisible(true);
		for (int y = 0, cellsLength = cells.length; y < cellsLength; y++) {
			GameCell[] cellsRow = cells[y];
			for (int x = 0, cellsRowLength = cellsRow.length; x < cellsRowLength; x++) {
				GameCell cell = cellsRow[x];
				GridPane.setConstraints(cell, x, y);
				gamePane.getChildren().add(cell);
			}
		}
		
		//pane containing timer and toggle
		BorderPane menuPane = new BorderPane();
		toggleButton.setOnAction(eventListeners);
//		menuPane.getChildren().setLeft(timer);
		menuPane.setCenter(toggleButton);
		
		root.setTop(menuPane);
		root.setBottom(gamePane);
		
	}
	
	public void setEventListeners(EventHandler<ActionEvent> eventListeners) {
		this.eventListeners = eventListeners;
	}
}
