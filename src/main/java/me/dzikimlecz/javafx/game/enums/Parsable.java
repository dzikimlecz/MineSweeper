package me.dzikimlecz.javafx.game.enums;

/**
 * Interface used to parse an enum value to readable string.
 */
public interface Parsable {
	String name();
	default String parseString() {
		String name = name();
		return name.charAt(0) +
				name.toLowerCase().substring(1).replaceAll("_", " ");
	}
}
