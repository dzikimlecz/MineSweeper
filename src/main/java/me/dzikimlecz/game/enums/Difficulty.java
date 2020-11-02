package me.dzikimlecz.game.enums;

public enum Difficulty implements Parsable {
	/*DEBUG,*/ EASY, MEDIUM, HARD, EXTREME;
	
	
	public float getBombsFactor() {
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
