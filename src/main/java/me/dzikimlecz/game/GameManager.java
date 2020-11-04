package me.dzikimlecz.game;

/**
 * Controller connecting parts of the game
 */
public class GameManager {
	/**
	 * Prevents from creating duplicate objects
	 */
	private GameManager() {}
	/**
	 * Instance of class allows to use one object instead of creating many duplicates
	 */
	private static GameManager instance;
	
	/**
	 * registered game instance.
	 */
	private MineSweeper game;
	
	/**
	 * Getter of registered game
	 * @return current registered game in manager instance
	 */
	protected MineSweeper getGame() {
		return game;
	}
	
	/**
	 * Registers a game
	 * @param game {@code MineSweeper} instance to be registered as a game.
	 */
	protected  void registerGame(MineSweeper game) {
		this.game = game;
	}
	
	/**
	 * Clears current manager instance.
	 */
	protected static void clear() {
		instance = null;
	}
	
	/**
	 * Gets or gets creates a manager instance depending on its reference
	 * @return new {@code GameManager} instance if {@code instance == null} or otherwise, an
	 * existing object.
	 */
	protected static GameManager getInstance() {
		if (instance == null) instance = new GameManager();
		return instance;
	}
}
