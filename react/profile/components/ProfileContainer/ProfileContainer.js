import React from 'react';
import ProfileHeader from '../ProfileHeader/ProfileHeader';
import ProfileFooter from '../ProfileFooter/ProfileFooter';
import './ProfileContainer.css';
class ProfileContainer extends React.Component {
    state = {
        postsNumber: 0,
    }
    addPost = () => {
        this.setState(({postsNumber})=> 
        ({postsNumber: postsNumber+1}))
    };
    render() {
        return(
        <div className="profile-container">
            <ProfileHeader addPost={this.addPost}/>
            <ProfileFooter posts={this.state.postsNumber}/>
        </div>
    );}
}

export default ProfileContainer;