package model.sql.connect;

import model.sql.SQLType;

public class MySQLConConfig extends SQLConConfigWithDbName{
	public MySQLConConfig(String conName, String url, String port, String user, String password, String initialDbName) {
		super(SQLType.MYSQL, conName, url, port, user, password, initialDbName);
	}
}
