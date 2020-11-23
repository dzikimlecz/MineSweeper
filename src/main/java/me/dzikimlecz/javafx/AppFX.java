package me.dzikimlecz.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.dzikimlecz.javafx.game.controller.EventListeners;
import me.dzikimlecz.javafx.game.model.GameConfigs;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.view.GameCell;
import me.dzikimlecz.javafx.game.view.GameScene;

public class AppFX extends javafx.application.Application {
	/**
	 * Emoji used as icon on buttons. (&#xD83D;&#xDEA9;)
	 */
	public static final String FLAG_EMOJI = "\uD83D\uDEA9";
	/**
	 * Emoji used as icon on {@code toggleMode} button. (&#x26CF;)
	 */
	public static final String PICKAXE_EMOJI = "\u26CF";
	/**
	 * Emoji used as icon on labels (&#xD83D;&#xDCA3;)
	 */
	public static final String BOMB_EMOJI = "\uD83D\uDCA3";
	
	private static AppFX instance;
	
	public static AppFX getInstance() {
		return instance;
	}
	
	private Stage window;
	private GameConfigs gameConfigs;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage = new Stage();
		instance = this;
		window = primaryStage;
		window.getIcons().add(new Image("bomb-icon.png"));
		gameConfigs = new GameConfigs();
		startConfigs();
		if (gameConfigs.isEmpty()) return;
		startGame();
	}
	
	private void startConfigs() {
		window.setTitle("Game Launcher");
		window.setScene(new ConfigsScene(gameConfigs, window));
		window.setWidth(420);
		window.setHeight(210);
		window.setResizable(false);
		window.centerOnScreen();
		window.toFront();
		window.showAndWait();
	}
	
	public void startGame() {
		window.setTitle("MineSweeper");
		GameScene gameScene = new GameScene();
		
		int x;
		int y;
		switch ((Difficulty) gameConfigs.get("difficulty")) {
			//case DEBUG:
			case EASY:
				x = 10;
				y = 14;
				break;
			case MEDIUM:
				x = 12;
				y = 22;
				break;
			case HARD:
				x = 27;
				y = 30;
				break;
			case EXTREME:
				x = 40;
				y = 35;
				break;
			default:
				throw new IllegalStateException(
						"Unexpected value: " + gameConfigs.get("difficulty"));
		}
		
		GameCell[][] cells = new GameCell[y][x];
		EventListeners eventListeners = new EventListeners(cells, gameScene, gameConfigs);
		gameScene.setEventListeners(eventListeners);
		for (int y1 = 0; y1 < y; y1++) {
			for (int x1 = 0; x1 < x; x1++) {
				cells[y1][x1] = new GameCell(x1, y1, eventListeners);
			}
		}
		gameScene.fill(cells);
		window.setScene(gameScene);
		window.sizeToScene();
		window.centerOnScreen();
		window.show();
		window.toFront();
	}
	
	public void endGame(boolean isGameWon) {
		String title = (isGameWon) ? "All Clear!" : "Boom";
		String text = (isGameWon) ? "Congrats! You made it! " : "You've lost :(";
		Stage popUpStage = new Stage(StageStyle.UTILITY);
		popUpStage.initModality(Modality.APPLICATION_MODAL);
		popUpStage.setOnCloseRequest(event -> window.close());
		popUpStage.setTitle(title);
		popUpStage.setWidth(270);
		popUpStage.setHeight(140);
		popUpStage.setResizable(false);
		Label msgLabel = new Label(text);
		msgLabel.setAlignment(Pos.CENTER);
		msgLabel.setFont(new Font(14));
		Label timeLabel = new Label("Time: " + " 0:00");
		timeLabel.setTextAlignment(TextAlignment.CENTER);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(msgLabel);
		borderPane.setBottom(timeLabel);
		StackPane root = new StackPane(borderPane);
		borderPane.setPadding(new Insets(50 ,10,50,10));
		popUpStage.setScene(new Scene(root));
		popUpStage.show();
		popUpStage.centerOnScreen();
		popUpStage.toFront();
		System.gc();
	}
}
