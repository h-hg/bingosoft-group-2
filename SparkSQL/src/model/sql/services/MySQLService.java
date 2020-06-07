package model.sql.services;

import model.sql.connect.MySQLConConfig;

public class MySQLService extends SQLService {
	
	protected MySQLConConfig config = null;
	
	public MySQLService(MySQLConConfig config) {
		super("com.mysql.cj.jdbc.Driver");
		this.config = config;
	}
	public String getConURL() {
		return "jdbc:mysql://" + config.url + ":" + config.port + "/" + config.initialDbName;
	}
	public MySQLConConfig getSQLConConfig() {
		return this.config;
	}
}