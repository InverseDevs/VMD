import React from 'react';
import './index.css';
const ChatInput = ({addMessage,handleMessageChange,onMessageSubmit}) => {
    return(
        <div className="chat-input">
            <form className="chat-form" onSubmit={onMessageSubmit}>
                <div className="chat-input-container">
                    <textarea type="text" placeholder="Type text here..."  onChange={handleMessageChange} className="msg-input"/>
                </div>
                <div onClick={addMessage} className="send-btn">
                    <input type="submit" className="msg-send" value=""/>
                </div>
            </form>
        </div>
    );
}


export default ChatInput;