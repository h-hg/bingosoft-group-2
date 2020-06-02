package application;

import controller.Manager;
import controller.StartedControl;
import javafx.application.Application;
import javafx.stage.Stage;
import view.StartedWin;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		StartedWin startedWin = new StartedWin();
		StartedControl startedControl = new StartedControl(startedWin);
		
		Manager.name2Win.put(startedWin.name, startedWin);
		Manager.name2Controller.put(startedWin.name, startedControl);
		
	    primaryStage.setScene(startedWin.scene);
	    primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
