import React from 'react';
import ProfileAside from '../ProfileAside/ProfileAside';
import ProfilePosts from '../ProfilePosts/ProfilePosts';
import './ProfileFooter.css';
class ProfileFooter extends React.Component{
    constructor(props){
        super(props)
    }
    render() {
        return (
            <div className="profile-footer">
                <ProfileAside/>
                <ProfilePosts posts={this.props.posts}/>
            </div>
        );
    }
}

export default ProfileFooter;