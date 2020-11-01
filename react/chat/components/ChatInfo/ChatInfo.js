import React from 'react';
import './ChatInfo.css';

class ChatInfo extends React.Component{
    render(){
        return (
            <div className="chat-info">
                <div className="chat-img"></div>
                <p className="chat-info-name">Chat Name</p>
            </div>
        )
    }
}
export default ChatInfo;