package model.sql.services;

import java.sql.Connection;

import model.sql.connect.SQLConOutline;
import model.sql.query.SQLResult;

public abstract class SQLService {
	public abstract Connection getService();
	public abstract SQLConOutline getSQLConInfo();
	public abstract SQLResult getResult(String sqlCode);
	public abstract void close();
}
