package com.aurionpro.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static Connection connection = null;
	
	public static Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			if(connection == null) connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/collegedb", "", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
