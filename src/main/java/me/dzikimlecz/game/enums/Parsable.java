package me.dzikimlecz.game.enums;

public interface Parsable {
	String name();
	default String parse() {
		String name = name();
		return name.charAt(0) +
				name.toLowerCase().substring(1).replaceAll("_", " ");
	}
}
