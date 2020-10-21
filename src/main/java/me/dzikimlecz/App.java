package me.dzikimlecz;


import me.dzikimlecz.game.Difficulty;
import me.dzikimlecz.game.MineSweeper;

import javax.swing.*;

public class App {
	
	public static void main(String[] args) {
		/* FIXME: 20.10.2020 Anomalies on non-extreme modes. Uncovering many non-related cells,
		*   wrong marks of near bombs. */
		// TODO: 20.10.2020 GameFrame::processNearbyCells
		// TODO: 20.10.2020 GameFrame::new
		SwingUtilities.invokeLater(() -> new MineSweeper(Difficulty.EXTREME));
	}
}
