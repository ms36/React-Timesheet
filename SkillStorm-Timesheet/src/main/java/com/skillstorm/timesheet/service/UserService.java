package com.skillstorm.timesheet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skillstorm.timesheet.beans.User;
import com.skillstorm.timesheet.data.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}
	
	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User login(User user) {
		System.out.println("UserService");
		System.out.println(user);
				
		User temp = userRepository.findByUserName(user.getUserName());
		 
		if(user.getPassword().equals(temp.getPassword())) {
			temp.setLoggedIn(true);
			temp.setPassword("");
		}
								System.out.println(temp);
		return temp;
	}
}
