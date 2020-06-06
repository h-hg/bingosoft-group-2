package model.sql.services;

import model.sql.connect.SQLConConfig;
import model.sql.connect.SparkSQLConConfig;

public class SparkSQLService extends SQLService {

	protected SparkSQLConConfig config = null;

	public SparkSQLService(SparkSQLConConfig config) {
		super("org.apache.hive.jdbc.HiveDriver");
		this.config = config;
	}
	public String getConURL() {
		return "jdbc:hive2://" + config.url + ":" + config.port + "/" + config.initialDbName;
	}
	public SQLConConfig getSQLConConfig() {
		return this.config;
	}
}
