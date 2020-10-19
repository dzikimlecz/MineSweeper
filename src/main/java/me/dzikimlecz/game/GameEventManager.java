package me.dzikimlecz.game;

public class GameEventManager {
	private static GameEventManager instance;
	private MineSweeper game;
	
	public MineSweeper getGame() {
		return game;
	}
	
	public  void registerGame(MineSweeper game) {
		this.game = game;
	}
	
	public static GameEventManager getInstance() {
		if (instance == null) {
			instance = new GameEventManager();
		}
		return instance;
	}
	
	
	
	
}
