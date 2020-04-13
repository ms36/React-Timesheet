package com.skillstorm.timesheet.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillstorm.timesheet.beans.Timesheet;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer>{
	
	List<Timesheet> findAllByUserId(int id);
	
	@Modifying
	@Query("UPDATE Timesheet SET Submitted = true WHERE weekEndingDate = ?1 AND user_Id = ?2")
	public void update(String weekEndingDate, int userId);
}
