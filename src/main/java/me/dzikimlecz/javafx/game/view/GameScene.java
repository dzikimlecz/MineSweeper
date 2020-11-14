package me.dzikimlecz.javafx.game.view;

import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import me.dzikimlecz.javafx.AppFX;
import me.dzikimlecz.swing.game.control.MineSweeper;

public class GameScene extends Scene{
	
	private final FlowPane root;
	
	private Dimension2D gridSize;
	
	private ToggleButton toggleButton;
	
	
	public GameScene() {
		super(new FlowPane());
		root = (FlowPane) this.getRoot();
		
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
		
		toggleButton = new ToggleButton(AppFX.PICKAXE_EMOJI);
		toggleButton.setOnAction(event -> System.out.println());
//		menuPane.getChildren().setLeft(timer);
		menuPane.setRight(toggleButton);
		
		root.setOrientation(Orientation.VERTICAL);
		root.getChildren().addAll(gamePane, menuPane);
		
		
	}
	
}