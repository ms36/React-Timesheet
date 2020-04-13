package com.skillstorm.timesheet.service;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.timesheet.beans.Timesheet;
import com.skillstorm.timesheet.beans.User;
import com.skillstorm.timesheet.data.TimesheetRepository;
import com.skillstorm.timesheet.data.UserRepository;
import com.skillstorm.timesheet.utility.Email;
import com.skillstorm.timesheet.utility.ExcelSpreadSheet;

@Service
public class TimesheetService {
	
	@Autowired
	TimesheetRepository timesheetRepository;
	
	@Autowired
	UserRepository userRepository;
	
	private static final Logger log = Logger.getLogger(TimesheetService.class);

	
	public Optional<Timesheet> findById(int id) {
		return timesheetRepository.findById(id);
	}

	public List<Timesheet> findAllByUserId(int userId) {
		return timesheetRepository.findAllByUserId(userId);
	}
	
	public Timesheet newTimesheeet(Timesheet timesheet, int userId) {

		Optional<User> user = userRepository.findById(userId);
		timesheet.setUser(user.get());

		return timesheetRepository.save(new Timesheet(timesheet.getWeekEndingDate(), user.get()));
	}
	
	public Timesheet save(Timesheet timesheet, int userId) {
				
		// Get user info for this timesheet
		Optional<User> user = userRepository.findById(userId);
		timesheet.setUser(user.get());
	
		if (timesheet.isSubmitted()) {
			// Send an email with the excel file
			Email email = new Email(timesheet.getWeekEndingDate());
			email.sendEmail();
			
			log.info("Timesheet submitted");
		} else {
			// Save timesheet to an excel file
			ExcelSpreadSheet excel = new ExcelSpreadSheet();
			excel.update(timesheet);	
			
			log.info("Timesheet saved");
		}
		return timesheetRepository.save(timesheet);
	}
}
