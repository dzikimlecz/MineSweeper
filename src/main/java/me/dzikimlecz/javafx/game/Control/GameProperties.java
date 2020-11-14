package me.dzikimlecz.javafx.game.Control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameProperties {
	private static final Map<String, Object> gameProperties;
	
	
	public static void register(String key, Object object) {
		gameProperties.putIfAbsent(key.toLowerCase(), object);
	}
	
	
	public static Optional<Object> get(String key) {
		return Optional.ofNullable(gameProperties.get(key));
	}
	
	
	
	static {
		gameProperties = new HashMap<>();
	}
}
