package me.dzikimlecz;

import javafx.application.Application;

public class App {
	static Integer difficulty = null;
	
	public static void main(String[] args) {
		Application.launch(ConfigsWindow.class);
		while (difficulty == null) {
			try {
				Thread.sleep(250);
			} catch (Exception ignore) {}
		}
		GameManager.init(difficulty);
		Application.launch(MineSweeper.class);
	}
}
