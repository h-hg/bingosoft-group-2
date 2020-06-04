package application;

import java.sql.SQLException;

import controller.Manager;
import controller.StartedControl;
import javafx.application.Application;
import javafx.stage.Stage;
import test.GenTestData;
import view.StartedWin;
import model.*;


public class Main extends Application {

	  String sqldriverClassName="org.apache.mysql.jdbc.mysqlDriver";
	  String conname="bigdata";
	  String sqlurl="bigdata31.depts.bingosoft.net";
	  String sqlpost="22231";	
	  String sqlusername="user08";
	  String sqlpassword="pass@bingo8";
	  String sqldb="user08_db";

	@Override
	
	public void start(Stage primaryStage) {
		//获取连接
		SparkSQLConConfig config=new SparkSQLConConfig(conname,sqlurl,sqlpost,sqlusername,sqlpassword);
		SparkSQLService serv=new SparkSQLService(config,sqldb);//至少需要连接一个数据库
		try {
			serv.getSparkSQLService();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StartedWin startedWin = new StartedWin();
		StartedControl startedControl = new StartedControl(startedWin);
		
		//Manager.name2Win.put(startedWin.name, startedWin);
		Manager.name2Controller.put(startedWin.name, startedControl);
	    primaryStage.setScene(startedWin.scene);
	    primaryStage.setTitle("Group 2");
	    primaryStage.show();
	    
	    //test code
	    startedControl.addSQLConInfo(GenTestData.genSQLConInfo(serv));
	    //startedControl.addSQLConInfo(GenTestData.genSQLConInfo());
	    startedControl.addSQLResult(GenTestData.genSQLResult());
	    startedControl.test();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
