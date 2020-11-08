package me.dzikimlecz;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
	private final static Map<String, Object> gameProperties;
	
	public static void init(Integer difficulty) {
		gameProperties.put("difficulty", difficulty);
	}
	
	public static Object get(String key) {
		return gameProperties.get(key);
	}
	
	static {
		gameProperties = new HashMap<>();
	}
}
