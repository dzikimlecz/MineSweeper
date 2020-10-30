package me.dzikimlecz;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.MineSweeper;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class App extends JFrame {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(App::new);
	}
	
	public App() {
		super("Game Launcher");
		this.setSize(480, 200);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		JPanel mainPanel = initMainPanel();
		this.add(mainPanel, BorderLayout.CENTER);
		setVisible(true);
	}
	
	private JPanel initMainPanel() {
		JPanel mainPanel = new JPanel(new GridLayout(2, 2));
		mainPanel.setBorder(BorderFactory.createTitledBorder("MineSweeper"));
		
		JPanel difficultyPanel = new JPanel(new BorderLayout());
		difficultyPanel.add(new JLabel("Difficulty:"), BorderLayout.CENTER);
		
		JComboBox<String> difficultyBox = new JComboBox<>();
		difficultyBox.setEditable(false);
		for (Difficulty value : Difficulty.values())
			difficultyBox.addItem(value.parse());
		difficultyBox.setSelectedIndex(0);
		difficultyPanel.add(difficultyBox, BorderLayout.SOUTH);
		
		JPanel proceedPanel = new JPanel(new BorderLayout());
		JButton start = new JButton("Start");
		start.addActionListener(e -> {
			@SuppressWarnings({"ConstantConditions"})
			String selected = ((String) difficultyBox.getSelectedItem()).toUpperCase();
			Difficulty difficulty = Difficulty.valueOf(selected.toUpperCase());
			SwingUtilities.invokeLater(() -> new MineSweeper(difficulty));dispose();
			
		});
		proceedPanel.setBorder(BorderFactory.createEmptyBorder(30,130,10,20));
		proceedPanel.add(start, BorderLayout.CENTER);
		
		mainPanel.add(difficultyPanel);
		mainPanel.add(new JPanel());
		mainPanel.add(new JPanel());
		mainPanel.add(proceedPanel);
		
		return mainPanel;
	}
	
	
	
}
