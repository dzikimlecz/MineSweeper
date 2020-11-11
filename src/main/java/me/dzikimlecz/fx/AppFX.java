package me.dzikimlecz.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppFX extends Application {
	
	public static void main(String[] args) {
		javafx.application.Platform.setImplicitExit(false);
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		new ConfigsWindow().start(primaryStage);
		while (GameProperties.areEmpty()) {
			try {
				Thread.sleep(250);
			} catch (Exception ignore) {}
		}
		new GameWindow().start(primaryStage);
	}
}
