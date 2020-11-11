package me.dzikimlecz.javafx;

import javafx.application.Application;
import javafx.stage.Stage;
import me.dzikimlecz.javafx.game.view.GameWindow;

public class AppFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private Stage configsStage;
	private Stage gameStage;
	private ConfigsWindow configsWindow;
	private GameWindow gameWindow;
	
	
	@Override
	public void start(Stage primaryStage) {
		configsWindow = new ConfigsWindow();
		gameWindow = new GameWindow();
		configsStage = new Stage();
		gameStage = primaryStage;
		initConfigs();
	}
	
	
	private void initConfigs() {
		configsWindow.start(configsStage);
	}
	
	private void startGame() {
		gameWindow.start(gameStage);
	}
	
	@Override
	public void stop() {
//		Platform.exit();
	}
}
