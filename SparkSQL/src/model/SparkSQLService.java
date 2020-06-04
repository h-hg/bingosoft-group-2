package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SparkSQLService {
	public String head="jdbc:hive2://";
	public String conname;
	public String url;
	public String post;
	public String username;
	public String password;
	public String db;
	public Connection conn;
    Statement stmt;
    ResultSet res;
    String driverClassName="org.apache.hive.jdbc.HiveDriver";
    
    public SparkSQLService(SparkSQLConConfig config,String sqldb) {
    	this.conname=config.name;
    	this.url=config.url;
    	this.post=config.port;
    	this.username=config.user;
    	this.password=config.password;
    	this.db=sqldb;
    }
    
    //获得连接
    public Connection getSparkSQLService() throws SQLException{
    	String link=head+ url+ ":" + post + "/" + db;
    	try {
    		
    		Class.forName(driverClassName);
            System.out.println("数据库驱动加载成功");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();      
            return null;
        }
    	try {
            conn = DriverManager.getConnection(link,username,password);
            System.out.println("数据库连接成功");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
    		        }
    	return conn;

    }
    

    //获得数据库列表
    @SuppressWarnings("null")
	public ArrayList<String> getDB() {
		ArrayList<String> dbList = new ArrayList<String>();
    	try {
			stmt = conn.createStatement();
			String sql = "show databases";
			res = stmt.executeQuery(sql);
			while(res.next()) {
				String databaseName  = res.getString(1);
				dbList.add(databaseName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return dbList;
		}
    	return dbList;
    }
    
    //获得表列表
    @SuppressWarnings("null")
	public ArrayList<String> getTable(String db) {
		ArrayList<String> tableList = new ArrayList<String>();
    	try {
			stmt = conn.createStatement();
			String sql = "show tables in " + db;
			res = stmt.executeQuery(sql);
			while(res.next()) {
				String tableName = res.getString(1);
				tableList.add(tableName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return tableList;
		}
    	return tableList;
    }
    
    //关闭连接
    public void close() {
    	try {
    		if(res != null) res.close();
    		if(stmt != null) stmt.close();
    		if(conn != null) conn.close();
    		System.out.println("关闭连接成功");
    		} catch(SQLException e) {
    			e.printStackTrace();
    		}

    	}

}
