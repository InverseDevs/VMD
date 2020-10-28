import React from 'react';
import './index.css';
import addBtn from './images/add_chat.png'
const ChatAdd = ({addGroup}) => {
    return (
        <button onClick={addGroup} id="add-btn"><img src={addBtn}  id="add-btn-img"></img></button>
    );
}

export default ChatAdd;