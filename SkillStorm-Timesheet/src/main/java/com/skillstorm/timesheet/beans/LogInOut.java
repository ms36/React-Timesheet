package com.skillstorm.timesheet.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "LOG_IN_OUT")
public class LogInOut {
	
	@Id
	@Column(name = "LOG_IN_OUT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "LOG_IN")
	private String logIn;
	
	@Column(name = "LOG_OUT")
	private String logOut;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAY_OF_WEEK_ID")
	@JsonBackReference(value = "dayOfWeekLogInOuts")
	private DayOfWeek dayOfWeek;
	
	public LogInOut() {
		super();
	}
	
	public LogInOut(String logIn, String logOut) {
		super();
		this.logIn = logIn;
		this.logOut = logOut;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogIn() {
		return logIn;
	}

	public void setLogIn(String logIn) {
		this.logIn = logIn;
	}

	public String getLogOut() {
		return logOut;
	}

	public void setLogOut(String logOut) {
		this.logOut = logOut;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public String toString() {
		return "LogInOut [id=" + id + ", logIn=" + logIn + ", logOut=" + logOut + "]";
	}
}
