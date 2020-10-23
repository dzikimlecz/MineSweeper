package me.dzikimlecz.game;

public enum Difficulty {
	EASY, MEDIUM, HARD, EXTREME;
	
	@Override
	public String toString() {
		StringBuilder nameBuffer = new StringBuilder(this.name().toLowerCase());
		nameBuffer.setCharAt(0, Character.toUpperCase(nameBuffer.charAt(0)));
		return nameBuffer.toString();
	}
}
