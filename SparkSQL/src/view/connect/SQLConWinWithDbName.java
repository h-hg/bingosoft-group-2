package view.connect;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public abstract class SQLConWinWithDbName extends SQLConWin {
	public Label initialDbNameLabel = null;
	public TextField initialDbNameInput = null;

	public SQLConWinWithDbName(String name) {
		super(name);
		initialDbNameLabel = new Label("Initial Database:");
		inputBar.add(initialDbNameLabel, 0, 5);
		initialDbNameInput = new TextField();
		inputBar.add(initialDbNameInput, 1, 5);
	}
}
