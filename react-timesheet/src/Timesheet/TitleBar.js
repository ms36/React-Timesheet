import React from 'react';
import skillstormHeader from './Assets/skillstorm_image.jpg';

function TitleBar(props) {
    return (
      <div className="header">
        <img className="header-image" src={skillstormHeader} alt=""/>   
        <button className="button-logout" onClick={() => {props.logout();}}>Log Out</button>      
      </div>
    );
  }
  
  export default TitleBar;