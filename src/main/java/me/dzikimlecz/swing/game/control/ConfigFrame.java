package me.dzikimlecz.swing.game.control;

import me.dzikimlecz.swing.App;
import me.dzikimlecz.javafx.game.enums.Difficulty;
import me.dzikimlecz.javafx.game.enums.Theme;

import javax.swing.*;
import java.awt.*;

public class ConfigFrame extends JFrame {
	private Difficulty difficulty;
	private Theme theme;
	
	/** Default and only constructor of the App.
	 *
	 * Instantiates new JFrame, used to select configurations of the game.
	 */
	public ConfigFrame() {
		super("Game Launcher");
		this.setSize(480, 200);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		JPanel mainPanel = initSetUpPanel(new JPanel());
		this.add(mainPanel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	
	/**Initialises the main panel of the set up frame.
	 * @return a complete main panel.
	 */
	private JPanel initSetUpPanel(JPanel panel) {
		panel.setLayout(new GridLayout(2, 2));
		panel.setBorder(BorderFactory.createTitledBorder("MineSweeper"));
		
		//JPanel containing components used to select difficulty.
		JPanel difficultyPanel = new JPanel(new BorderLayout());
		difficultyPanel.add(new JLabel("Difficulty:"), BorderLayout.CENTER);
		
		//JComboBox used to select difficulty.
		JComboBox<String> difficultyBox = new JComboBox<>();
		difficultyBox.setEditable(false);
		
		//fill all possible values from difficulty enums
		for (Difficulty value : Difficulty.values())
			difficultyBox.addItem(value.parseString());
		
		//prevents NullPointerException
		difficultyBox.setSelectedIndex(0);
		difficultyPanel.add(difficultyBox, BorderLayout.SOUTH);
		
		//panel containing components used to start an actual game.
		JPanel proceedPanel = new JPanel(new BorderLayout());
		
		//button starting the game
		JButton start = new JButton("Start");
		start.addActionListener(event -> {
			//get Difficulty object from difficultyBox (JComboBox)
			//Suppressing warning of possible null reference, selected item was set at line 45.
			@SuppressWarnings({"ConstantConditions"})
			Difficulty difficulty =
					Difficulty.parseDifficulty(difficultyBox.getSelectedItem().toString());
			this.difficulty = difficulty;
			dispose();
		});
		proceedPanel.setBorder(BorderFactory.createEmptyBorder(30,130,10,20));
		proceedPanel.add(start, BorderLayout.CENTER);
		
		panel.add(difficultyPanel);
		panel.add(new JPanel());
		panel.add(new JPanel());
		panel.add(proceedPanel);
		
		return panel;
	}
	
	/**
	 * Disposes set up frame and starts the game.
	 * */
	public void dispose() {
		App.setDifficulty(difficulty);
		super.dispose();
	}
}
