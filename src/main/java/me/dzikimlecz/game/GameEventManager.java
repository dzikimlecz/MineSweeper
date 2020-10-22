package me.dzikimlecz.game;

public class GameEventManager {
	private static GameEventManager instance;
	private MineSweeper game;
	
	protected MineSweeper getGame() {
		return game;
	}
	
	protected  void registerGame(MineSweeper game) {
		this.game = game;
	}
	
	protected static GameEventManager getInstance() {
		if (instance == null) {
			instance = new GameEventManager();
		}
		return instance;
	}
	
	
	
	
}
