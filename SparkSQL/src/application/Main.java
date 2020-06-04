package application;

import controller.Controller;
import controller.Manager;
import controller.StartedControl;
import javafx.application.Application;
import javafx.stage.Stage;
import test.GenTestData;
import view.StartedWin;

public class Main extends Application {
	private Controller controller = null;

	public void start(Stage primaryStage) {
		StartedWin startedWin = new StartedWin();
		StartedControl startedControl = new StartedControl(startedWin);
		this.controller = startedControl;
		
		Manager.name2Controller.put(startedWin.name, startedControl);
	    primaryStage.setScene(startedWin.scene);
	    primaryStage.setTitle("Group 2");
	    primaryStage.show();
	    
	    //test code
	    //SparkSQLConConfig config = new SparkSQLConConfig(conname,sqlurl,sqlpost,sqlusername,sqlpassword, sqldb);
	    //SparkSQLService serv = new SparkSQLService(config);//至少需要连接一个数据库
	    //serv.getService();
	    //startedControl.addSQLConInfo(serv.getSQLConInfo());

	    startedControl.addSQLResult(GenTestData.genSQLResult());
	}
	@Override
    public void stop() throws Exception {
		//release the resource
		this.controller.close();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
