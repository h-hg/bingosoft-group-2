package controller;

import java.util.HashMap;
import java.util.Map;
import controller.connect.MySQLConControl;
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
import model.sql.SQLType;
import model.sql.connect.MySQLConConfig;
import model.sql.connect.SQLConConfig;
import model.sql.connect.SparkSQLConConfig;
import model.sql.query.DatabaseOutline;
import model.sql.query.SQLConOutline;
import model.sql.query.SQLResult;
import model.sql.query.SQLResultTable;
import model.sql.query.TableOutline;
import model.sql.services.MySQLService;
import model.sql.services.SQLService;
import model.sql.services.SparkSQLService;
import view.StartedWin;
import view.connect.MySQLConWin;
import view.connect.SQLConWin;
import view.connect.SparkSQLConWin;

public class StartedControl extends Controller {
	private StartedWin view = null;
	private Map<String, SQLConConfig> conName2SQLConInfo = new HashMap<>();
	private String currentConName = null; // Suppose there is only one active sql-link.
	private SQLService conService = null;

	public StartedControl(StartedWin mainWin) {
		view = mainWin;
		initMenu();
		initControlBar();
		initNavBar();
	}

	public void setStatusBarText(String text) {
		view.statusBar.setText(text);
	}

	public void removeCon(String conName) {
		if (conName == null || conName2SQLConInfo.get(conName) == null)
			return;
		conName2SQLConInfo.remove(conName);
		if (currentConName.equals(conName)) {
			currentConName = null;
			conService.close();
			conService = null;
		}
		view.conChoiceBox.getItems().remove(conName);
		for(TreeItem<String> item : view.treeRoot.getChildren()) {
			if(item.getValue().equals(conName)) {
				view.treeRoot.getChildren().remove(item);
				break;
			}		
		}
	}
	protected void lanuchConWin(String winName, SQLType type) {
		SQLConWin conWin = null;
		SQLConControl conControl = (SQLConControl) Manager.name2Controller.get(winName);
		if (conWin == null) {
			switch (type) {
			case MYSQL:
				conWin = new MySQLConWin();
				conControl = new MySQLConControl((MySQLConWin) conWin); 
				break;
			case SPARK:
				conWin = new SparkSQLConWin();
				conControl = new SparkSQLConControl((SparkSQLConWin) conWin);				
				break;
			default:
				break;
			}
			Manager.name2Controller.put(conWin.name, conControl);
		}
		conControl.show();
	}
	private void initMenu() {
		view.newSparkSQLConMenuItem.setOnAction(actionEvent -> {
			lanuchConWin("SparkSQLConWin", SQLType.SPARK);
		});
		view.newMySQLConMenuItem.setOnAction(actionEvent -> {
			lanuchConWin("MySQLConWin", SQLType.MYSQL);
		});
		view.exitMenuItem.setOnAction(actionEvent -> Platform.exit());
	}

	private void initControlBar() {
		view.runBtn.setOnAction(actionEvent -> {
			if (conService == null) {
				return;
			}
			addSQLResult(conService.getResult(view.sqlTextInput.getText()));
		});
		view.conBtn.setOnAction(actionEvent -> {
			if (view.conChoiceBox.getSelectionModel().getSelectedIndex() == -1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Connetion Error");
				alert.setContentText("Ooops, Choose at least one connection!");
				alert.showAndWait();
				return;
			}
			String conName = view.conChoiceBox.getValue();
			changeCurrentCon(conName2SQLConInfo.get(conName));
		});
		view.refreshBtn.setOnAction(actionEvent -> {
			// clear
			view.treeRoot.getChildren().clear();
			// add
			for (Map.Entry<String, SQLConConfig> item : conName2SQLConInfo.entrySet()) {
				SQLService service = getSQLService(item.getValue());
				service.getService();
				addSQLConInfo(service.getSQLConInfo());
				service.close();
			}
		});
		view.rmBtn.setOnAction(actionEvent -> {
			if(view.conChoiceBox.getSelectionModel().getSelectedIndex() == -1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Remove Error");
				alert.setContentText("Ooops, Choose at least one connection!");
				alert.showAndWait();
				return;
			}
			removeCon(view.conChoiceBox.getValue());
		});
	}

	private void initNavBar() {
		// listen clicking even:
		// https://stackoverflow.com/questions/27894108/how-do-i-make-a-mouse-click-event-be-acknowledged-by-a-treeitem-in-a-treeview

		// listen TreeItem changing event:
		// https://stackoverflow.com/questions/15792090/javafx-treeview-item-action-event
		view.treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			TreeItem<String> treeItem = newValue;
			int level = view.treeView.getTreeItemLevel(treeItem);
			String conName = null, dbName = null;
			if (level == 1) { // coName
				conName = treeItem.getValue();
			} else if (level == 2) { // coName
				dbName = treeItem.getValue();
				conName = treeItem.getParent().getValue();
			} else if (level == 3) { // table
				dbName = treeItem.getParent().getValue();
				conName = treeItem.getParent().getParent().getValue();
			}
			if (conName == null || dbName == null)
				return;
			if (currentConName == conName) {

			} else {

			}
			setStatusBarText(String.format("connect name: %s, database name: %s", conName, dbName));
		});
	}

	SQLService getSQLService(SQLConConfig config) {
		switch (config.type) {
		case MYSQL:
			return new MySQLService((MySQLConConfig)config);
		case SPARK:
			return new SparkSQLService((SparkSQLConConfig) config);
		default:
			return null;
		}
	}

	boolean changeCurrentCon(SQLConConfig config) {
		// Suppose there is only one active sql-link.
		if (config.name.equals(currentConName))
			return true;
		if (conService != null) {
			conService.close();
		}
		conService = getSQLService(config);
		if (conService.getService() == false)
			return false;
		currentConName = config.name;
		return true;
	}

	public void addNewCon(SQLConConfig config) {
		// judge whether there is a connection name config.name now.
		if (conName2SQLConInfo.get(config.name) != null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("New Connection Error");
			alert.setContentText("Ooops, there was an connection named" + config.name + "!");
			alert.showAndWait();
			return;
		}
		if (changeCurrentCon(config) == false) {
			return;
		}
		view.conChoiceBox.getItems().add(config.name);
		conName2SQLConInfo.put(currentConName, config);
		addSQLConInfo(conService.getSQLConInfo());
	}

	void addSQLConInfo(SQLConOutline info) {
		// connection name
		TreeItem<String> root = new TreeItem<String>(info.conConfig.name);
		for (DatabaseOutline dbInfo : info.dbs) {
			// database name
			TreeItem<String> dbRoot = new TreeItem<String>(dbInfo.name);
			for (TableOutline tbInfo : dbInfo.tables) {
				dbRoot.getChildren().add(new TreeItem<String>(tbInfo.name));
			}
			root.getChildren().add(dbRoot);
		}
		view.treeRoot.getChildren().add(root);
	}

	protected Tab createSQLResultTableTab(SQLResultTable table) {
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

	void addSQLResult(SQLResult res) {
		// set sql info
		view.sqlInfoOutput.setText(res.info);
		// set tabs
		view.sqlResTabPane.getTabs().clear();
		view.sqlResTabPane.getTabs().add(view.sqlInfoTab);
		for (SQLResultTable table : res.tables) {
			view.sqlResTabPane.getTabs().add(createSQLResultTableTab(table));
		}
	}

	public void show() {
		view.show();
	}

	public void close() {
		if (conService != null) {
			conService.close();
		}
		view.close();
	}
}
