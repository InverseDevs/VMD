import React from 'react';
import './index.css';
import TabList from './tab-list';
import ChatInfo from './chat-info';
import Chat from './chat';

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