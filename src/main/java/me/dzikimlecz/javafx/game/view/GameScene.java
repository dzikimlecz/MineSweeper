package me.dzikimlecz.javafx.game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import me.dzikimlecz.javafx.AppFX;
import me.dzikimlecz.javafx.components.BorderTitlePane;

public class GameScene extends Scene{
	
	private EventHandler<ActionEvent> eventListeners;
	
	private final BorderTitlePane root;
	
	private final BorderPane borderPane;
	
	private final Button toggleButton;
	
	private final GameTimer timer;
	
	public Button getToggleButton() {
		return toggleButton;
	}
	
	public static String toggleButtonId() {
		return "toggle";
	}
	
	public GameTimer getTimer() {
		return timer;
	}
	
	public GameScene() {
		super(new BorderTitlePane());
		root = (BorderTitlePane) this.getRoot();
		borderPane = new BorderPane();
		toggleButton = new Button(AppFX.PICKAXE_EMOJI);
		toggleButton.setId(toggleButtonId());
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
		
		borderPane.setTop(menuPane);
		borderPane.setBottom(gamePane);
	}
	
	public void setEventListeners(EventHandler<ActionEvent> eventListeners) {
		this.eventListeners = eventListeners;
	}
	
	public void setTitle(String title) {
		root.setTitle(title);
	}
}
