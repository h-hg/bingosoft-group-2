package model.sql.services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import model.sql.connect.MySQLConConfig;
import model.sql.query.DatabaseOutline;
import model.sql.query.SQLConOutline;
import model.sql.query.TableOutline;

public class MySQLService extends SQLService {

	protected String driverClassName = "com.mysql.cj.jdbc.Driver";
	protected String protocol = "jdbc:mysql://";
	
	protected MySQLConConfig config = null;

	public MySQLService(MySQLConConfig config) {
		this.config = config;
	}
	public boolean getService() {
		String link = protocol + config.url + ":" + config.port + "/" + config.initialDbName;
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			System.err.println(driverClassName + " can't be loaded");
			e.printStackTrace();
			return false;
		}
		try {
			conn = DriverManager.getConnection(link, config.user, config.password);
		} catch (SQLException e) {
			System.err.println("Can't connect the database");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// get the information about this connection
	public SQLConOutline getSQLConInfo() {
		SQLConOutline info = new SQLConOutline();
		info.conConfig = config;
		info.dbs = new ArrayList<DatabaseOutline>();

		for (String dbName : getDatabaseNames()) {
			DatabaseOutline dbInfo = new DatabaseOutline(dbName);
			dbInfo.tables = new ArrayList<TableOutline>();
			for (String tbName : getTableNames(dbName)) {
				dbInfo.tables.add(new TableOutline(tbName));
			}
			info.dbs.add(dbInfo);
		}
		return info;
	}

}