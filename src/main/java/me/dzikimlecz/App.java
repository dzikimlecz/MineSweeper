package me.dzikimlecz;

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
import me.dzikimlecz.game.controller.EventListeners;
import me.dzikimlecz.game.model.GameConfigs;
import me.dzikimlecz.game.view.GameCell;
import me.dzikimlecz.game.view.GameScene;

public class App extends javafx.application.Application {
	/**
	 * Emoji used as icon on buttons. (&#x2611;&#xFE0F;)
	 */
	public static final String FLAG_EMOJI = "\u2611\uFE0F";
	/**
	 * Emoji used as icon on {@code toggleMode} button. (&#x26CF;)
	 */
	public static final String PICKAXE_EMOJI = "\u26CF";
	/**
	 * Emoji used as icon on labels (&#xD83D;&#xDCA3;)
	 */
	public static final String BOMB_EMOJI = "\uD83D\uDCA3";
	
	private static App instance;
	
	public static App getInstance() {
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
		switch (gameConfigs.getDifficulty()) {
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
				x = 44;
				y = 33;
				break;
			default:
				throw new IllegalStateException(
						"Unexpected value: " + gameConfigs.getDifficulty());
		}
		
		GameCell[][] cells = new GameCell[y][x];
		EventListeners eventListeners = new EventListeners(cells, gameScene, gameConfigs);
		gameScene.setEventListeners(eventListeners);
		for (int y1 = 0; y1 < y; y1++) {
			for (int x1 = 0; x1 < x; x1++) {
				cells[y1][x1] = new GameCell(x1, y1, eventListeners);
			}
		}
		
		gameScene.fill(cells, gameConfigs.getCSS());
		window.setScene(gameScene);
		window.sizeToScene();
		window.show();
		window.toFront();
		window.centerOnScreen();
	}
	
	public void endGame(boolean isGameWon) {
		
		String title = (isGameWon) ? "All Clear!" : "Boom";
		String text = (isGameWon) ? "Congrats! You made it! " : "You've lost :(";
		
		Stage popUpStage = new Stage(StageStyle.UTILITY);
		popUpStage.initOwner(window);
		popUpStage.initModality(Modality.WINDOW_MODAL);
		popUpStage.setOnCloseRequest(event -> window.close());
		popUpStage.setTitle(title);
		popUpStage.setWidth(270);
		popUpStage.setHeight(140);
		popUpStage.setResizable(false);
		
		Label msgLabel = new Label(text);
		msgLabel.setAlignment(Pos.CENTER);
		msgLabel.setFont(new Font(14));
		
		String time = ((GameScene) window.getScene()).getTimer().toString();
		Label timeLabel = new Label("Time: " + time);
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
	
	
	public static void applicationError(Exception e) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			frame.setTitle("Application Error");
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			javax.swing.JPanel panel = new javax.swing.JPanel();
			panel.setLayout(new java.awt.BorderLayout());
			String msgLine1;
			String msgLine2;
			if (e instanceof ClassNotFoundException) {
				msgLine1 = "Application could not be started. Please ensure, that";
				msgLine2 = "you are starting it using JRE 1.8, or JDK not newer than JDK 10";
			} else {
				StringBuilder builder = new StringBuilder(e.getClass().toString());
				builder.replace(0, builder.indexOf(" "), "Error: ");
				msgLine1 = builder.toString();
				msgLine2 = e.getMessage();
			}
			
			panel.add(new javax.swing.JLabel(msgLine1, javax.swing.JLabel.CENTER), "North" );
			panel.add(new javax.swing.JLabel(msgLine2, javax.swing.JLabel.CENTER), "South" );
			frame.getContentPane().add(panel);
			frame.pack();
			frame.setSize(frame.getWidth() + 15, frame.getHeight() + 2);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.toFront();
			frame.setVisible(true);
		});
	}
}
