import React from 'react';
import './MessageFrom.css';

class MessageFrom extends React.Component {
    render() {
        return(
            <div className="message-from">
                <div className="user-from-info">
                    <div className="user-from-img"></div>
                    <p className="user-from-name">UserFrom Name</p>
                </div>
                <div className="message-from-container">
                    <p className="msg-from">Message</p>
                </div>
            </div>
        );
    }
}

export default MessageFrom;