package me.dzikimlecz.game.enums;

/**
 * Enum representing difficulty of the game.
 */
public enum Difficulty implements Parsable {
	/*DEBUG,*/ EASY, MEDIUM, HARD, EXTREME;
	
	/**
	 * Converts readable string to enum value.
	 * @param string readable form of enum.
	 * @return difficulty value of string parameter
	 */
	public static Difficulty parseDifficulty(String string) {
		return Difficulty.valueOf(string.replaceAll("\\s", "_").toUpperCase());
	}
	
	/**
	 * returns mines factor depending on enum value
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
