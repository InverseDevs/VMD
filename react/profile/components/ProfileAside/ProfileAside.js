import React from 'react';
import './ProfileAside.css';
import {BrowserRouter, Link} from 'react-router-dom';
class ProfileInfo extends React.Component{
    render() {
        return (
            <div className="profile-aside">
                <button className="profile-friends">Друзья</button>
                <Link to="/chat" ><button className="profile-chats">Чаты</button></Link>
                <button className="profile-groups">Группы</button>
                <button className="profile-music">Музыка</button>
            </div>
        );
    }
}

export default ProfileInfo;