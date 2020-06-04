package model.sql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.HEAD;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.nullCondition_return;

import model.sql.connect.SparkSQLConConfig;

public class SparkSQLService {

	private String driverClassName = "org.apache.hive.jdbc.HiveDriver";
	private String protocol = "jdbc:hive2://";
	private Connection conn;
	private SparkSQLConConfig config = null;
	public SparkSQLService(SparkSQLConConfig config) {
		this.config = config;
	}

	public Connection getService() {
		String link = protocol + config.url + ":" + config.port + "/" + config.initialDbName;
		try {
			Class.forName(driverClassName);
			System.err.println("Database driver can't be loaded");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		try {
			conn = DriverManager.getConnection(link, config.user, config.password);
		} catch (SQLException e) {
			System.err.println("Can't connect the database");
			e.printStackTrace();
			return null;
		}
		return conn;
	}

	//get the list of database name
	public ArrayList<String> getDbNames() {
		ArrayList<String> dbList = new ArrayList<String>();
		try {
			try(Statement stmt = conn.createStatement()) {
				try(ResultSet res = stmt.executeQuery("show databases")) {
					while (res.next()) {
						dbList.add(res.getString(1));
					}					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbList;
	}

	//get the list of  table name in database db
	public ArrayList<String> getTableNames(String db) {
		ArrayList<String> tableList = new ArrayList<String>();
		try {
			try(Statement stmt = conn.createStatement()) {
				try(ResultSet res = stmt.executeQuery("show tables in " + db)) {
					while (res.next()) {
						tableList.add(res.getString(1));
					}					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return tableList;
		}
		return tableList;
	}

	//close the connection
	public void close() {
		try {
			if (conn != null)
				conn.close();
			//System.out.println("关闭连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
