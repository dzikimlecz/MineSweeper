package me.dzikimlecz.game.enums;

/**
 * Enum representing mode of the cells' buttons.
 */
public enum ToggleMode implements Parsable {
	/**
	 * uncovers a cell and possibly end the game.
	 */
	DIG,
	/**
	 * marks a cell as mined.
	 */
	MARK
}
