import React from 'react';
import ProfileContainer from '../ProfileContainer/ProfileContainer';
import './ProfileApp.css';
import logo from '../../../images/logo_opacity.png';

class ProfileApp extends React.Component{
    
    render(){
        return (
                    <div className="profile-page">
                        <a href="login-page.html"><img src={logo} id="profile-logo"/></a>
                        <div className="profile-big-ellipse" id="profile-big-1"></div>
                        <div className="profile-big-ellipse" id="profile-big-2"></div>
                        <div className="profile-ellipse" id="profile-1"></div>
                        <div className="profile-ellipse" id="profile-2"></div>
                        <div className="profile-ellipse" id="profile-3"></div>
                        <div className="profile-ellipse" id="profile-4"></div>
                        <ProfileContainer />
                    </div>
        );
    }
}

export default ProfileApp;