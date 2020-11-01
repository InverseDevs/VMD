import React from 'react';
import './ChatGroup.css';

const ChatGroup = ({addTab}) => {
    return(
        <div className="chat-group">
            <div onClick={addTab} className="group-image"></div>
            <p className="group-name">Group Name</p>
        </div>
    );
}

export default ChatGroup;