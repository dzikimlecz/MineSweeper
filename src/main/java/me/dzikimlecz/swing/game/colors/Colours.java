package me.dzikimlecz.swing.game.colors;

import me.dzikimlecz.javafx.game.enums.Theme;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colours {
	
	private static Colours instance;
	
	public static Colours getInstance() {
		if (instance == null) {
			instance = new Colours();
		}
		return instance;
	}
	
	public static void registerTheme(Theme theme) {
		getInstance().setTheme(theme);
	}
	
	/**
	 * Map containing colour data.
	 */
	private Map<String, Color> colourMap = new HashMap<>();
	/*
	coveredCell,
	uncoveredCell,
	toggleButton,
	font,
	flag,
	border,
	background
	*/
	
	public void setTheme(Theme theme) {
	
	}
	
	
	/**
	 * Method getting right color for the theme
	 * @param key
	 * @return
	 */
	public Color get(String key) {
		return colourMap.get(key.toLowerCase());
	}
}
