package view.connect;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SparkSQLConWin extends SQLConWin{
	public Label initialDbNameLabel = null;
	public TextField initialDbNameInput = null;
	public SparkSQLConWin() {
		super("SQLConControl");
		initialDbNameLabel = new Label("Initial Database:");
		inputBar.add(initialDbNameLabel, 0, 5);
		initialDbNameInput = new TextField();
		inputBar.add(initialDbNameInput, 1, 5);
		
		//for test
		//xxx
	}
	public void show() {
		if(stage == null) {
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("SparkSQL Configuration");
		}
	    stage.show();
	}
}
