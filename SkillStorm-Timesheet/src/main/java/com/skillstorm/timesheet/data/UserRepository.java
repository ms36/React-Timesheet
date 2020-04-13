package com.skillstorm.timesheet.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.timesheet.beans.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByUserName(String userName);

}
