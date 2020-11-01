import React from 'react';
import './ProfilePost.css';
import like from '../../../images/like.svg';
class ProfilePost extends React.Component {
    render() {
        return(
            <div className="profile-post">
                <div className="profile-post-header">
                    <div className="profile-post-header-left">
                        <button className="profile-post-like"><img className="post-like" src={like}/></button>
                        <button className="profile-post-comments">Комментарии</button>
                    </div>
                    <div className="user-img"></div>
                </div>
                <div className="post-img"></div>
                <h6 className="post-text">Повседневная практика показывает, что новая модель организационной деятельности влечет за собой процесс внедрения и модернизации направлений прогрессивного развития.</h6>
                <button className="post-readmore">Подробнее</button>
            </div>
        );
    }
}

export default ProfilePost;