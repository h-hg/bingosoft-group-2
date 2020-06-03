package controller;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import model.DatabaseInfo;
import model.DatabaseTableInfo;
import model.SQLConConfig;
import model.SQLConInfo;
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
				//Manager.name2Win.put(conWin.name, conWin);
				Manager.name2Controller.put(conWin.name, conControl);
			}
			conControl.show();
		});
		view.exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	}
	public void addNewCon(SQLConConfig config) {
		System.out.println(config.url + " " + config.port + " " + config.user + " " + config.password);
	}
	public void addSQLConInfo(SQLConInfo info) {
		//connection name
		TreeItem<String> root = new TreeItem<String>(info.conConfig.name);
		for(DatabaseInfo dbInfo : info.dbs) {
			TreeItem<String> dbRoot = new TreeItem<String>(dbInfo.name);
			for(DatabaseTableInfo tbInfo : dbInfo.tables) {
				dbRoot.getChildren().add(new TreeItem<String>(tbInfo.name));
			}
			root.getChildren().add(dbRoot);
		}
		view.treeRoot.getChildren().add(root);
	}
	public void show() {
		view.show();
	}
	public void close() {
		view.close();
	}
}
