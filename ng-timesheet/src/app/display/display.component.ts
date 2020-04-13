import { TimesheetHours } from './../../models/timesheetHours';
import { User } from './../../models/user';
import { Timesheet } from './../../models/timesheet';
import { DisplayService } from './../display.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.css']
})
export class DisplayComponent implements OnInit {

  userId: number;
  timesheets: Timesheet[];
  timesheetHours: TimesheetHours[] = [];
  isTimesheetMinimized = [];
  isTimesheetSaved = [];
  isTimesheetSubmitted = [];

  constructor(private displayService: DisplayService) { }

  ngOnInit() {
    this.userId = 1; // TODO need to change this to get the actual user id
    this.getAllTimesheetsByUserId(this.userId);

    window.addEventListener('DOMContentLoaded', (event) => {

      console.log('DOM fully loaded and parsed');
  });

  }

  // Used to minimize/maximize the timesheet details
  toggleTimesheetDetail(timesheetIndex: number) {
    this.isTimesheetMinimized[timesheetIndex] = !this.isTimesheetMinimized[timesheetIndex];
  }

  // Calculates all login/out times for a day
  calculateHoursInADay(timesheetIndex: number, dayOfWeek: number) {

    // Change has occured, make the user resave
    this.isTimesheetSaved[timesheetIndex] = false;

    // Number of login/logout pairs
    const seriesCount = this.timesheets[timesheetIndex].dayOfWeeks[dayOfWeek].logInOuts.length;

    // Reset to recalculate
    this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] = 0;

    for (let i = 0; i < seriesCount; i++) {
      // Split time between hours/minutes
      const timeIn = this.timesheets[timesheetIndex].dayOfWeeks[dayOfWeek].logInOuts[i].logIn.split(':');
      const timeOut = this.timesheets[timesheetIndex].dayOfWeeks[dayOfWeek].logInOuts[i].logOut.split(':');

      // Convert from string to number
      let hoursIn = parseInt(timeIn[0], 10);
      let hoursOut = parseInt(timeOut[0], 10);
      let minutesIn = parseInt(timeIn[1], 10);
      let minutesOut = parseInt(timeOut[1], 10);
      let minutes = 0.0;

      // Ensure there is a number to calculate
      if (isNaN(hoursOut)) {
        hoursOut = 0;
      }
      if (isNaN(hoursIn)) {
        hoursIn = 0;
      }
      if (isNaN(minutesOut)) {
        minutesOut = 0;
      }
      if (isNaN(minutesIn)) {
        minutesIn = 0;
      }
      // Adds the hours for the login/out to a total for a day
      this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] += hoursOut - hoursIn;

      // Convert to base 10 for calculations
      minutes = (minutesOut / 60) - (minutesIn / 60);

      // If minutes is negative, "borrow" 1 from hours
      // And/Or add to totalHoursWorked
      if (minutes < 0) {
        this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek]--;
        minutes = 1.0 + minutes;
        this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] += minutes;
      } else {
        this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] += minutes;
      }
    }
    // Adds hours and minutes to a total for a day
    // Plus rounds the decimal to 2 places
    this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] =
      Math.round(this.timesheetHours[timesheetIndex].totalHoursWorked[dayOfWeek] * 100) / 100;

    this.calculateTotalHoursInAWeek(timesheetIndex);
  }

  // Calculates the hours worked each day in a week
  calculateTotalHoursInAWeek(timesheetIndex: number) {
    /*
    * totalHoursWorked length = 8
    * index 0 - 6 = Monday - Sunday
    * index 7 = each days' hours totaled for the week
    */
    this.timesheetHours[timesheetIndex].totalHoursWorked[7] = 0;

    for (let i = 0; i < this.timesheetHours[timesheetIndex].totalHoursWorked.length - 1; i++) {
      this.timesheetHours[timesheetIndex].totalHoursWorked[7] +=
       this.timesheetHours[timesheetIndex].totalHoursWorked[i];
    }
  }

  // Calculates all login/out times for the week and totals
  calculateAllHoursInAWeek(timesheetIndex: number) {
    const daysInAWeek = 7;
    for (let i = 0; i < daysInAWeek; i++) {
      this.calculateHoursInADay(timesheetIndex, i);
    }
  }

  getAllTimesheetsByUserId(userId: number): void {
    this.displayService.getAllTimesheetsByUserId(userId)
    .subscribe(
      timesheet => this.timesheets = timesheet,
      error => console.log('Error: ', error),
      () => {
        for (let i = 0; i < this.timesheets.length; i++) {
          // Initializes the arrays and sets their
          // lengths equal to the number of timesheets
          this.isTimesheetMinimized.push(true);
          this.isTimesheetSaved.push(false);
          this.isTimesheetSubmitted.push(this.timesheets[i].submitted);
          this.timesheetHours.push(new TimesheetHours());

          this.calculateAllHoursInAWeek(i);
        }
      });
  }

  // Gets the previous timesheet's login/outs
  // and sets this timesheet with the same data,
  // then calculates them to be displayed
  setHoursWithPreviousTimesheet(timesheetIndex: number) {
    // Ensures it's not the first timesheet
    if (timesheetIndex - 1 > -1) {
      if (this.timesheets[timesheetIndex - 1] != null) {
        // Number of login/logout pairs
        const seriesCount = this.timesheets[timesheetIndex].dayOfWeeks[0].logInOuts.length;
        const daysInAWeek = 7;

        for (let i = 0; i < daysInAWeek; i++) {
          for (let j = 0; j < seriesCount; j++) {
            this.timesheets[timesheetIndex].dayOfWeeks[i].logInOuts[j] =
            this.timesheets[timesheetIndex - 1].dayOfWeeks[i].logInOuts[j];
          }
        }
        this.calculateAllHoursInAWeek(timesheetIndex);
      }
    }
  }

  saveTimesheet(timesheet: Timesheet, userId: number, timesheetIndex: number) {
    this.isTimesheetSaved[timesheetIndex] = true;
    console.log('Timesheet saved');
    this.displayService.saveTimesheet(timesheet, userId).subscribe();
  }

  submitTimesheet(timesheet: Timesheet, userId: number, timesheetIndex: number) {
    this.isTimesheetSubmitted[timesheetIndex] = true;
    this.isTimesheetSaved[timesheetIndex] = false;
    timesheet.submitted = true;
    console.log('Timesheet submitted');
    this.displayService.saveTimesheet(timesheet, userId).subscribe();
  }
}
