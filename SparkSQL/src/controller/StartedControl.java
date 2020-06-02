package controller;

import view.ConWin;
import view.StartedWin;

public class StartedControl extends Controller {
	private StartedWin view = null;
	
	public StartedControl(StartedWin mainWin) {
		view = mainWin;
	}
	
	private void initMenu() {
		view.newConMenuItem.setOnAction(actionEvent -> {
			ConWin conWin = (ConWin)Manager.name2Win.get("ConWin");
			ConControl conControl = null;
			if(conWin == null) {
				conWin = new ConWin();
				conControl = new ConControl(conWin);
				Manager.name2Win.put(conWin.name, conWin);
				Manager.name2Controller.put(conWin.name, conControl);
			}
			conWin.show();
		});
		view.exitMenuItem.setOnAction(actionEvent -> view.close());

	}
}
