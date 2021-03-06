package controller.connect;

import model.sql.connect.SparkSQLConConfig;
import view.connect.SparkSQLConWin;

public class SparkSQLConControl extends SQLConControl{
	public SparkSQLConWin view = null;
	public SparkSQLConControl(SparkSQLConWin view) {
		super(view);
		this.view = view;
		initControlBar();
	}
	public SparkSQLConConfig getSQLConConfig() {
		return new SparkSQLConConfig(view.conNameInput.getText(), view.urlInput.getText(), view.portInput.getText(), view.userInput.getText(), view.pwInput.getText(), view.initialDbNameInput.getText());
	}
}
