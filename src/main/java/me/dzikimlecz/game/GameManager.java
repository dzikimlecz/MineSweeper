package me.dzikimlecz.game;

public class GameManager {
	private static GameManager instance;
	private MineSweeper game;
	
	protected MineSweeper getGame() {
		return game;
	}
	
	protected  void registerGame(MineSweeper game) {
		this.game = game;
	}
	
	protected static void clear() {
		instance = null;
	}
	
	protected static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	
	
	
}
