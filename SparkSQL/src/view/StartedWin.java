package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartedWin extends Win {
	
	public BorderPane root = null; 
	
	//menu bar
	public MenuBar menuBar = null;
	public Menu fileMenu = null;
	public MenuItem newConMenuItem = null;
	public MenuItem exitMenuItem = null;
	public Menu aboutMenu = null;
	public MenuItem helpMenuItem = null;
	public MenuItem aboutMenuItem = null;
	
	//nav part
	TreeView treeView = null;
	
	//main part
	VBox mainPane = null;
	//main part - control bar
	FlowPane controlBar = null;
	ChoiceBox conChoiceBox = null;
	ChoiceBox dbChoiceBox = null;
	Button runBtn = null;
	//main part - sql input part
	TextArea sqlTextInput = null;
	//main part - sql result part
	TabPane sqlResTabPane = null;
	Tab sqlInfoTab = null;
	TextArea sqlInfoOutput = null;
	
	Label statusBar = null;
	public void initRoot() {
		root = new BorderPane();
	}
	public void initMenuBar() {
	    menuBar = new MenuBar();
	    //menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

	    fileMenu = new Menu("File");
	    newConMenuItem = new MenuItem("New");
	    exitMenuItem = new MenuItem("Exit");
	    
	    fileMenu.getItems().addAll(newConMenuItem, new SeparatorMenuItem(), exitMenuItem);

	    aboutMenu = new Menu("About");
	    helpMenuItem = new MenuItem("Help");
	    aboutMenuItem = new MenuItem("About");
	    aboutMenu.getItems().addAll(helpMenuItem, new SeparatorMenuItem(), aboutMenuItem);

	    menuBar.getMenus().addAll(fileMenu, aboutMenu);
	    root.setTop(menuBar);
	}
	public void initNavPart() {
		treeView = new TreeView<>();
		root.setLeft(treeView);
	    //for test
		TreeItem treeItem = new TreeItem<>("root");
	    treeItem.setExpanded(true);

	    for(int i = 0;i < 5;i++){
	        TreeItem item = new TreeItem<>("node" + i);
	        treeItem.getChildren().add(item);
	    }
	    treeView.setRoot(treeItem);
	    
	    
	}
	public void initMainPart() {
		mainPane = new VBox();
		//control bar
		controlBar = new FlowPane();
		conChoiceBox = new ChoiceBox<>();
		dbChoiceBox = new ChoiceBox<>();
		runBtn = new Button("run");
		controlBar.getChildren().addAll(conChoiceBox, dbChoiceBox, runBtn);
		//sql input part
		sqlTextInput = new TextArea();
		//sql result part
		sqlResTabPane = new TabPane();
		sqlInfoTab = new Tab("Info");
		sqlInfoOutput = new TextArea();
		sqlInfoOutput.setEditable(false);
		sqlInfoTab.setContent(sqlInfoOutput);
		sqlResTabPane.getTabs().addAll(sqlInfoTab);
		
		mainPane.getChildren().addAll(controlBar, sqlTextInput, sqlResTabPane);
		root.setCenter(mainPane);
	}
	public void initStatusBar() {
		statusBar = new Label("status bar");
		root.setBottom(statusBar);
	}
	
	public StartedWin() {
		super("StartedWin");
		initRoot();
		initMenuBar();
		initNavPart();
		initMainPart();
		initStatusBar();
		scene = new Scene(root);
	}
	public void show() {
		if(stage == null)
			stage = new Stage();
	    stage.setScene(scene);
	    stage.show();
	}
	public void close() {
		if(stage == null)
			return;
		stage.close();
	}
}
