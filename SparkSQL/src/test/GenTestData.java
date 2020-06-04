package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.collections.FXCollections;
import model.sql.SQLType;
import model.sql.connect.DatabaseOutline;
import model.sql.connect.SQLConConfig;
import model.sql.connect.SQLConOutline;
import model.sql.connect.TableOutline;
import model.sql.query.SQLResult;
import model.sql.query.SQLResultTable;

public class GenTestData {
	public static Random random = new Random();
	static public int genInt(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}
	static public String genString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuffer sb =new StringBuffer();   
	     for(int i = 0; i < length; i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	}
	static public SQLConConfig genSQLConConfig() {
		SQLConConfig ret = new SQLConConfig(SQLType.SPARK, genString(5), genString(5), genString(5), genString(5), genString(5));
		return ret;
	}
	static public TableOutline genTableOutline() {
		TableOutline ret = new TableOutline(genString(5));
		return ret;
	}
	static public DatabaseOutline genDatabaseOutline() {
		DatabaseOutline ret = new DatabaseOutline();
		ret.name = genString(5);
		ArrayList<TableOutline> tbs = new ArrayList<TableOutline>();
		for(int i = 0, n = genInt(1, 5); i < n; ++i) {
			tbs.add(genTableOutline());
		}
		ret.tables = tbs;
		return ret;
	}
	static public SQLConOutline genSQLConInfo() {
		SQLConOutline ret = new SQLConOutline();
		ret.conConfig = genSQLConConfig();
		ArrayList<DatabaseOutline> dbs = new ArrayList<DatabaseOutline>();
		for(int i = 0, n = genInt(1, 3); i < n; ++i) {
			dbs.add(genDatabaseOutline());
		}
		ret.dbs = dbs;
		return ret;
	}
	static public Map<String, String> genSQLResultTableRow(ArrayList<String> columns) {
		Map<String, String> ret = new HashMap<>();
		for(int i = 0; i < columns.size(); ++i) {
			ret.put(columns.get(i), genString(5));
		}
		return ret;
	}
	static public SQLResultTable genSQLResultTable() {
		SQLResultTable ret = new SQLResultTable();
		ret.name = genString(5);
		//generate column
		ret.columns = new ArrayList<String>();
		int n = genInt(2, 3);
		for(int i = 0; i < n; ++i) {
			ret.columns.add(genString(5));
		}
		//generate data
		ret.content = FXCollections.observableArrayList();
		int m = genInt(3, 5);
		for(int i = 0; i < m; ++i) {
			ret.content.add(genSQLResultTableRow(ret.columns));
		}
		return ret;
	}
	static public SQLResult genSQLResult() {
		SQLResult ret = new SQLResult();
		ret.info = genString(30);
		ret.tables = new ArrayList<SQLResultTable>();
		int n = genInt(2, 3);
		for(int i = 0; i < n; ++i) {
			ret.tables.add(genSQLResultTable());
		}
		return ret;
	}
}
