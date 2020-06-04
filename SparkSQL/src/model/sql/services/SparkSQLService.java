package model.sql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import javax.ws.rs.HEAD;

//import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.intervalLiteral_return;
//import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.nullCondition_return;

import model.sql.connect.DatabaseOutline;
import model.sql.connect.TableOutline;
import model.sql.connect.SQLConOutline;
import model.sql.connect.SparkSQLConConfig;

public class SparkSQLService extends SQLService {

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
		} catch (ClassNotFoundException e) {
			System.err.println("Database driver can't be loaded");
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
	public ArrayList<String> getDatabaseNames() {
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
	//get the information about this connection
	public SQLConOutline getSQLConInfo() {
		SQLConOutline info = new SQLConOutline();
		info.conConfig = config;
		info.dbs = new ArrayList<DatabaseOutline>();
		
		for(String dbName : getDatabaseNames()) {
			DatabaseOutline dbInfo = new DatabaseOutline(dbName);
			dbInfo.tables = new ArrayList<TableOutline>();
			for(String tbName : getTableNames(dbName)) {
				dbInfo.tables.add(new TableOutline(tbName));
			}
			info.dbs.add(dbInfo);
		}
		return info;
	}
	//close the connection
	public void close() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
