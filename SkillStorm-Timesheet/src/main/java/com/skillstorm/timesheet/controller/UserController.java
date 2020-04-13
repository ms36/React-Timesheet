package com.skillstorm.timesheet.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.timesheet.beans.User;
import com.skillstorm.timesheet.service.UserService;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Optional<User>> findById(@PathVariable int id) {	
		return new ResponseEntity<Optional<User>>(userService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/name/{userName}")
	public ResponseEntity<User> findByUserName(@PathVariable String userName) {	
		return new ResponseEntity<User>(userService.findByUserName(userName), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<User>> findAll() {
		return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<User> login(@Valid @RequestBody User user) {
		User temp = userService.login(user);
		
		if(temp != null) {			
			return new ResponseEntity<>(temp, HttpStatus.OK);			
		} 
		else {			
			return new ResponseEntity<>(temp, HttpStatus.NOT_FOUND);			
		}
	}
}
