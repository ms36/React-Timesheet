package com.skillstorm.service;

import java.util.List;

import com.skillstorm.DAO.TimeSheetDAO;
import com.skillstorm.data.TimeSheet;
import com.skillstorm.data.User;

public class TimeSheetService 
{
	TimeSheetDAO timesheetdao = new TimeSheetDAO();
	
	public User getCredentialsbyUsername(User userFromClient) 
	{		
		User userFromDataBase = null;
		
		// Path to Database from Client
		if(userFromClient.getUsername() != null)
		{			
			userFromDataBase = timesheetdao.getCredentialsbyUsername(userFromClient.getUsername());			
		}
		else
		{
			// TODO return message saying nothing is entered			
		}
		
		// Path back to Client
		// If username/password match
		if(((userFromClient.getPassword()).equals(userFromDataBase.getPassword()))
		&& ((userFromClient.getUsername()).equals(userFromDataBase.getUsername())))
		{
			userFromDataBase.setLoggedIn(true);			
		}
		else
		{			
			// TODO return message saying username/password invalid	
		}		
		return userFromDataBase;
	}
	
	
	public void newTimeSheet(int userid, String dateEnd)
	{		
		timesheetdao.newTimeSheet(userid, dateEnd);		
	}
	
	public void submitTimeSheet(int userid, String dateEnd)
	{		
		timesheetdao.submitTimeSheet(userid, dateEnd);		
	}
	
	public void saveTimeSheet(TimeSheet timesheet)
	{		
		timesheetdao.saveTimeSheet(timesheet);		
	}
	
	public TimeSheet getTimeSheet(int userid, String dateEnd)
	{					
		return timesheetdao.getTimeSheet(userid, dateEnd);
	}
	
	public List<TimeSheet> getAllTimesheetabyUserId(int userid)
	{		
		return timesheetdao.getAllTimesheetabyUserId(userid);
	}
}
