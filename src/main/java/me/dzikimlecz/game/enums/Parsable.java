package me.dzikimlecz.game.enums;

/**
 * Interface used to parse an enum value to readable string.
 */
public interface Parsable {
	String name();
	default String parseString() {
		String name = name();
		StringBuilder nameBuilder = new StringBuilder();
		
		while (name.contains("_")) {
			nameBuilder.append(name.charAt(0))
					.append(name.toLowerCase(), 1, name.indexOf('_'))
					.append(' ');
			name = name.substring(name.indexOf('_') + 1);
		}
		nameBuilder.append(name.charAt(0))
				.append(name.toLowerCase(), 1, name.length());
		
		return nameBuilder.toString();
	}
}
