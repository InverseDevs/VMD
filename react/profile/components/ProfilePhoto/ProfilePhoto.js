import React from 'react';
import './ProfilePhoto.css';

class ProfilePhoto extends React.Component{
    render() {
        return (
            <div className="profile-photo-container">
                <div className="profile-photo"></div>
                <button className="profile-btn">Изменить</button>
            </div>
        );
    }
}

export default ProfilePhoto;