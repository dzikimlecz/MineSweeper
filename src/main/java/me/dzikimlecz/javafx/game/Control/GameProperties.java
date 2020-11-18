package me.dzikimlecz.javafx.game.Control;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GameProperties {
	private final Map<String, Object> gameProperties;
	
	public GameProperties() {
		this.gameProperties = new HashMap<>();
	}
	
	public void register(String key, @NotNull Object object) {
		gameProperties.putIfAbsent(key.toLowerCase(), object);
	}
	
	
	public Object get(String key) {
		return gameProperties.get(key);
	}
	
	
}
