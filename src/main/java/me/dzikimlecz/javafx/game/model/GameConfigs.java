package me.dzikimlecz.javafx.game.model;

import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GameConfigs {
	private final GameProperties gameProperties;
	public static final List<String> reservedKeys = Arrays.asList("difficulty", "theme");
	
	public GameConfigs() {
		this.gameProperties = new GameProperties();
	}
	
	public String getCSS() {
		Object theme = get("theme");
		return CSSLoader.load((Theme) theme);
	}
	
	public void register(@NotNull String key, @NotNull Object value) {
		if (!checkRegistry(key, value)) {
			String msg = String.format(
					"\"%s\" key must correspond to instance of %s value. (Passed: %s)",
					key, key.toUpperCase().charAt(0) + key.substring(1),
					value.getClass().toString()
			);
			throw new IllegalArgumentException(msg);
		}
		gameProperties.register(key, value);
	}
	
	private boolean checkRegistry(String key, Object value) {
		if(!reservedKeys.contains(key)) return true;
		String valClassName = value.getClass().toString();
		valClassName = valClassName.substring(valClassName.lastIndexOf('.')+1).toLowerCase();
		return key.equals(valClassName);
	}
	
	public Object get(@NotNull String key) {
		Object value = gameProperties.get(key.toLowerCase());
		if (value == null) throw new IllegalStateException(String.format(
				"No value corresponding to \"%s\" key", key
		));
		return value;
	}
	
	public Difficulty getDifficulty() {
		return (Difficulty) get("difficulty");
	}
	
	public boolean isEmpty() {
		return gameProperties.isEmpty();
	}
	
	private static class CSSLoader {
		static String load(Theme theme) {
			return "styles/themes/" + theme.parseString().replaceAll("\\s", "-") + ".css";
		}
	}
	
}
