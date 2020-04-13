package com.skillstorm.timesheet.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.timesheet.beans.Timesheet;
import com.skillstorm.timesheet.service.TimesheetService;

@RestController
@RequestMapping(value = "/timesheet")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class TimesheetController {
	
	private static final Logger log = Logger.getLogger(TimesheetController.class);
	
	@Autowired
	TimesheetService timesheetService;

	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Optional<Timesheet>> findById(@PathVariable int id) {	
		return new ResponseEntity<Optional<Timesheet>>(timesheetService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/id/{userId}")
	public ResponseEntity<List<Timesheet>> findAllByUserId(@PathVariable int userId) {
		log.info("finding...");
		return new ResponseEntity<List<Timesheet>>(timesheetService.findAllByUserId(userId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/user/id/{userId}")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<Timesheet> newTimesheeet (@Valid @RequestBody Timesheet timesheet, @PathVariable int userId) {
		return new ResponseEntity<Timesheet>(timesheetService.newTimesheeet(timesheet, userId), HttpStatus.OK);
	}
	
	@PutMapping(value = "/user/id/{userId}")
	public ResponseEntity<Timesheet> save(@Valid  @RequestBody Timesheet timesheet, @PathVariable int userId){
		return new ResponseEntity<Timesheet>(timesheetService.save(timesheet, userId), HttpStatus.OK);
		
	}
}
