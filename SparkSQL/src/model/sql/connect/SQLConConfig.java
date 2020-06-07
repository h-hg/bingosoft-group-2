package model.sql.connect;

import model.sql.SQLType;

public abstract class SQLConConfig {
	public SQLType type = null;
	public String name = null;
	public String url = null;
	public String port = null;
	public String user = null;
	public String password = null;
	public SQLConConfig(SQLType type, String conName, String url, String port, String user, String password) {
		this.type = type;
		this.name = conName;
		this.url = url;
		this.port = port;
		this.user = user;
		this.password = password;
	}
}
