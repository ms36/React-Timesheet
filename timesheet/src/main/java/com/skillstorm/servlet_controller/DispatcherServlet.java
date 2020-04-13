package com.skillstorm.servlet_controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet{
	
	private TimeSheetController timesheetController = new TimeSheetController();

	protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{				
		String uri = req.getRequestURI();
		String requestType = req.getMethod();
		
		if(requestType.equals("GET"))
		{
			switch (uri) 
			{
			case "/timesheet/portal/timesheet-new":				
				timesheetController.newTimeSheet(req, resp);
				return;
			case "/timesheet/portal/timesheet-get":				
				timesheetController.getTimeSheet(req, resp);
				return;
			case "/timesheet/portal/timesheet-getall":				
				timesheetController.getAllTimesheetabyUserId(req, resp);
				return;
			default:				
				break;
			}
		
		}
		else if(requestType.equals("POST"))
		{
			switch (uri) 
			{
			case "/timesheet/portal/timesheet-save":					
				timesheetController.saveTimeSheet(req, resp);
				return;						
			case "/timesheet/portal/timesheet-submit":
				timesheetController.submitTimeSheet(req, resp);				
				return;
			case "/timesheet/portal/login":					
				timesheetController.getCredentials(req, resp);
				return;
			default:				
				break;
			}
		}
		else
		{
			// TODO return message saying invalid request
		}		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {				
		doDispatch(req, resp);		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		doDispatch(req, resp);		
	}
}
 