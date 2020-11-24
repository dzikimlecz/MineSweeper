package me.dzikimlecz.javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.dzikimlecz.javafx.components.BorderTitlePane;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.Theme;
import me.dzikimlecz.javafx.game.model.GameConfigs;

import java.util.List;

public class ConfigsScene extends Scene {
	
	private final ComboBox<String> difficultyBox;
	private final ComboBox<String> themeBox;
	private final GameConfigs gameConfigs;
	private final Stage window;
	
	public ConfigsScene(GameConfigs gameConfigs, Stage window) {
		super(new BorderTitlePane("MineSweeper"));
		
		GridPane grid;
		((BorderTitlePane) this.getRoot()).add(grid = new GridPane());
		grid.setId("configGrid");
		
		this.gameConfigs = gameConfigs;
		this.window = window;
		List<javafx.scene.Node> gridChildren = grid.getChildren();
		
		FlowPane difficultyPane = new FlowPane();
		difficultyPane.getStyleClass().add("configPane");
		
		Label difficultyLabel = new Label("Difficulty: ");
		difficultyLabel.getStyleClass().add("configLabel");
		
		difficultyBox = new ComboBox<>();
		difficultyBox.getStyleClass().add("configBox");
		for (Difficulty difficulty : Difficulty.values())
			difficultyBox.getItems().add(difficulty.parseString());
		difficultyPane.getChildren().addAll(difficultyLabel, difficultyBox);
		setFontSize(difficultyBox, 14);
		
		difficultyBox.getSelectionModel().select(0);
		
		FlowPane themePane = new FlowPane();
		themePane.getStyleClass().add("configPane");
		
		Label themeLabel = new Label("Theme: ");
		themeLabel.getStyleClass().add("configLabel");
		
		themeBox = new ComboBox<>();
		themeBox.getStyleClass().add("configBox");
		for (Theme theme : Theme.values())
			themeBox.getItems().add(theme.parseString());
		themePane.getChildren().addAll(themeLabel, themeBox);
		themeBox.getSelectionModel().select(0);
		setFontSize(themeBox, 14);
		
		BorderPane proceedPane = new BorderPane();
		proceedPane.setId("proceedPane");
		Button startButton = new Button("Start");
		startButton.getStyleClass().add("configButton");
		
		startButton.setOnAction(e -> commit());
		startButton.setMinWidth(49);
		proceedPane.setBottom(startButton);
		
		
		
		GridPane.setConstraints(difficultyPane, 0, 0);
		GridPane.setConstraints(themePane, 0, 1);
		GridPane.setConstraints(proceedPane, 1, 1);
		
		gridChildren.add(difficultyPane);
		gridChildren.add(themePane);
		gridChildren.add(proceedPane);
		
		getStylesheets().addAll("styles/config-styles.css", "styles/BorderTitlePane.css");
	}
	
	
	public void commit() {
		gameConfigs.register("difficulty",
		                     Difficulty.parseDifficulty(difficultyBox.getValue()));
		gameConfigs.register("theme",
		                     Theme.parseTheme(themeBox.getValue()));
		window.hide();
	}
	
	public static void setFontSize(ComboBox<String> comboBox, double size) {
		class ComboBoxCell extends ListCell<String> {
			private final Text text;
			ComboBoxCell() {
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				text = new Text();
				text.setFont(Font.font(size));
			}
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setGraphic(null);
				} else {
					text.setText(item);
					
					setGraphic(text);
				}
			}
		}
		comboBox.setButtonCell(new ComboBoxCell());
		comboBox.setCellFactory(param -> new ComboBoxCell());
	}
	
}
