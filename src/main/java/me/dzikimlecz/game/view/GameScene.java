package me.dzikimlecz.game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import me.dzikimlecz.App;
import me.dzikimlecz.components.BorderTitlePane;

public class GameScene extends Scene{
	
	private EventHandler<ActionEvent> eventListeners;
	
	private final BorderTitlePane root;
	
	private final BorderPane innerPane;
	
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
		innerPane = new BorderPane();
		root.add(innerPane);
		toggleButton = new Button(App.PICKAXE_EMOJI);
		toggleButton.setId(toggleButtonId());
		timer = new GameTimer();
	}
	
	public void fill(GameCell[][] cells, String css) {
		
		//pane containing cells
		GridPane gamePane = new GridPane();
		gamePane.getStyleClass().add("game-grid");
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
		menuPane.setLeft(timer);
		menuPane.setCenter(toggleButton);
		
		innerPane.setTop(menuPane);
		innerPane.setBottom(gamePane);
		
		this.getStylesheets().addAll("styles/BorderTitlePane.css",
		                             "styles/game-style.css",
		                             css);
	}
	
	public void setEventListeners(EventHandler<ActionEvent> eventListeners) {
		this.eventListeners = eventListeners;
	}
	
	public void setTitle(String title) {
		root.setTitle(title);
	}
}
