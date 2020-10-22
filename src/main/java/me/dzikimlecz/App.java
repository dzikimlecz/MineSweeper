package me.dzikimlecz;


import me.dzikimlecz.game.Difficulty;
import me.dzikimlecz.game.MineSweeper;

import javax.swing.*;

public class App {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MineSweeper(Difficulty.EXTREME));
	}
}
