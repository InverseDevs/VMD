import React from 'react';
import ProfilePhoto from '../ProfilePhoto/ProfilePhoto'
import ProfileInfo from '../ProfileInfo/ProfileInfo';
import './ProfileHeader.css';

class ProfileHeader extends React.Component{
    constructor(props){
        super(props);
    }
    render() {
        return (
            <div className="profile-header">
                <ProfilePhoto />
                <ProfileInfo addPost={this.props.addPost}/>
            </div>
        );
    }
}

export default ProfileHeader;