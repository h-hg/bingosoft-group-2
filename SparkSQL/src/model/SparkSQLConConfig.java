package model;

public class SparkSQLConConfig extends SQLConConfig{
	public SparkSQLConConfig(String conName, String url, String port, String user, String password) {
		super(SQLType.SPARK, conName, url, port, user, password);
	}
}
