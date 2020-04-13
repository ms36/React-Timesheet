package com.skillstorm.timesheet.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.timesheet.beans.DayOfWeek;

public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Integer>{

}
