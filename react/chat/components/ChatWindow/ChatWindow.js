import React from 'react';
import './ChatWindow.css';
import TabList from '../TabList/TabList';
import ChatInfo from '../ChatInfo/ChatInfo';
import Chat from '../Chat/Chat';

const ChatWindow = ({tabs,closeTab}) => {
    return(
        <div className="chat-window">
            <TabList tabs={tabs} closeTab={closeTab}/>
            <ChatInfo />
            <Chat />
        </div>
    );
}


export default ChatWindow;