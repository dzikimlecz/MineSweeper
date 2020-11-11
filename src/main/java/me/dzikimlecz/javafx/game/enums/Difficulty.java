package me.dzikimlecz.javafx.game.enums;

/**
 * Enum representing difficulty of the game.
 */
public enum Difficulty implements Parsable {
	/*DEBUG,*/ EASY, MEDIUM, HARD, EXTREME;
	
	/**
	 * Converts readable string to enums value.
	 * @param value readable form of enums.
	 * @return difficulty value of value parameter
	 */
	public static Difficulty parseDifficulty(String value) {
		return Difficulty.valueOf(value.replaceAll("\\s", "_").toUpperCase());
	}
	
	/**
	 * returns mines factor depending on enums value
	 * @return float being a percentage of cells to be bombed.
	 */
	public float getMinesFactor() {
		switch (this) {
			/*case DEBUG:
				return 0.07f;*/
			case EASY:
				return 0.15f;
			case MEDIUM:
				return 0.2f;
			case HARD:
				return 0.3f;
			case EXTREME:
				return 0.4f;
			default:
				throw new IllegalStateException("Unexpected value: " + this);
		}
	}
}
