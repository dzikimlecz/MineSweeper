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
	
	public static final String cellButonIdPrefix = "cell ";
	
	private final Dimension2D location;
	private final Button uncoverButton;
	private String content;
	private boolean isMined;
	private boolean isCovered;
	private boolean isMarked;
	
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
		this.content = null;
		this.uncoverButton = new Button("");
		uncoverButton.setId(cellButonIdPrefix + x + ':' + y);
		uncoverButton.setOnAction(eventListeners);
		this.setPrefSize(28, 28);
		uncoverButton.setPrefSize(28,28);
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
		this.getChildren().add(new Label(content));
		this.isCovered = false;
	}
	
	public void setMark(boolean marked) {
		isMarked = marked;
		uncoverButton.setText((isMarked) ? AppFX.FLAG_EMOJI : "");
	}
	
}
