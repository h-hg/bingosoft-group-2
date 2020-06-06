package model.sql.query;

import java.util.ArrayList;

import model.sql.connect.SQLConConfig;

public class SQLConOutline {
	public SQLConConfig conConfig = null;
	public ArrayList<DatabaseOutline> dbs = null;
	public SQLConOutline() {}
	public SQLConOutline(SQLConConfig config) {
		this.conConfig = config;
	}
}
