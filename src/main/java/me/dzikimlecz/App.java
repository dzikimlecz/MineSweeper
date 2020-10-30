package me.dzikimlecz;


import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.setup.LauncherFrame;

import javax.swing.SwingUtilities;

public class App {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(LauncherFrame::new);
	}
}
