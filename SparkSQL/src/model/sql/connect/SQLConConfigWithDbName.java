package model.sql.connect;

import model.sql.SQLType;

public class SQLConConfigWithDbName extends SQLConConfig{
	public String initialDbName = null;
	public SQLConConfigWithDbName(SQLType type, String conName, String url, String port, String user, String password, String initialDbName) {
		super(type, conName, url, port, user, password);
		this.initialDbName = initialDbName;
	}
}
