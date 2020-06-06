package view.connect;

import javafx.stage.Stage;

public class MySQLConWin extends SQLConWinWithDbName {
	
	public MySQLConWin() {
		super("MySQLConWin");

		// for test
		String conname = "MySQL";
		String sqlurl = "bigdata28.depts.bingosoft.net";
		String sqlport = "23307";
		String sqlusername = "user07";
		String sqlpassword = "pass@bingo7";
		String sqldb = "user07_db";
		conNameInput.setText(conname);
		urlInput.setText(sqlurl);
		portInput.setText(sqlport);
		userInput.setText(sqlusername);
		pwInput.setText(sqlpassword);
		initialDbNameInput.setText(sqldb);
	}

	public void show() {
		if (stage == null) {
			stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("MySQL Configuration");
		}
		stage.show();
	}
}
