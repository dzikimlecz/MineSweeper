package me.dzikimlecz.javafx.game.Control;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameProperties {
	private static final Map<String, Object> gameProperties;
	
	
	public static void register(String key, @NotNull Object object) {
		gameProperties.putIfAbsent(key.toLowerCase(), object);
	}
	
	
	public static Object get(String key) {
		return gameProperties.get(key);
	}
	
	
	
	static {
		gameProperties = new HashMap<>();
	}
}
