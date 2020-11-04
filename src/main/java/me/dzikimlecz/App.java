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

// TODO: 03.11.2020 Migrate from Swing to JavaFX. ASAP.
// TODO: 04.11.2020 separate model from view. Increase role of GameManager controller.

/**
 * Main Class, represents set up frame and starts game.
 * @see MineSweeper
 */
public class App extends JFrame {
	
	private Difficulty difficulty;
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	/** Main method of the program
	 * Starts the whole game.
	 * @param ignored start arguments, not used in program
	 */
	public static void main(String[] ignored) {
		SwingUtilities.invokeLater(App::new);
	}
	
	
	/** Default and only constructor of the App.
	 *
	 * Instantiates new JFrame, used to select configurations of the game.
	 */
	public App() {
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
		//fill all possible values from difficulty enum
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
			// Suppressing warning of possible null reference, selected item was set at line 45.
			@SuppressWarnings({"ConstantConditions"})
			Difficulty difficulty = Difficulty.parseDifficulty(
					difficultyBox.getSelectedItem().toString());
			setDifficulty(difficulty);
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
	@Override
	public void dispose() {
		SwingUtilities.invokeLater(() -> new MineSweeper(difficulty));
		super.dispose();
	}
}
