import React, { Component } from 'react';
import skillstormLogo from './Assets/skillstormAlpha.png';

class LogIn extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
            user: { 
            userName: this.props.user.userName,
            password: this.props.user.password
            }
        }
    }

    inputChange = (event) => {
        let newUser = this.state.user;        
        switch(event.target.className) {            
            case 'login-username': 
            newUser.userName = event.target.value;                    
            break;

            case 'login-password': 
            newUser.password = event.target.value;             
            break;
            default:                
        }
        this.setState({user: newUser});
    }

    loginSubmit = () => {this.props.login(this.state.user);}

    keyPressed = (event) => {
        if(event.key === 'Enter') {
            this.loginSubmit();
        }
    }

    render() {
        return(
            <div className="login">                   
                <div>                                   
                    <p className="login-title">TIMESHEET</p>                     
                </div>
                <div>
                    <input 
                        className="login-username" 
                        placeholder="Username" 
                        autoFocus
                        value={this.state.user.userName}
                        onChange={this.inputChange}>                    
                    </input>
                </div>
                <div>
                    <input 
                        className="login-password" 
                        placeholder="Password" 
                        type="password"
                        value={this.state.user.password}
                        onKeyUp={this.keyPressed}
                        onChange={this.inputChange}>                        
                    </input>
                </div>
                <button 
                    className="button-login"
                    onClick={this.loginSubmit}                    
                    >Log In                    
                </button>
                <img className="login-logo" src={skillstormLogo} alt=""/> 
            </div>
        )
    }
}

export default LogIn;