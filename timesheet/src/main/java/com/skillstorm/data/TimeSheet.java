package com.skillstorm.data;

import java.util.Arrays;

public class TimeSheet 
{
	private int timeSheetId;
    private boolean submitted;
    
    // The date of the last day of the week (Sunday)
    private String weekEndingDate;
    public User user = new User();
    
    // Holds the hours for each day of the week
    // starting from Monday ending on Sunday
    private double[] weekDayHours = new double[7];
     
    public TimeSheet(){
    	super();
    }

	public TimeSheet(int timeSheetId, double[] weekDayHours, boolean subitted, String weekEndingDate, User user) {
		super();
		this.timeSheetId = timeSheetId;
		this.weekDayHours = weekDayHours;
		this.submitted = submitted;
		this.weekEndingDate = weekEndingDate;
		this.user = user;
	}

	public int getTimeSheetId() {
		return timeSheetId;
	}

	public void setTimeSheetId(int timeSheetId) {
		this.timeSheetId = timeSheetId;
	}

	// Returns the whole week
	public double[] getWeekDayHours() {
		return weekDayHours;
	}
	
	// Returns the day of the week
	public double getWeekDayHours(int element)
	{
		return this.weekDayHours[element];
	}
	
    // Sets the whole week
	public void setWeekDayHours(double[] weekDayHours) {
		this.weekDayHours = weekDayHours;
	}

	// Sets the day of the week
	public void setWeekDayHours(int element, double weekDayHours) {
		if(weekDayHours < 0)
		{
			weekDayHours = 0;
		}
		if(weekDayHours > 20)
		{
			weekDayHours = 20;
		}
		this.weekDayHours[element] = weekDayHours;
	}
	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean subitted) {
		this.submitted = subitted;
	}

	public String getWeekEndingDate() {
		return weekEndingDate;
	}

	public void setWeekEndingDate(String weekEndingDate) {
		this.weekEndingDate = weekEndingDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + timeSheetId;
		result = prime * result + (submitted ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + Arrays.hashCode(weekDayHours);
		result = prime * result + ((weekEndingDate == null) ? 0 : weekEndingDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSheet other = (TimeSheet) obj;
		if (timeSheetId != other.timeSheetId)
			return false;
		if (submitted != other.submitted)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (!Arrays.equals(weekDayHours, other.weekDayHours))
			return false;
		if (weekEndingDate == null) {
			if (other.weekEndingDate != null)
				return false;
		} else if (!weekEndingDate.equals(other.weekEndingDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeSheet [timeSheetId=" + timeSheetId + ", weekDay=" + Arrays.toString(weekDayHours)
				+ ", timeSheetSubitted=" + submitted + ", weekEnding=" + weekEndingDate + ", user=" + user + "]";
	}
	       
}
