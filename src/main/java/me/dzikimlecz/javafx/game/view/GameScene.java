package me.dzikimlecz.javafx.game.view;

import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import me.dzikimlecz.javafx.AppFX;
import me.dzikimlecz.javafx.game.Control.EventListeners;

public class GameScene extends Scene{
	
	private final BorderPane root;
	
	private Dimension2D gridSize;
	
	private final Button toggleButton;
	
	public Button getToggleButton() {
		return toggleButton;
	}
	
	public GameScene() {
		super(new BorderPane());
		root = (BorderPane) this.getRoot();
		toggleButton = new Button(AppFX.PICKAXE_EMOJI);
	}
	
	public void setGridSize(int x, int y) {
		this.gridSize = new Dimension2D(x, y);
	}
	
	public void fill(GameCell[][] cells) {
		gridSize = new Dimension2D(cells[0].length, cells.length);
		
		
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
		toggleButton.setOnAction(event -> EventListeners.getInstance().switchToggleMode());
//		menuPane.getChildren().setLeft(timer);
		menuPane.setCenter(toggleButton);
		
		root.setTop(menuPane);
		root.setBottom(gamePane);
		
	}
	
}
