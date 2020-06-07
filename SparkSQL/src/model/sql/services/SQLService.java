package model.sql.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import model.sql.connect.SQLConConfig;
import model.sql.query.DatabaseOutline;
import model.sql.query.SQLConOutline;
import model.sql.query.SQLResult;
import model.sql.query.SQLResultTable;
import model.sql.query.TableOutline;

public abstract class SQLService {
	protected Connection conn = null;
	protected String driverClassName = null;


	public SQLService(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public abstract String getConURL();
	public abstract SQLConConfig getSQLConConfig();
	public boolean getService() {
		String link = getConURL();
		SQLConConfig config = getSQLConConfig();
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			System.err.println(driverClassName + " can't be loaded");
			e.printStackTrace();
			return false;
		}
		try {
			conn = DriverManager.getConnection(link, config.user, config.password);
		} catch (SQLException e) {
			System.err.println("Can't connect the database");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// get the information about this connection
	public SQLConOutline getSQLConInfo() {
		SQLConOutline info = new SQLConOutline();
		info.conConfig = getSQLConConfig();
		info.dbs = new ArrayList<DatabaseOutline>();

		for (String dbName : getDatabaseNames()) {
			DatabaseOutline dbInfo = new DatabaseOutline(dbName);
			dbInfo.tables = new ArrayList<TableOutline>();
			for (String tbName : getTableNames(dbName)) {
				dbInfo.tables.add(new TableOutline(tbName));
			}
			info.dbs.add(dbInfo);
		}
		return info;
	}
	// get the list of database name
	public ArrayList<String> getDatabaseNames() {
		ArrayList<String> dbList = new ArrayList<String>();
		try {
			try (Statement stmt = conn.createStatement()) {
				try (ResultSet res = stmt.executeQuery("show databases")) {
					while (res.next()) {
						dbList.add(res.getString(1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbList;
	}

	// get the list of table name in database db
	public ArrayList<String> getTableNames(String db) {
		ArrayList<String> tableList = new ArrayList<String>();
		try {
			try (Statement stmt = conn.createStatement()) {
				try (ResultSet res = stmt.executeQuery("show tables in " + db)) {
					while (res.next()) {
						tableList.add(res.getString(1));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return tableList;
		}
		return tableList;
	}

	public SQLResult getResult(String sqlCode) {

		SQLResult sqlResult = new SQLResult();
		StringBuilder sb = new StringBuilder();
		ArrayList<SQLResultTable> resultTables = new ArrayList<SQLResultTable>();
		// the result index
		int cnt = 0;
		// the operation index
		int count = 0;
		for (String query : sqlCode.split(";")) {
			query = query.trim();//remove the space
			if (query == null || query.length() == 0)
				continue;

			//change first word of query statement
			query=changeFirstWordToLowerCase(query);

			int type = getOperationType(query);
			// there exists spelling mistake or
			// the situation this program doesn's consider
			if (type == 0) {
				sb.append("Opertion " + (++count) + ": Something wrong! Please check your statement(word spell).\n\n");
				continue;
			}

			// select, show, describe
			if (type == 1) {
				SQLResultTable sqlResultTable = getRetrieveResult(query);
				if (sqlResultTable == null) {
					sb.append("Opertion " + (++count) + ": Failed\n\n");
				} else {
					sb.append("Opertion " + (++count) + ": Successful\n\n");
					sqlResultTable.name = "Result " + (++cnt);
					resultTables.add(sqlResultTable);
				}
			}
			// insert, delete, update
			else if (type == 2) {
				String rs = getUpdateResult(query);
				sb.append("Opertion " + (++count) + ": " + rs + "\n\n");
			}
			// create, drop
			else if (type == 3) {
				String rs = getCreateResult(conn, query);
				sb.append("Opertion " + (++count) + ": " + rs + "\n\n");
			}
		}
		sqlResult.info = sb.toString();
		sqlResult.tables = resultTables;
		return sqlResult;
	}

	//change the first word of the query statement to lower case
	//cause mysql and sparksql can deal with word like from, into and table name in upper case
	//we can just care about first.
	protected String changeFirstWordToLowerCase(String query){
		if(query.indexOf(' ') == -1)
			return query;
		StringBuilder sb=new StringBuilder();
		int i=0;
		for(;i<query.length();i++){
			char c= query.charAt(i);
			if(c==' ') break;
			sb.append(Character.toLowerCase(c));
		}
		sb.append(query,i,query.length());
		return  sb.toString();
	}



	/*
	 * 0: the operation that this code doesn's implement
	 * 1: select or show operation
	 * 2: insert, update, alter or delete operation 3: create or drop operation
	 */
	protected int getOperationType(String query) {
		//find the query statement's first word to judge what kind of operation it is
		String kind = query.split(" ")[0];
		if ("select".equals(kind) || "show".equals(kind)||"describe".equals(kind)||"desc".equals(kind))
			return 1;
		if ("insert".equals(kind) || "update".equals(kind) || "delete".equals(kind) || "alter".equals(kind))
			return 2;
		if ("create".equals(kind) || "drop".equals(kind))
			return 3;
		return 0;
	}

	// select, show, describe
	protected SQLResultTable getRetrieveResult(String qurey) {

		SQLResultTable sqlResultTable = new SQLResultTable();
		try (PreparedStatement ps = conn.prepareStatement(qurey)) {
			ResultSet rs = ps.executeQuery();

			// table's columns
			ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int metaCount = resultSetMetaData.getColumnCount();			
			sqlResultTable.columns = new ArrayList<String>(metaCount);
			for(int i = 1; i <= metaCount; ++i) {
				sqlResultTable.columns.add(resultSetMetaData.getColumnName(i));
			}
			
			// table's content
			sqlResultTable.content = FXCollections.observableArrayList();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= metaCount; i++) {
					map.put(resultSetMetaData.getColumnName(i), rs.getString(i));
				}
				sqlResultTable.content.add(map);
			}

			return sqlResultTable;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}
	}

	// insert, update, delete, alter no SQLResultTable to return
	protected String getUpdateResult(String qurey) {

		try (PreparedStatement ps = conn.prepareStatement(qurey)) {
			int rs = ps.executeUpdate();
			if (rs > 0)
				return new String("Successful!");
			else
				return new String("Failed!");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return new String("Something wrong! Please check your statement or make sure you have right to do this operation.");
		}
	}

	// create, drop no SQLResultTable to return
	protected String getCreateResult(Connection conn, String qurey) {
		try (PreparedStatement ps = conn.prepareStatement(qurey)) {
			if (ps.executeUpdate() == 0)
				return new String("Successful!");
			else
				return new String("Failed!");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return new String("Something wrong! Please check your statement or make sure you have right to do this operation.");
		}
	}

	// close the connection
	public void close() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
