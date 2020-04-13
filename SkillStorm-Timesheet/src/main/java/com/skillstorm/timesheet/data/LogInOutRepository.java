package com.skillstorm.timesheet.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.timesheet.beans.LogInOut;

public interface LogInOutRepository extends JpaRepository<LogInOut, Integer>{

}
