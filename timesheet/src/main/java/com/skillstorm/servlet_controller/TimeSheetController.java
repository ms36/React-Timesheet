package com.skillstorm.servlet_controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.data.TimeSheet;
import com.skillstorm.data.User;
import com.skillstorm.service.TimeSheetService;

public class TimeSheetController 
{
	TimeSheetService timesheetservice = new TimeSheetService();
	
	public void getCredentials(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		resp.setContentType("application/json");
				
		// Get the Username/Password entered
		User userIn =  new User();
		User userOut = new User();
		userIn.setUsername(req.getParameter("username"));
		userIn.setPassword(req.getParameter("password"));
		
		userOut = timesheetservice.getCredentialsbyUsername(userIn);
		
		resp.getWriter().println(new ObjectMapper().writeValueAsString(userOut));
		
		if (userOut.isLoggedIn())
		{
			userOut.setPassword("");
			req.getSession().setAttribute("userinfo", userOut);
		}
		else
		{
			resp.setStatus(401);
		}		
	}
	
	public void newTimeSheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{		
		int userid = Integer.parseInt(req.getParameter("userid"));
		String dateEnd = req.getParameter("dateEnd");
		
		timesheetservice.newTimeSheet(userid, dateEnd);			
	}
	
	public void submitTimeSheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{		
		int userid = Integer.parseInt(req.getParameter("userid"));
		String dateEnd = req.getParameter("dateEnd");
		
		timesheetservice.submitTimeSheet(userid, dateEnd);		
	}
	
	public void saveTimeSheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{					
		resp.setContentType("application/json");
		timesheetservice.saveTimeSheet(new ObjectMapper().readValue(req.getInputStream(), TimeSheet.class));					
	}
	
	public void getTimeSheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{		
		TimeSheet timesheet = new TimeSheet();
		int userid = Integer.parseInt(req.getParameter("userid"));
		String dateEnd = req.getParameter("dateEnd");
		
		timesheet = timesheetservice.getTimeSheet(userid, dateEnd);
		
		resp.getWriter().println(new ObjectMapper().writeValueAsString(timesheet));		
	}
	
	public void getAllTimesheetabyUserId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{				
		int userid = Integer.parseInt(req.getParameter("userid"));
		resp.getWriter().println(new ObjectMapper().writeValueAsString(timesheetservice.getAllTimesheetabyUserId(userid)));	
	}
}
