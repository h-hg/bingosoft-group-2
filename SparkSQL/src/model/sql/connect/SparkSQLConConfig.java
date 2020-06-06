package model.sql.connect;

import model.sql.SQLType;

public class SparkSQLConConfig extends SQLConConfigWithDbName{
	public SparkSQLConConfig(String conName, String url, String port, String user, String password, String initialDbName) {
		super(SQLType.SPARK, conName, url, port, user, password, initialDbName);
	}
}
