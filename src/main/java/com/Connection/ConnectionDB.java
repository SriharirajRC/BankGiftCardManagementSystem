package com.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
	private static final String url = "jdbc:postgresql://localhost:5432/bankmanagement";
	private static final String user = "postgres";
	private static final String pass = "7621";
	
	public static Connection getConnection() throws ClassNotFoundException {
		
		Class.forName("org.postgresql.Driver");
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url,user,pass);
		}
		catch(SQLException e){
			System.out.println("Error While Connection to Database"+ e.getMessage());
		}
		
		return connection;		
	}
}
