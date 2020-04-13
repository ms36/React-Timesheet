package com.skillstorm.DAO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.skillstorm.data.TimeSheet;
import com.skillstorm.data.User;



public class TimeSheetDAO 
{
	Connection connection = null;
	PreparedStatement preStatement = null;
	ResultSet resultSet = null;	
	
	private static final Logger log = Logger.getGlobal();

	private Connection getConnection() throws SQLException 
	{
		Connection conn;
		conn = ConnectionFactory.getInstance().getConnection();
		return conn;
	}
	
	// Create a new TimeSheet
	public void newTimeSheet(int userid, String dateEnd)
	{		
		try
		{
			String query = "INSERT INTO TimeSheet (MondaysHours, TuesdaysHours, WednesdaysHours, "
						+ "ThursdaysHours, FridaysHours, SaturdaysHours, SundaysHours, DateEndingIn, UserId)"
						+ " VALUES (0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, ?, ?)";
			connection = getConnection();
			
			preStatement = connection.prepareStatement(query);
			
			preStatement.setString(1, dateEnd);
			preStatement.setInt(2, userid);
			
			preStatement.executeUpdate();
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
	}
	
	// Changes TimeSheet's submitted to true
	public void submitTimeSheet(int userid, String dateEnd)
	{		
		try
		{
			String query = "UPDATE TimeSheet SET Submitted = true WHERE UserId = ? AND DateEndingIn = ?";
			connection = getConnection();
			
			preStatement = connection.prepareStatement(query);
			preStatement.setInt(1, userid);
			preStatement.setString(2, dateEnd);
			
			preStatement.executeUpdate(); 
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
	}
		
	// Updates the TimeSheet
	public void saveTimeSheet(TimeSheet timesheet)
	{							
		try
		{
			String query = "UPDATE TimeSheet SET  MondaysHours = ?, TuesdaysHours = ?, WednesdaysHours = ?, ThursdaysHours = ?, FridaysHours = ?, SaturdaysHours = ?, SundaysHours = ? WHERE UserId = ? AND DateEndingIn = ?";
			connection = getConnection();
			preStatement = connection.prepareStatement(query);
			
			preStatement.setDouble(1, timesheet.getWeekDayHours(0));
			preStatement.setDouble(2, timesheet.getWeekDayHours(1));
			preStatement.setDouble(3, timesheet.getWeekDayHours(2));
			preStatement.setDouble(4, timesheet.getWeekDayHours(3));
			preStatement.setDouble(5, timesheet.getWeekDayHours(4));
			preStatement.setDouble(6, timesheet.getWeekDayHours(5));
			preStatement.setDouble(7, timesheet.getWeekDayHours(6));
			preStatement.setInt(8, timesheet.user.getUserId());
			preStatement.setString(9, timesheet.getWeekEndingDate());
			
			preStatement.executeUpdate();
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
	}
		
	// Get a TimeSheet for a single week from a specific user
	public TimeSheet getTimeSheet(int userid, String dateEnd)
	{			
		TimeSheet timesheet = new TimeSheet();
		
		try
		{
			String query = "SELECT * FROM timesheet WHERE UserId = ? AND DateEndingIn = ?";
			connection = getConnection();
			
			preStatement = connection.prepareStatement(query);
			preStatement.setInt(1, userid);
			preStatement.setString(2, dateEnd);
			
			resultSet = preStatement.executeQuery();
			resultSet.next();
			
			timesheet.setTimeSheetId(resultSet.getInt(1));
			timesheet.setWeekDayHours(0, resultSet.getDouble(2));
			timesheet.setWeekDayHours(1, resultSet.getDouble(3));
			timesheet.setWeekDayHours(2, resultSet.getDouble(4));
			timesheet.setWeekDayHours(3, resultSet.getDouble(5));
			timesheet.setWeekDayHours(4, resultSet.getDouble(6));
			timesheet.setWeekDayHours(5, resultSet.getDouble(7));
			timesheet.setWeekDayHours(6, resultSet.getDouble(8));
			timesheet.setWeekEndingDate(resultSet.getString(9));
			timesheet.user.setUserId(resultSet.getInt(10));
			timesheet.setSubmitted(resultSet.getBoolean(11));
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
		return timesheet;
	}
		
	// Get all TimeSheets with user id
	public List<TimeSheet> getAllTimesheetabyUserId(int userid)
	{				
		LinkedList<TimeSheet> results = new LinkedList<>();
		
		try 
		{
			connection = getConnection();
			
			String query = "SELECT * FROM timesheet WHERE UserId = ?";
			preStatement = connection.prepareStatement(query);							
			preStatement.setInt(1, userid);
					
			resultSet = preStatement.executeQuery();
			
			while(resultSet.next())
			{
				TimeSheet ts = new TimeSheet();	
				
				ts.setTimeSheetId(resultSet.getInt(1));
				ts.setWeekDayHours(0, resultSet.getDouble(2));
				ts.setWeekDayHours(1, resultSet.getDouble(3));
				ts.setWeekDayHours(2, resultSet.getDouble(4));
				ts.setWeekDayHours(3, resultSet.getDouble(5));
				ts.setWeekDayHours(4, resultSet.getDouble(6));
				ts.setWeekDayHours(5, resultSet.getDouble(7));
				ts.setWeekDayHours(6, resultSet.getDouble(8));
				ts.setWeekEndingDate(resultSet.getString(9));
				ts.user.setUserId(resultSet.getInt(10));
				ts.setSubmitted(resultSet.getBoolean(11));
				
				results.add(ts);
			}
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
		return results;
	}
	
	// Get username and password with user name
	public User getCredentialsbyUsername(String username)
	{				
		User user = new User();
		try
		{
			String query = "SELECT * FROM user WHERE username = ?";
			connection = getConnection();
			
			preStatement = connection.prepareStatement(query);
			preStatement.setString(1, username);
			
			resultSet = preStatement.executeQuery();
			resultSet.next(); 
			
			user.setUserId(resultSet.getInt(1));
			user.setFirstName(resultSet.getString(2));
			user.setLastName(resultSet.getString(3));
			user.setUsername(resultSet.getString(4));
			user.setPassword(resultSet.getString(5));
		}
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		finally 
		{
			tryCatchInFinally();
		}		
		return user;
	}
	
	// The try/catch used in every finally{} DAO method
	public void tryCatchInFinally()
	{
		try 
		{
			if (preStatement != null)
				preStatement.close();
			if (connection != null)
				connection.close();
		} 
		catch (SQLException e) 
		{
			log.info(e.toString());
		} 
		catch (Exception e) 
		{
			log.info(e.toString());
		}
	}	
}
