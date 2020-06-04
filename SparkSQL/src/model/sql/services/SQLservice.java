package model.sql.services;

import java.sql.Connection;

import model.sql.connect.SQLConOutline;

public abstract class SQLService {
	public abstract Connection getService();
	public abstract SQLConOutline getSQLConInfo();
	public abstract void close();
}
