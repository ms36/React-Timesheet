import React, {Component} from 'react'
import './Timesheet.css';
import TitleBar from './TitleBar';
import LogIn from './LogIn';
import TimesheetSimple from './TimesheetSimple';
import TimesheetDetail from './TimesheetDetail';
import logoBackground from './Assets/stormyCentered.png';

class Timesheet extends Component {
    state = {
        isLoading: true,
        isLoggedIn: false,
        isMinimized: [],
        timesheets: [], 
        user : {
            userName: "",
            password: ""
        }        
    }
       
    callbackFunction = (index) => {    
        const newIsMinimized = [...this.state.isMinimized];
        newIsMinimized[index] = !newIsMinimized[index];
        this.setState({ isMinimized: newIsMinimized });
    }

    login = (user) => {                        

        fetch('/users/login', 
        {method: 'POST', 
        headers:{'Content-Type': 'application/json'},
        body: JSON.stringify(user)}
        ).then(async aresponse => {
            if(aresponse.ok) {
                const userReturned = await aresponse.json();                
                this.setState({isLoggedIn: true, user: user});

                const response = await fetch('/timesheet/user/id/' + userReturned.id);
                const body = await response.json();
                
                this.setState({timesheets: body, isLoading: false});        
                
                for(let i = 0; i < this.state.timesheets.length; i++) {
                    this.setState({isMinimized: this.state.isMinimized.concat(true)});
                }              
            }
        });  
    }

    logout = () => {
        this.setState({isLoggedIn: !this.state.isLoggedIn})
    }

    render () {
        const {timesheets} = this.state;
                
        return (
            <React.Fragment>  
            <TitleBar logout={this.logout}></TitleBar>
            <img className="image-logo" src={logoBackground} alt=""/>  
            {this.state.isLoggedIn ? 
                timesheets.map((timesheet, index) =>   
                <div key = {index}>     
                <br></br>   
                {this.state.isMinimized[index] ?          
                    <TimesheetSimple 
                        timesheet ={timesheet} 
                        index={index} 
                        parentCallback = {this.callbackFunction}>
                    </TimesheetSimple>
                : <TimesheetDetail 
                        timesheet ={timesheet}
                        timesheets ={this.state.timesheets}                
                        index={index} 
                        parentCallback = {this.callbackFunction}>
                </TimesheetDetail>
                }                              
                </div>                                                   
                )  
                : <LogIn user = {this.state.user} login = {this.login}></LogIn> 
            }
            </React.Fragment>  
        )
    }

}

export default Timesheet;