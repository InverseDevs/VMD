import React from 'react';
import ChatWindow from '../ChatWindow/ChatWindow';
import Chats from '../Chats/Chats';


class ChatContainer extends React.Component {
    state = {
        tabs_number: 0,
        groups_number: 0,
        
    };
    addTab = () => {
        this.setState(({tabs_number}) => ({
            tabs_number: tabs_number+1,
        }))};
    closeTab = () => {
        this.setState(({tabs_number}) => ({
            tabs_number: tabs_number-1,
        }))
    };
    addGroup = () => {
        this.setState(({groups_number}) => ({
            groups_number: groups_number+1,
        }))};
    render() {
        return(
        <div className="chat-container">
        <Chats groups={this.state.groups_number} addGroup={this.addGroup} addTab={this.addTab}/>
        <ChatWindow tabs={this.state.tabs_number} closeTab={this.closeTab}/>
        </div>
    );}
}

export default ChatContainer;