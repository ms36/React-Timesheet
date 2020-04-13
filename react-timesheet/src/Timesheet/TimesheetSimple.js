import React, {Component} from 'react';

class TimesheetSimple extends Component {
    
    constructor(props) {
        super(props);
    
        this.state = {           
            timesheetHours: [0, 0, 0, 0, 0, 0, 0, 0],
            timesheet: props.timesheet            
        };
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

    sendData = () => {this.props.parentCallback(this.props.index);}
    
    componentDidMount() {      
        this.calculateAllHoursInAWeek();
    }

    render() {
        return (
            <table>                                                   
                <thead>
                    <tr className="title-bar">
                        <th>{this.props.timesheet.weekEndingDate}</th>
                        <th></th>
                        <th></th>
                        <th className={this.props.timesheet.submitted ? "" : "not-submitted"} colSpan="2">{this.props.timesheet.submitted ? "" : "Not Submitted"}</th>                                           
                        <th></th>
                        <th></th>
                        <th><button onClick={this.sendData} className="button-maximize">[_]</button></th>
                    </tr>
                    <tr className="row-description">
                        <th>Monday</th>
                        <th>Tuesday</th>
                        <th>Wednesday</th>
                        <th>Thursday</th>
                        <th>Friday</th>
                        <th>Saturday</th>
                        <th>Sunday</th>
                        <th>Total Hours</th>
                    </tr>
                </thead>
                <tbody>
                    <tr className="row-total">
                        <td>{this.state.timesheetHours[0]}</td>
                        <td>{this.state.timesheetHours[1]}</td>
                        <td>{this.state.timesheetHours[2]}</td>
                        <td>{this.state.timesheetHours[3]}</td>
                        <td>{this.state.timesheetHours[4]}</td>
                        <td>{this.state.timesheetHours[5]}</td>
                        <td>{this.state.timesheetHours[6]}</td>
                        <td>{this.state.timesheetHours[7]}</td>
                    </tr>
                </tbody>
            </table> 
        )  
    }
}

export default TimesheetSimple;