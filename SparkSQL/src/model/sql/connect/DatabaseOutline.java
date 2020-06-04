package model.sql.connect;

import java.util.ArrayList;

public class DatabaseOutline {
	public String name = null;
	public ArrayList<TableOutline> tables = null;
	public DatabaseOutline() {}
	public DatabaseOutline(String name) {
		this.name = name;
	}
}
