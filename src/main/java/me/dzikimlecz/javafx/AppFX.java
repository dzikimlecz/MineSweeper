package me.dzikimlecz.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import me.dzikimlecz.javafx.game.view.GameScene;

public class AppFX extends Application {
	public static final String PICKAXE_EMOJI = "";
	private static AppFX instance;
	
	public static AppFX getInstance() {
		return instance;
	}
	
	private GameScene gameScene;
	private Stage window;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		window = primaryStage;
		startConfigs();
	}
	
	private void startConfigs() {
		window.setTitle("Game Launcher");
		window.setScene(new ConfigsScene());
		window.setWidth(410);
		window.setHeight(200);
		window.setResizable(false);
		window.centerOnScreen();
		window.show();
		window.toFront();
	}
	
	public void startGame() {
		window.hide();
		window.setTitle("MineSweeper");
		gameScene = new GameScene();
		window.setScene(gameScene);
		window.sizeToScene();
		window.centerOnScreen();
		try {
			Thread.sleep(200);
		} catch (Exception ignore) {}
		window.show();
		window.toFront();
	}
	
	public GameScene getGameScene() {
		return gameScene;
	}
	
	public static void main(String[] args) {
		launch();
	}
}
