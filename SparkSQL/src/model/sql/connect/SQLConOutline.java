package model.sql.connect;

import java.util.ArrayList;

public class SQLConOutline {
	public SQLConConfig conConfig = null;
	public ArrayList<DatabaseOutline> dbs = null;
	public SQLConOutline() {}
	public SQLConOutline(SQLConConfig config) {
		this.conConfig = config;
	}
}
