import React,{ Component } from "react";

class TimesheetDetail extends Component {

    constructor(props) {
        super(props);
    
        this.state = {
            /*
            * Need to change this to dynamically get the user id
            */
            userId: 1,
            isSaved: false,
            isSubmitted: props.timesheet.submitted,
            timesheetHours: [0, 0, 0, 0, 0, 0, 0, 0],
            timesheet: props.timesheet
        };
      }

    sendData = () => {this.props.parentCallback(this.props.index);}

    buttonAction = (event) => {                
        switch(event.target.className) {
            case 'button-minimize': 
                this.sendData(); 
            break;            
            case 'button-save': 
                this.setState({isSaved: true}); 
                this.saveTimesheet(); 
            break;
            case 'button-submit': 
                let tempTimesheet = this.state.timesheet;
                tempTimesheet.submitted = true;
                this.setState({isSaved: false, isSubmitted: true, timesheet: tempTimesheet}); 
                this.saveTimesheet(); 
            break;
            case 'button-repeat': 
                this.setHoursWithPreviousTimesheet();                
            break;
            default:
        }
    }

    // Calculates all login/out times for a day
    calculateHoursInADay(dayOfWeek) {

        let timesheetHoursWorked = this.state.timesheetHours;

        // Change has occured, make the user resave
        this.setState({isSaved: false});

        // Number of login/logout pairs
        const seriesCount = this.state.timesheet.dayOfWeeks[dayOfWeek].logInOuts.length;
        
        // Reset to recalculate
        timesheetHoursWorked[dayOfWeek] = 0;

        // Calculates each logIn/logOut in a day
        for (let i = 0; i < seriesCount; i++) {
            // Split time between hours/minutes
            const timeIn = this.state.timesheet.dayOfWeeks[dayOfWeek].logInOuts[i].logIn.split(':');
            const timeOut = this.state.timesheet.dayOfWeeks[dayOfWeek].logInOuts[i].logOut.split(':');    
            
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
            timesheetHoursWorked[dayOfWeek] += hoursOut - hoursIn;

            // Convert to base 10 for calculations
            minutes = (minutesOut / 60) - (minutesIn / 60);

            // If minutes is negative, "borrow" 1 from hours
            // And/Or add to totalHoursWorked
            if (minutes < 0) {
                timesheetHoursWorked[dayOfWeek]--;
                minutes = 1.0 + minutes;
                timesheetHoursWorked[dayOfWeek] += minutes;
            } else {
                timesheetHoursWorked[dayOfWeek] += minutes;
            }
        }
        // Adds hours and minutes to a total for a day
        // Plus rounds the decimal to 2 places
        timesheetHoursWorked[dayOfWeek] =
        Math.round(timesheetHoursWorked[dayOfWeek] * 100) / 100;

        this.setState({timesheetHours: timesheetHoursWorked});
        this.calculateTotalHoursInAWeek();
    }

    // Calculates the total hours worked each day in a week
    calculateTotalHoursInAWeek() {
        /*
        * totalHoursWorked length = 8
        * index 0 - 6 = Monday - Sunday
        * index 7 = each days' hours totaled for the week
        */
        let timesheetHoursWorked = this.state.timesheetHours;
        timesheetHoursWorked[7] = 0;

        for (let i = 0; i < timesheetHoursWorked.length - 1; i++) {
            timesheetHoursWorked[7] += timesheetHoursWorked[i];
        }
        // Rounds the decimal to 2 places
        timesheetHoursWorked[7] = Math.round(timesheetHoursWorked[7] * 100) / 100;
        this.setState({timesheetHours: timesheetHoursWorked});
    }

    // Calculates all login/out times for the week and totals
    calculateAllHoursInAWeek() {
        const daysInAWeek = 7;
        for (let i = 0; i < daysInAWeek; i++) {
            this.calculateHoursInADay(i);
        }
    }

  // Gets the previous timesheet's login/outs
  // and sets this timesheet with the same data,
  // then calculates them to be displayed
  setHoursWithPreviousTimesheet() {
    // Ensures it's not the first timesheet
    if (this.props.index - 1 > -1) {
      if (this.props.timesheets[this.props.index - 1] != null) {
        // Number of login/logout pairs
        const seriesCount = this.state.timesheet.dayOfWeeks[0].logInOuts.length;
        const daysInAWeek = 7;
        let tempTimesheet = this.state.timesheet;

        // Loops through all logIn/logOut times for a timesheet and
        // sets them to the previous timesheets logIn/logOut times
        for (let i = 0; i < daysInAWeek; i++) {
          for (let j = 0; j < seriesCount; j++) {
            tempTimesheet.dayOfWeeks[i].logInOuts[j] =
            this.props.timesheets[this.props.index - 1].dayOfWeeks[i].logInOuts[j];
          }
        }
        this.setState({timesheet: tempTimesheet});
        this.calculateAllHoursInAWeek();
      }
    }
  }

    // Sets the logIn/logOut time to user's input
    changeLogInOutTime = (day, column, isLogIn, event) => {      
        let time = event.target.value;    
        let tempTimesheet = this.state.timesheet;
        if(isLogIn) {
            tempTimesheet.dayOfWeeks[day].logInOuts[column].logIn = time;
        } else {
            tempTimesheet.dayOfWeeks[day].logInOuts[column].logOut = time;
        }
        
        this.setState({timesheet: tempTimesheet});
        this.calculateHoursInADay(day);    
    }

    // Saves timesheet to database
    saveTimesheet = () => {          
        fetch('/timesheet/user/id/' + this.state.userId, 
        {method: 'PUT', 
        headers:{'Content-Type': 'application/json'},
        body: JSON.stringify(this.state.timesheet)}
        );            
    };

    componentDidMount() {      
        this.calculateAllHoursInAWeek();
    }

    render() {
        return (     
            <table className="table-detailed">
                <thead>
                    <tr className="title-bar">
                        <th>{this.state.timesheet.weekEndingDate}</th>
                        <th>{this.state.isSubmitted ? "" : <button onClick={this.buttonAction} className="button-save">Save</button>}</th>
                        <th>{this.state.isSaved ? <button onClick={this.buttonAction} className="button-submit">Submit</button> : ""}</th>
                        <th className={this.state.isSubmitted ? "" : "not-submitted"} colSpan="2">{this.state.isSubmitted ? "" : "Not Submitted"}</th>          
                        <th></th>
                        <th>{this.state.isSubmitted ? "" : <button onClick={this.buttonAction} className="button-repeat">Repeat</button>}</th>
                        <th><button onClick={this.buttonAction} className="button-minimize">---</button></th>
                    </tr>
                    <tr className="row-description">
                        <th></th>
                        <th>Log In</th>
                        <th>Log Out</th>
                        <th>Log In</th>
                        <th>Log Out</th>
                        <th>Log In</th>
                        <th>Log Out</th>
                        <th>Total Hours</th>
                    </tr>
                </thead>        
                <tbody>                                        
                    {this.state.timesheet.dayOfWeeks.map((day, index) =>
                        <tr className={this.state.isSubmitted ? "row-no-input" : "row-input"} key = {index}>
                        <td>{day.name}</td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[0].logIn : 
                            <input 
                                type="time" 
                                value={day.logInOuts[0].logIn}
                                onChange={(event) => this.changeLogInOutTime(index, 0, true, event)}
                            />
                        }
                        </td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[0].logOut : 
                            <input 
                                type="time" 
                                defaultValue={day.logInOuts[0].logOut}
                                onChange={(event) => this.changeLogInOutTime(index, 0, false, event)}
                            />
                        }
                        </td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[1].logIn : 
                            <input 
                                type="time" 
                                defaultValue={day.logInOuts[1].logIn}
                                onChange={(event) => this.changeLogInOutTime(index, 1, true, event)}
                            />
                        }
                        </td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[1].logOut : 
                            <input 
                                type="time" 
                                defaultValue={day.logInOuts[1].logOut}
                                onChange={(event) => this.changeLogInOutTime(index, 1, false, event)}
                            />
                        }
                        </td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[2].logIn : 
                            <input 
                                type="time" 
                                defaultValue={day.logInOuts[2].logIn}
                                onChange={(event) => this.changeLogInOutTime(index, 2, true, event)}
                            />
                        }
                        </td>
                        <td>
                        {this.state.isSubmitted ? day.logInOuts[2].logOut : 
                            <input 
                                type="time" 
                                defaultValue={day.logInOuts[2].logOut}
                                onChange={(event) => this.changeLogInOutTime(index, 2, false, event)}
                            />
                        }
                        </td>
                        <td>{this.state.timesheetHours[index]}</td>
                        </tr>
                    )}                                                                 
                    
                    <tr className="row-total">
                        <td>Total</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>{this.state.timesheetHours[7]}</td>
                    </tr>
                </tbody>
            </table>
        )
    }
}

export default TimesheetDetail;