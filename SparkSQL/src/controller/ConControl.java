package controller;


import model.SparkSQLConConfig;
import view.ConWin;

public class ConControl extends Controller {
	private ConWin view = null;
	public void initBtn() {
		view.cancelBtn.setOnAction(actionEvent -> view.close());
		view.conBtn.setOnAction(actionEvent -> {
			StartedControl startedControl = (StartedControl)Manager.name2Controller.get("StartedWin");
			startedControl.addNewCon(new SparkSQLConConfig(view.conNameInput.getText(), view.urlInput.getText(), view.portInput.getText(), view.userInput.getText(), view.pwInput.getText()));
			close();
		});
	}
	public ConControl(ConWin conWin) {
		view = conWin;
		initBtn();
	}
	public void show() {
		view.show();
		view.stage.setResizable(false);
	}
	public void close() {
		view.close();
	}
}
