package controller.connect;

import controller.Controller;
import controller.Manager;
import controller.StartedControl;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.sql.connect.SQLConConfig;
import view.connect.SQLConWin;

public abstract class SQLConControl extends Controller {
	private SQLConWin view = null;
	
	protected abstract SQLConConfig generateSQLConConfig();	
	public void initControlBar() {
		view.cancelBtn.setOnAction(actionEvent -> view.close());
		view.conBtn.setOnAction(actionEvent -> {
			String name = view.conNameInput.getText();
			if(name == null || name.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("New Connection Error");
				alert.setContentText("Connection name shouldn't be empty!");
				alert.showAndWait();
				return;
			}
			StartedControl startedControl = (StartedControl)Manager.name2Controller.get("StartedWin");
			close();
			startedControl.addNewCon(generateSQLConConfig());
		});
	}

	public SQLConControl(SQLConWin conWin) {
		view = conWin;
	}
	public void show() {
		view.show();
		view.stage.setResizable(false);
	}
	public void close() {
		view.close();
	}
}
