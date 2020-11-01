import React from 'react';
import ProfilePost from '../ProfilePost/ProfilePost';
import './ProfilePosts.css';
class ProfilePosts extends React.Component {
    constructor(props){
        super(props);
    }
    render() {
        return(
        <div className="profile-posts">
            {[...Array(this.props.posts)].map(() => <ProfilePost />)}
        </div>
    );}
}

export default ProfilePosts;