package controller.connect;

import controller.Controller;
import view.connect.SQLConWin;

public abstract class SQLConControl extends Controller {
	private SQLConWin view = null;
	
	public abstract void initControlBar();
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
