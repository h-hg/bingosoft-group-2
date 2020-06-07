package controller.connect;

import model.sql.connect.MySQLConConfig;
import view.connect.MySQLConWin;

public class MySQLConControl extends SQLConControl{
	public MySQLConWin view = null;
	public MySQLConControl(MySQLConWin view) {
		super(view);
		this.view = view;
		initControlBar();
	}
	public MySQLConConfig getSQLConConfig() {
		return new MySQLConConfig(view.conNameInput.getText(), view.urlInput.getText(), view.portInput.getText(), view.userInput.getText(), view.pwInput.getText(), view.initialDbNameInput.getText());
	}
}
