package main;

import me.dzikimlecz.App;

import javafx.application.Application;

public class Main {
	public static void main(String[] ignored) {
		try {
			Application.launch(App.class);
		} catch (Exception e) {
			App.applicationError(e);
		}
	}
}
