package me.dzikimlecz.javafx.game.enums;

//not used yet.
/**
 * Enum representing theme of the game.
 */
public enum Theme implements Parsable {
	LIGHT, DARK, YELLOW, YELLOW_DARK, PINK, PINK_DARK;
	
	public static Theme parseTheme(String value) {
		return Theme.valueOf(value.replaceAll("\\s", "_").toUpperCase());
	}
}
