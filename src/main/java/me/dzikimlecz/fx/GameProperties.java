package me.dzikimlecz.fx;

import java.util.HashMap;
import java.util.Map;

public class GameProperties {
	private static boolean empty;
	private final static Map<String, Object> gameProperties;
	
	public static void register(String key, Object object) {
		empty = false;
		gameProperties.put(key.toLowerCase(), object);
	}
	
	public static Object get(String key) {
		if (empty) throw new IllegalStateException("");
		return gameProperties.get(key);
	}
	
	public static boolean areEmpty() {
		return empty;
	}
	
	static {
		gameProperties = new HashMap<>();
		empty = true;
	}
}
