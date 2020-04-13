package com.skillstorm.timesheet.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import com.skillstorm.timesheet.beans.DayOfWeek;
import com.skillstorm.timesheet.beans.LogInOut;
import com.skillstorm.timesheet.beans.Timesheet;
import com.skillstorm.timesheet.service.TimesheetService;


public class ExcelSpreadSheet {
	String file = "C:\\Users\\mstev\\Desktop\\Timesheet.xlsx";
	String otherFile = "C:\\Users\\mstev\\Desktop\\new.xlsx";
	FileInputStream fileInputStream;
	Workbook workbook;
	Sheet sheet;
	Cell cell;
	Row row;
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	double[] totalHoursWorked = {0, 0, 0, 0, 0, 0, 0, 0};
	
	private static final Logger log = Logger.getLogger(TimesheetService.class);
	
	public ExcelSpreadSheet() 
	{
		try 
		{
			fileInputStream = new FileInputStream(new File(file));
			
			workbook = WorkbookFactory.create(fileInputStream);				
			sheet = workbook.getSheetAt(0);
			cell = null;
			row = null;
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	};

	public void create() {

		// Table header color (216, 216, 216)
		// Table days color (242, 242, 242)
		// Table total color (238, 236, 225)
		// Name/Week End font color (192, 0, 0)
		
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		style.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 216, 216), new DefaultIndexedColorMap()));
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
		
		
		Map<String, Object[]> data = new HashMap<String, Object[]>();
		//data.put("1", new Object[] {"Emp No.", "Name", "Salary"});
		//data.put("2", new Object[] {1d, "John", 1500000d});
		//data.put("3", new Object[] {2d, "Sam", 800000d});
		data.put("4", new Object[] {3d, "Dean", 700000d});
		
		Set<String> keyset = data.keySet();
		int rownum = 0;
		
		for (String key : keyset) {
			row = sheet.createRow(rownum++);
			Object [] objArr = data.get(key);
			int cellnum = 0;
			
			for (Object obj : objArr) {
				cell = row.createCell(cellnum++);
				
				if(obj instanceof Date) {
					cell.setCellValue((Date)obj);
				}
				else if(obj instanceof Boolean) {
					cell.setCellValue((Boolean)obj);
				}
				else if(obj instanceof String) {
					cell.setCellValue((String)obj);
					cell.setCellStyle(style);
					
				}
				else if(obj instanceof Double)
					cell.setCellValue((Double)obj);
			}
		}
		
		try {
			FileOutputStream out = 
					new FileOutputStream(new File(otherFile));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Update Excel Spreadsheet
		public void update(Timesheet timesheet) 
		{
			log.info("Updating timesheet");
			try {
				double sumOfHours = 0.0;
				int cellNumber = 7;
				int rowSum = 15;	
				List<DayOfWeek> dayOfWeeks = timesheet.getDayOfWeeks();
				List<LogInOut> logInOut = new ArrayList<>();
				int listSize = dayOfWeeks.size();
				
				// Get all the LogInOuts from the all the dayOfWeeks
				for(int i = 0; i < listSize; ++i) 
				{
					logInOut.addAll(timesheet.getDayOfWeeks().get(i).getLogInOuts());
				}
				log.info(logInOut);
				// Enter name
				updateCell(1, 1, timesheet.getUser().getFirstName() + " " + timesheet.getUser().getLastName());
				
				// Enter week ending date
				updateCell(2, 1, timesheet.getWeekEndingDate());
				
				// Enter login/logout times
				for(int rowNumber = 0; rowNumber < 7; rowNumber++) 
				{
					int rowOffset = 8;
					// Number of login/logout pairs
					int seriesCount = 0;
					
					for(int columnNumber = 1; columnNumber < 7; columnNumber++) 
					{
						if (columnNumber % 2 == 0) 
						{
							updateCell(rowNumber + rowOffset, columnNumber, logInOut.get(seriesCount + (3 * rowNumber)).getLogOut());
							seriesCount++;
						}
						else 
						{
							updateCell(rowNumber + rowOffset, columnNumber, logInOut.get(seriesCount + (3 * rowNumber)).getLogIn());
						}
					}
				}
				
				calculateAllHoursInAWeek(timesheet);
				
				// Enters the hours for the week
				for(int rowNumber = 0; rowNumber < 7; rowNumber++) 
				{
					int rowOffset = 8;	
					
					updateCell(rowNumber + rowOffset, cellNumber, decimalFormat.format(totalHoursWorked[rowNumber]));
					
					sumOfHours += totalHoursWorked[rowNumber];
				}
				// Sum of the hours for the week
				updateCell(rowSum, cellNumber, decimalFormat.format(sumOfHours));
				
				fileInputStream.close();
				
				FileOutputStream outFile =new FileOutputStream(new File(file));
				workbook.write(outFile);
				outFile.close();
				
				log.info("Excel updated successfully..");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Update Cell with a double
		private void updateCell (int rowNumber, int columnNumber, double cellValue) 
		{
			// Get/Create row
			row = sheet.getRow(rowNumber);
			if (row == null) 
			{
				row = sheet.createRow(rowNumber);
			}
			
			// Get/Create cell
			cell = sheet.getRow(rowNumber).getCell(columnNumber);
			if (cell == null) 
			{
				cell = row.createCell(columnNumber);
			}
			cell.setCellValue(cellValue);
		}
		
		// Update Cell with a String
		private void updateCell (int rowNumber, int columnNumber, String cellValue) 
		{
			// Get/Create row
			row = sheet.getRow(rowNumber);
			if (row == null) 
			{
				row = sheet.createRow(rowNumber);
			}
			
			// Get/Create cell
			cell = sheet.getRow(rowNumber).getCell(columnNumber);
			if (cell == null) 
			{
				cell = row.createCell(columnNumber);
			}
			cell.setCellValue(cellValue);
		}
		
		// Calculates all login/out times for a day
		  private void calculateHoursInADay(Timesheet timesheet, int dayOfWeek) 
		  {
			  
			  List<DayOfWeek> dayOfWeeks = timesheet.getDayOfWeeks();
			  List<LogInOut> logInOut = new ArrayList<>();
			  logInOut.addAll(dayOfWeeks.get(dayOfWeek).getLogInOuts());			  
			
		    // Number of login/logout pairs
		    int seriesCount = dayOfWeeks.get(dayOfWeek).getLogInOuts().size();
		    
		    // Reset to recalculate
		    totalHoursWorked[dayOfWeek] = 0;

		    for (int i = 0; i < seriesCount; i++) 
		    {
		    	double hoursIn;
			    double hoursOut;
			    double minutesIn;
			    double minutesOut;
			    double minutes;
			    
			    // If time is empty, set hours/minutes to zero
		    	if (logInOut.get(i).getLogIn() == "") 
		    	{
		    		hoursIn = 0.0;
				    hoursOut = 0.0;
				    minutesIn = 0.0;
				    minutesOut = 0.0;
		    	} 
		    	else // Split time between hours/minutes and convert from string to number
		    	{		    		
				    String[] timeIn = logInOut.get(i).getLogIn().split(":");
				    String[] timeOut = logInOut.get(i).getLogOut().split(":");
				    hoursIn = Double.parseDouble(timeIn[0]);
				    hoursOut = Double.parseDouble(timeOut[0]);
				    minutesIn = Double.parseDouble(timeIn[1]);
				    minutesOut = Double.parseDouble(timeOut[1]);
		    	}

		      totalHoursWorked[dayOfWeek] += hoursOut - hoursIn;

		      // Convert to base 10 for calculations
		      minutes = (minutesOut / 60.0) - (minutesIn / 60.0);

		      // If minutes is negative, "borrow" 1 from hours
		      // And/Or add to totalHoursWorked
		      if (minutes < 0) 
		      {
		        this.totalHoursWorked[dayOfWeek]--;
		        minutes = 1.0 + minutes;
		        this.totalHoursWorked[dayOfWeek] += minutes;
		      } 
		      else 
		      {
		        this.totalHoursWorked[dayOfWeek] += minutes;
		      }
		    }
		    calculateTotalHoursInAWeek();
		  }

		  // Calculates the hours worked each day in a week
		  private void calculateTotalHoursInAWeek() 
		  {
		    totalHoursWorked[7] = 0;
		    
		    for (int i = 0; i < totalHoursWorked.length - 1; i++) 
		    {
		      totalHoursWorked[7] += totalHoursWorked[i];
		    }
		  }

		  // Calculates all login/out times for the week and totals
		  private void calculateAllHoursInAWeek(Timesheet timesheet) 
		  {
		    for (int i = 0; i < 7; i++) 
		    {
		      calculateHoursInADay(timesheet, i);
		    }
		  }
}
