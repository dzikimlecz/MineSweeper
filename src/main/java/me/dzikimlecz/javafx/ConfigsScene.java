package me.dzikimlecz.javafx;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.dzikimlecz.javafx.game.Control.GameProperties;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.Theme;

import java.util.List;

public class ConfigsScene extends Scene {
	
	private ComboBox<String> difficultyBox;
	private ComboBox<String> themeBox;
	
	public ConfigsScene() {
		super(new GridPane());
		
		GridPane grid = (GridPane) this.getRoot();
		grid.setPadding(new Insets(15, 10, 15, 10));
		grid.setHgap(200);
		grid.setVgap(20);
		List<javafx.scene.Node> gridChildren = grid.getChildren();
		
		FlowPane difficultyPane = new FlowPane();
		difficultyPane.setOrientation(Orientation.VERTICAL);
		difficultyPane.setVgap(7);
		
		Label difficultyLabel = new Label("Difficulty: ");
		difficultyLabel.setFont(Font.font(14));
		
		difficultyBox = new ComboBox<>();
		for (Difficulty difficulty : Difficulty.values())
			difficultyBox.getItems().add(difficulty.parseString());
		difficultyPane.getChildren().addAll(difficultyLabel, difficultyBox);
		setFontSize(difficultyBox, 14);
		
		difficultyBox.getSelectionModel().select(0);
		
		GridPane.setConstraints(difficultyPane, 0, 0);
		gridChildren.add(difficultyPane);
		
		FlowPane themePane = new FlowPane();
		themePane.setOrientation(Orientation.VERTICAL);
		themePane.setVgap(7);
		
		Label themeLabel = new Label("Theme: ");
		themeLabel.setFont(Font.font(14));
		
		themeBox = new ComboBox<>();
		for (Theme theme : Theme.values())
			themeBox.getItems().add(theme.parseString());
		themePane.getChildren().addAll(themeLabel, themeBox);
		themeBox.getSelectionModel().select(0);
		setFontSize(themeBox, 14);
		
		GridPane.setConstraints(themePane, 0, 1);
		gridChildren.add(themePane);
		
		BorderPane proceedPane = new BorderPane();
		Button startButton = new Button("Start");
		startButton.setFont(Font.font(14));
		startButton.setOnAction(this::commit);
		
		proceedPane.setBottom(startButton);
		
		GridPane.setConstraints(proceedPane, 1, 1);
		gridChildren.add(proceedPane);
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
	
	public void commit(ActionEvent event) {
		GameProperties.register("difficulty",
		                        Difficulty.parseDifficulty(difficultyBox.getValue()));
		GameProperties.register("theme",
		                        Theme.parseTheme(themeBox.getValue()));
		AppFX.getInstance().startGame();
	}
	
}
