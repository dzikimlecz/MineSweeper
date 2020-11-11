package me.dzikimlecz.javafx.game.view;

import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import me.dzikimlecz.javafx.game.Control.EventListeners;
import org.jetbrains.annotations.Nullable;

public class GameCell extends StackPane {
	
	private final Dimension2D location;
	private final Button uncoverButton;
	private String content;
	private boolean isMined;
	private boolean isCovered;
	
	public Dimension2D getLocation() {
		return location;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public boolean isCovered() {
		return isCovered;
	}
	
	public GameCell(int x, int y) {
		this.location = new Dimension2D(x, y);
		this.uncoverButton = new Button();
		uncoverButton.setOnAction(event -> EventListeners.cellClicked(this));
		this.isCovered = false;
	}
	
	public void fill(boolean isMined, @Nullable String content) {
		this.isMined = isMined;
		this.content = (content != null) ? content : (isMined) ? "BOMB TBA" : "";
	}
	
	
	
}
