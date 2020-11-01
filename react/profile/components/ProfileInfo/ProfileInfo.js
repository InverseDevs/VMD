import React from 'react';
import './ProfileInfo.css';

class ProfileInfo extends React.Component{
    constructor(props){
        super(props);
    }
    render() {
        return (
            <div className="profile-info">
                <div className="profile-info-header">
                    <h2 className="profile-username">Имя пользователя</h2>
                    <h4 className="profile-status">Статус</h4>
                </div>
                <div className="profile-info-footer">
                    <h3 className="profile-town">Город</h3>
                    <h3 className="profile-study">Место учёбы</h3>
                    <h3 className="profile-bday">День рождения</h3>
                </div>
                <div className="profile-btns">
                    <button className="profile-readmore">Показать подробнее</button>
                    <button onClick={this.props.addPost} className="profile-create-post">Оставить запись</button>
                </div>
            </div>
        );
    }
}

export default ProfileInfo;