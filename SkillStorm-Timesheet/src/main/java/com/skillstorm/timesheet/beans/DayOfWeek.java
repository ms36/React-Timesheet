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
@Table(name = "DAY_OF_WEEK")
public class DayOfWeek {
	
	@Id
	@Column(name = "DAY_OF_WEEK_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "NAME")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIMESHEET_ID")
	@JsonBackReference(value = "timesheetDayOfWeeks")
	private Timesheet timesheet;
	
	@OneToMany(mappedBy = "dayOfWeek", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "dayOfWeekLogInOuts")
	@OrderBy("id")
	private List<LogInOut> logInOuts;
	
	public DayOfWeek() {
		super();
	}
	
	public DayOfWeek(String name) {
		super();
		this.name = name;
		this.logInOuts = new ArrayList<LogInOut>();
		LogInOut lio1 = new LogInOut("", "");
		lio1.setDayOfWeek(this);
		this.logInOuts.add(lio1);
		LogInOut lio2 = new LogInOut("", "");
		lio2.setDayOfWeek(this);
		this.logInOuts.add(lio2);
		LogInOut lio3 = new LogInOut("", "");
		lio3.setDayOfWeek(this);
		this.logInOuts.add(lio3);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<LogInOut> getLogInOuts() {
		return logInOuts;
	}

	public void setLogInOuts(List<LogInOut> logInOuts) {
		this.logInOuts = logInOuts;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	@Override
	public String toString() {
		return "DayOfWeek [id=" + id + ", name=" + name + "]";
	}
}
