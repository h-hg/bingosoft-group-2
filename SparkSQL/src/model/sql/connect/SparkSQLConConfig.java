package model.sql.connect;

import model.sql.SQLType;

public class SparkSQLConConfig extends SQLConConfig{
	public String initialDbName = null;
	public SparkSQLConConfig(String conName, String url, String port, String user, String password, String initialDbName) {
		super(SQLType.SPARK, conName, url, port, user, password);
		this.initialDbName = initialDbName;
	}
}
