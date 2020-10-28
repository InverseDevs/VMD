import React from 'react';
import './index.css';
const MessageTo = ({message}) => {
    return(
        <div className="message-to">
            <div className="user-to-info">
                <p className="user-to-name">UserTo Name</p>
                <div className="user-to-img"></div>
            </div>
            <div className="message-to-container">
                <p className="msg-to">{message}</p>
            </div>
        </div>
    );
}


export default MessageTo;