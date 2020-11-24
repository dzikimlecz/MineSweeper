package me.dzikimlecz.game.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GameProperties {
	private final Map<String, Object> gameProperties;
	
	public GameProperties() {
		this.gameProperties = new HashMap<>();
	}
	
	public void register(@NotNull String key, @NotNull Object object) {
		gameProperties.putIfAbsent(key.toLowerCase(), object);
	}
	
	
	public Object get(String key) {
		return gameProperties.get(key);
	}
	
	public boolean isEmpty() {
		return gameProperties.isEmpty();
	}
}
