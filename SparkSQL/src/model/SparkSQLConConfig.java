package model;

public class SparkSQLConConfig {
	public String url = null;
	public String port = null;
	public String user = null;
	public String password = null;
	public SparkSQLConConfig(String url, String port, String user, String password) {
		this.url = url;
		this.port = port;
		this.user = user;
		this.password = password;
	}
}
