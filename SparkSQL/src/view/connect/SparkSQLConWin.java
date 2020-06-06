package view.connect;

import javafx.stage.Stage;

public class SparkSQLConWin extends SQLConWinWithDbName {

	public SparkSQLConWin() {
		super("SQLConControl");

		// for test
		String conname = "bigdata";
		String sqlurl = "bigdata31.depts.bingosoft.net";
		String sqlport = "22231";
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
			stage.setTitle("SparkSQL Configuration");
		}
		stage.show();
	}
}
