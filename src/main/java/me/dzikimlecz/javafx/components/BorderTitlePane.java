package me.dzikimlecz.javafx.components;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class BorderTitlePane extends StackPane {
	
	Label titleLabel;
	StackPane contentPane;
	
	public BorderTitlePane(String title) {
		
		titleLabel = new Label(' ' + title + ' ');
		titleLabel.getStyleClass().add("border-title-pane-label");
		StackPane.setAlignment(titleLabel, Pos.TOP_LEFT);
		
		contentPane = new StackPane();
		contentPane.getStyleClass().add("border-title-pane-content");
		
		this.getStyleClass().add("border-title-pane");
		this.getChildren().addAll(titleLabel, contentPane);
		this.getStylesheets().add("styles/BorderTitlePane.css");
	}
	
	public void add(Node node) {
		contentPane.getChildren().add(node);
	}
	
	public void setTitle(String title) {
		titleLabel.setText(' ' + title + ' ');
	}
}
