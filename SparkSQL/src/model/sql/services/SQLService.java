package model.sql.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import model.sql.connect.SQLConOutline;
import model.sql.query.SQLResult;
import model.sql.query.SQLResultTable;

public abstract class SQLService {

	protected Connection conn = null;
	
	public SQLService() {}
	public abstract boolean getService();
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

	// get the information about this connection
	public abstract SQLConOutline getSQLConInfo();

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
			int type = getOperationType(query);
			// there exists spelling mistake or
			// the situation this program doesn's consider
			if (type == 0) {
				sb.append("Opertion " + (++count) + ": something wrong! Please check your statement(word spell).\n\n");
				continue;
			}

			// select, show
			if (type == 1) {
				SQLResultTable sqlResultTable = getRetrieveResult(query);
				if (sqlResultTable == null) {
					sb.append("Opertion " + (++count) + ": failed\n\n");
				} else {
					sb.append("Opertion " + (++count) + ": successful\n\n");
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

	/*
	 * 0: the operation that this code doesn's implement
	 * 1: select or show operation
	 * 2: insert, update, alter or delete operation 3: create or drop operation
	 */
	protected int getOperationType(String query) {
		//find the query statement's first word to judge what kind of operation it is
		String kind = query.split(" ")[0];
		if ("select".equals(kind) || "show".equals(kind))
			return 1;
		if ("insert".equals(kind) || "update".equals(kind) || "delete".equals(kind) || "alter".equals(kind))
			return 2;
		if ("create".equals(kind) || "drop".equals(kind))
			return 3;
		return 0;
	}

	// select, show
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
			if (ps.execute())
				return new String("Successful!");
			else
				return new String("Failed!");
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return new String("something wrong! Please check your statement or make sure you have right to do this operation.");
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
