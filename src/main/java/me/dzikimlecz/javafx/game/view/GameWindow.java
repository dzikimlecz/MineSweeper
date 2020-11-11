package me.dzikimlecz.javafx.game.view;

import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class GameWindow {
	
	
	public void start(Stage primaryStage) {
		primaryStage.setTitle("GameWindow");
		primaryStage.setHeight(500);
		primaryStage.setWidth(100);
		primaryStage.show();
		FlowPane mainPane = new FlowPane();
		
		
		
		
		
		
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.show();
	}
}
