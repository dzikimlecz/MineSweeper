package me.dzikimlecz;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.List;

public class ConfigsWindow extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Game Launcher");
		primaryStage.setWidth(480);
		primaryStage.setHeight(200);
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.toFront();
		primaryStage.setScene(initConfigScene());
		primaryStage.show();
	}
	
	private Scene initConfigScene() {
		GridPane root = new GridPane();
		List<Node> children = root.getChildren();
		Label difficultyLabel = new Label("Difficulty: ");
		ComboBox<String> difficultyBox = new ComboBox<>();
		for (int i : new int[]{1, 2, 3, 4, 5}){
			difficultyBox.getItems().addAll("Extreme");
		}
		children.add(difficultyLabel);
		children.add(difficultyBox);
		
		
		
		return new Scene(root);
	}
}
