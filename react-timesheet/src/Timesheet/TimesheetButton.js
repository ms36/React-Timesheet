import React, {Component} from 'react'
// import './Timesheet.css';


class TimesheetButton extends  Component {       

    render () {
        return(
            <button onClick={this.props.buttonPressed} className={this.props.className}>{this.props.children}</button>
        )
    }
}
export default TimesheetButton;