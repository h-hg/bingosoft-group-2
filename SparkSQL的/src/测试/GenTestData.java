package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.collections.FXCollections;
import model.DatabaseInfo;
import model.DatabaseTableInfo;
import model.SQLConConfig;
import model.SQLConInfo;
import model.SQLResult;
import model.SQLResultTable;
import model.SQLType;
import model.SparkSQLConConfig;
import model.SparkSQLService;

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
	static public DatabaseTableInfo genDatabaseTableInfo(String table) {
		DatabaseTableInfo ret = new DatabaseTableInfo(table);
		return ret;
	}
	static public DatabaseInfo genDatabaseInfo(SparkSQLService serv,String db) {
		DatabaseInfo ret = new DatabaseInfo();
		ret.name = db;
		ArrayList <String> tableList=serv.getTable(db);
		ArrayList<DatabaseTableInfo> tbs = new ArrayList<DatabaseTableInfo>();
		for(int i = 0; i < tableList.size(); ++i) {
			tbs.add(genDatabaseTableInfo(tableList.get(i)));
		}
		ret.tables = tbs;
		return ret;
	}
	static public SQLConInfo genSQLConInfo(SparkSQLService serv) {
		SQLConInfo ret = new SQLConInfo();
		ret.conConfig = new SparkSQLConConfig(serv.conname,serv.url,serv.post,serv.username,serv.password);
		
		ArrayList <String> dbList=serv.getDB();
		ArrayList<DatabaseInfo> dbs = new ArrayList<DatabaseInfo>();
		for(int i = 0; i < dbList.size(); ++i) {
			dbs.add(genDatabaseInfo(serv,dbList.get(i)));
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

