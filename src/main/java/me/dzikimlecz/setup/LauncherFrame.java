package me.dzikimlecz.setup;

import me.dzikimlecz.game.enums.Difficulty;
import me.dzikimlecz.game.MineSweeper;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class LauncherFrame extends JFrame {
	
	public LauncherFrame() {
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
			String selected = (String) difficultyBox.getSelectedItem();
			if (selected == null) showDialogBox();
			else {
				Difficulty difficulty = Difficulty.valueOf(selected.toUpperCase());
				SwingUtilities.invokeLater(() -> new MineSweeper(difficulty));
				dispose();
			}
		});
		proceedPanel.setBorder(BorderFactory.createEmptyBorder(30,130,10,20));
		proceedPanel.add(start, BorderLayout.CENTER);
		
		mainPanel.add(difficultyPanel);
		mainPanel.add(new JPanel());
		mainPanel.add(new JPanel());
		mainPanel.add(proceedPanel);
		
		return mainPanel;
	}
	
	private void showDialogBox() {
		JDialog dialogBox = new JDialog(this, "MineSweeper", true);
		dialogBox.setSize(250,150);
		dialogBox.setResizable(false);
		dialogBox.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dialogBox.setLayout(new BorderLayout());
		dialogBox.setLocationRelativeTo(this);
		
		JPanel upperPanel = new JPanel(new BorderLayout());
		upperPanel.add(new JLabel("Please select difficulty!"),
		               BorderLayout.CENTER);
		upperPanel.setBorder(BorderFactory.createEmptyBorder(30,30,0,0));
		
		JPanel lowerPanel = new JPanel(new BorderLayout());
		JButton okBut = new JButton("Ok");
		okBut.addActionListener(e -> dialogBox.dispose());
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(30,150,10,20));
		lowerPanel.add(okBut);
		
		dialogBox.add(upperPanel, BorderLayout.NORTH);
		dialogBox.add(lowerPanel, BorderLayout.SOUTH);
		
		dialogBox.setVisible(true);
	}
}
