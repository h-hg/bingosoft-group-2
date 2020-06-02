package controller;

import javafx.application.Platform;
import model.SparkSQLConConfig;
import view.ConWin;
import view.StartedWin;

public class StartedControl extends Controller {
	private StartedWin view = null;
	
	public StartedControl(StartedWin mainWin) {
		view = mainWin;
		initMenu();
	}
	
	private void initMenu() {
		view.newConMenuItem.setOnAction(actionEvent -> {
			ConWin conWin = null;
			ConControl conControl = (ConControl)Manager.name2Controller.get("ConWin");
			if(conWin == null) {
				conWin = new ConWin();
				conControl = new ConControl(conWin);
				Manager.name2Win.put(conWin.name, conWin);
				Manager.name2Controller.put(conWin.name, conControl);
			}
			conControl.show();
		});
		view.exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	}
	public void addNewCon(SparkSQLConConfig config) {
		System.out.println(config.url + " " + config.port + " " + config.user + " " + config.password);
	}
	public void show() {
		view.show();
	}
	public void close() {
		view.close();
	}
}
