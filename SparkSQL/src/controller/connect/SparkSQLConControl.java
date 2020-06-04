package controller.connect;

import controller.Manager;
import controller.StartedControl;
import model.sql.connect.SparkSQLConConfig;
import view.connect.SparkSQLConWin;

public class SparkSQLConControl extends SQLConControl{
	public SparkSQLConWin view = null;
	public SparkSQLConControl(SparkSQLConWin view) {
		super(view);
		this.view = view;
		initControlBar();
	}
	public void initControlBar()  {
		view.cancelBtn.setOnAction(actionEvent -> view.close());
		view.conBtn.setOnAction(actionEvent -> {
			StartedControl startedControl = (StartedControl)Manager.name2Controller.get("StartedWin");
			close();
			startedControl.addNewCon(new SparkSQLConConfig(view.conNameInput.getText(), view.urlInput.getText(), view.portInput.getText(), view.userInput.getText(), view.pwInput.getText(), view.initialDbNameInput.getText()));
		});
	}
}
