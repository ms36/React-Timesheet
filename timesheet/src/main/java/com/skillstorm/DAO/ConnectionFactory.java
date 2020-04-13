package com.skillstorm.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionFactory {
	
	String driverClassName = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/macgyver";
	String user = "root";
	String password = "Root";
	
	private static final Logger log = Logger.getGlobal();
	private static ConnectionFactory connectFactory;
	
	private ConnectionFactory()
	{
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {			
			log.info(e.toString());
		}
	}
	
	public Connection getConnection() throws SQLException
	{
		Connection conn = null;	
		conn = DriverManager.getConnection(url, user, password);
		
		return conn;
	}
	
	public static ConnectionFactory getInstance() {
		if (connectFactory == null) 
		{
			connectFactory = new ConnectionFactory();
		} 
		return connectFactory;
	}
}
