package com.skillstorm.timesheet.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "TIMESHEET")
public class Timesheet {
	
	@Id
	@Column(name = "TIMESHEET_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "WEEK_ENDING_DATE")
	private String weekEndingDate;
	
	@Column(name = "SUBMITTED")
	private boolean submitted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonBackReference(value = "userTimesheets")
	private User user;
	
	@OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "timesheetDayOfWeeks")
	@OrderBy("id")
	private List<DayOfWeek> dayOfWeeks;

	public Timesheet() {
		super();
	}

	public Timesheet(String weekEndingDate, User user) {
		super();
		this.weekEndingDate = weekEndingDate;
		this.submitted = false;
		this.user = user;
		this.dayOfWeeks = new ArrayList<DayOfWeek>();
		DayOfWeek dow1 = new DayOfWeek("Monday");
		dow1.setTimesheet(this);
		this.dayOfWeeks.add(dow1);
		DayOfWeek dow2 = new DayOfWeek("Tuesday");
		dow2.setTimesheet(this);
		this.dayOfWeeks.add(dow2);
		DayOfWeek dow3 = new DayOfWeek("Wednesday");
		dow3.setTimesheet(this);
		this.dayOfWeeks.add(dow3);
		DayOfWeek dow4 = new DayOfWeek("Thursday");
		dow4.setTimesheet(this);
		this.dayOfWeeks.add(dow4);
		DayOfWeek dow5 = new DayOfWeek("Friday");
		dow5.setTimesheet(this);
		this.dayOfWeeks.add(dow5);
		DayOfWeek dow6 = new DayOfWeek("Saturday");
		dow6.setTimesheet(this);
		this.dayOfWeeks.add(dow6);
		DayOfWeek dow7 = new DayOfWeek("Sunday");
		dow7.setTimesheet(this);
		this.dayOfWeeks.add(dow7);
		}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWeekEndingDate() {
		return weekEndingDate;
	}

	public void setWeekEndingDate(String weekEndingDate) {
		this.weekEndingDate = weekEndingDate;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<DayOfWeek> getDayOfWeeks() {
		return dayOfWeeks;
	}

	public void setDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
		this.dayOfWeeks = dayOfWeeks;
	}

	@Override
	public String toString() {
		return "Timesheet [id=" + id + ", weekEndingDate=" + weekEndingDate + ", submitted=" + submitted + ", user="
				+ user + "]";
	}

}
