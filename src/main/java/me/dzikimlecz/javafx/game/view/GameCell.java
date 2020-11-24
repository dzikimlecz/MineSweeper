package me.dzikimlecz.javafx.game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import me.dzikimlecz.javafx.AppFX;
import org.jetbrains.annotations.Nullable;

public class GameCell extends StackPane {
	
	///////////////////////////////////////////////////////////////////////////
	// Static constants
	///////////////////////////////////////////////////////////////////////////
	public static final String cellButonIdPrefix = "cell-button ";
	private static final int cellSize = 30;
	
	///////////////////////////////////////////////////////////////////////////
	// Components
	///////////////////////////////////////////////////////////////////////////
	private final Button uncoverButton;
	private final Label label;
	
	///////////////////////////////////////////////////////////////////////////
	// properties
	///////////////////////////////////////////////////////////////////////////
	private final Dimension2D location;
	private String content;
	private boolean isMined;
	private boolean isCovered;
	private boolean isMarked;
	
	///////////////////////////////////////////////////////////////////////////
	//properties getters
	///////////////////////////////////////////////////////////////////////////
	public Dimension2D getLocation() {
		return location;
	}
	
	public boolean isMined() {
		return isMined;
	}
	
	public boolean isCovered() {
		return isCovered;
	}
	
	public boolean isNotMarked() {
		return !isMarked;
	}
	
	public boolean isClear() {
		return content.trim().isEmpty();
	}
	
	
	public GameCell(int x, int y, EventHandler<ActionEvent> eventListeners) {
		super();
		this.isCovered = true;
		this.location = new Dimension2D(x, y);
		this.uncoverButton = new Button("");
		this.label = new Label();
		label.getStyleClass().add("cell-label");
		uncoverButton.getStyleClass().add(cellButonIdPrefix.trim());
		uncoverButton.setId(cellButonIdPrefix + x + ':' + y);
		uncoverButton.setOnAction(eventListeners);
		
		this.setPrefSize(cellSize, cellSize);
		uncoverButton.setPrefSize(cellSize, cellSize);
		this.getChildren().add(uncoverButton);
	}
	
	public void fill(boolean isMined, @Nullable String content) {
		this.isMined = isMined;
		this.content = (content != null) ? content : (isMined) ? AppFX.BOMB_EMOJI : "";
	}
	
	public void uncover() {
		if (content == null)
			throw new IllegalStateException("Tried to uncover not-filled GamePanel");
		this.getChildren().remove(uncoverButton);
		label.setText(content);
		if(!this.getChildren().contains(label))
			this.getChildren().add(label);
		this.isCovered = false;
	}
	
	public void setMark(boolean marked) {
		isMarked = marked;
		uncoverButton.setText((isMarked) ? AppFX.FLAG_EMOJI : "");
	}
	
}
