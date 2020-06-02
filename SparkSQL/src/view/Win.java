package view;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Win {
	public Stage stage = null;
	public Scene scene = null;
	public String name = null;
	Win(String winName) {
		name = winName;
	}
	public void show() {
		if(stage == null)
			stage = new Stage();
	    stage.setScene(scene);
	    stage.show();
	}
	public void close() {
		if(stage == null)
			return;
		stage.close();
	}
}
