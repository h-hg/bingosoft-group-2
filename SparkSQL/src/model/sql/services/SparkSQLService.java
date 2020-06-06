package model.sql.services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import model.sql.connect.DatabaseOutline;
import model.sql.connect.TableOutline;
import model.sql.connect.SQLConOutline;
import model.sql.connect.SparkSQLConConfig;

public class SparkSQLService extends SQLService {

	protected String driverClassName = "org.apache.hive.jdbc.HiveDriver";
	protected String protocol = "jdbc:hive2://";
	
	protected SparkSQLConConfig config = null;

	public SparkSQLService(SparkSQLConConfig config) {
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
