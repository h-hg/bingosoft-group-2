package application;

import controller.Controller;
import controller.Manager;
import controller.StartedControl;
import javafx.application.Application;
import javafx.stage.Stage;
import view.StartedWin;

public class Main extends Application {
	private Controller controller = null;

	public void start(Stage primaryStage) {
		StartedWin startedWin = new StartedWin();
		StartedControl startedControl = new StartedControl(startedWin);
		this.controller = startedControl;
		
		Manager.name2Controller.put(startedWin.name, startedControl);
	    primaryStage.setScene(startedWin.scene);
	    primaryStage.setTitle("Group 2");
	    primaryStage.show();
	    
	}
	@Override
    public void stop() throws Exception {
		//release the resource
		this.controller.close();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
