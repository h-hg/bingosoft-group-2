package controller;

import java.util.HashMap;
import java.util.Map;

import controller.connect.SQLConControl;
import controller.connect.SparkSQLConControl;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.MapValueFactory;
import model.DatabaseTableInfo;
import model.SQLResult;
import model.SQLResultTable;
import model.sql.connect.DatabaseInfo;
import model.sql.connect.SQLConConfig;
import model.sql.connect.SQLConInfo;
import model.sql.connect.SparkSQLConConfig;
import view.StartedWin;
import view.connect.SQLConWin;
import view.connect.SparkSQLConWin;

public class StartedControl extends Controller {
	private StartedWin view = null;
	private Map<String, SQLConInfo> conName2SQLConInfo = new HashMap<>();
	private String currentConName = null; //Suppose there is only one active sql-link.
	
	public StartedControl(StartedWin mainWin) {
		view = mainWin;
		initMenu();
		initControlBar();
		initNavBar();
	}

	private void initMenu() {
		view.newSparkSQLConMenuItem.setOnAction(actionEvent -> {
			SQLConWin conWin = null;
			SQLConControl conControl = (SQLConControl) Manager.name2Controller.get("SparkConWin");
			if (conWin == null) {
				conWin = new SparkSQLConWin();
				conControl = new SparkSQLConControl((SparkSQLConWin)conWin);
				Manager.name2Controller.put(conWin.name, conControl);
			}
			conControl.show();
		});
		view.exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	}

	private void initControlBar() {
		view.runBtn.setOnAction(actionEvent -> {
			System.out.println(view.treeView.getSelectionModel().getSelectedItem().getValue());
		});
	}

	private void initNavBar() {
		//listen clicking even: https://stackoverflow.com/questions/27894108/how-do-i-make-a-mouse-click-event-be-acknowledged-by-a-treeitem-in-a-treeview

		//listen TreeItem changing event: https://stackoverflow.com/questions/15792090/javafx-treeview-item-action-event
		view.treeView.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
			TreeItem<String> treeItem = newValue;
			int level = view.treeView.getTreeItemLevel(treeItem);
			String conName = null, dbName = null;
			if(level == 1) { //coName
				conName = treeItem.getValue();
			} else if(level == 2) { //coName
				dbName = treeItem.getValue();
				conName = treeItem.getParent().getValue();
			} else if(level == 3) { // table
				dbName = treeItem.getParent().getValue();
				conName = treeItem.getParent().getParent().getValue();
			}
			if(conName == null || dbName == null)
				return;
			if(currentConName == conName) {
				
			} else {
				
			}
			System.out.format("connect name: %s, database name: %s\n", conName, dbName);
		});
	}

	public void addNewCon(SQLConConfig config) {
		if(conName2SQLConInfo.get(config.name) != null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			//alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("Ooops, there was an connection named " + config.name + "!");
			alert.showAndWait();
			return;
		}
		currentConName = config.name;
		if(config instanceof SparkSQLConConfig) {
			addSparkSQLCon((SparkSQLConConfig)config);
		}
	}
	public void addSparkSQLCon(SparkSQLConConfig config) {		
		//The omitted codes are roughly as follows
		//SQLConInfo info = new SQLConInfo();
		//do something with info
		//conName2SQLConInfo.put(currentConName, info);
		//addSQLConInfo(info);
		System.out.println(config.url + " " + config.port + " " + config.user + " " + config.password);
	}
	public void addSQLConInfo(SQLConInfo info) {
		// connection name
		TreeItem<String> root = new TreeItem<String>(info.conConfig.name);
		for (DatabaseInfo dbInfo : info.dbs) {
			// database name
			TreeItem<String> dbRoot = new TreeItem<String>(dbInfo.name);
			for (DatabaseTableInfo tbInfo : dbInfo.tables) {
				dbRoot.getChildren().add(new TreeItem<String>(tbInfo.name));
			}
			root.getChildren().add(dbRoot);
		}
		view.treeRoot.getChildren().add(root);
	}

	public Tab createSQLResultTableTab(SQLResultTable table) {
		TableView<Map<String, String>> tableView = new TableView<>();
		// add column, see https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		for (String columnName : table.columns) {
			TableColumn<Map<String, String>, String> column = new TableColumn<>(columnName);
			column.setCellValueFactory(new MapValueFactory(columnName));
			tableView.getColumns().add(column);
		}
		// add data
		tableView.setItems(table.content);
		Tab tab = new Tab(table.name);
		tab.setContent(tableView);
		return tab;
	}

	public void addSQLResult(SQLResult res) {
		// set sql info
		view.sqlInfoOutput.setText(res.info);
		// set tabs
		view.sqlResTabPane.getTabs().clear();
		view.sqlResTabPane.getTabs().add(view.sqlInfoTab);
		for (SQLResultTable table : res.tables) {
			view.sqlResTabPane.getTabs().add(createSQLResultTableTab(table));
		}
	}

	public void test() {
		view.treeRoot.isLeaf();
		// view.treeRoot.getParent() != null;
		System.out.println(view.treeView.getTreeItemLevel(view.treeRoot));
	}

	public void show() {
		view.show();
	}

	public void close() {
		view.close();
	}
}
