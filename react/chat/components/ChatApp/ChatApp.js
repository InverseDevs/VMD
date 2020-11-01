import React from 'react';
import './ChatApp.css';
import logo from '../../../images/logo_opacity.png';
import ChatContainer from '../ChatContainer/ChatContainer';
import {BrowserRouter, Link} from 'react-router-dom';
class ChatApp extends React.Component{
    render(){
        return (
                    <div className="chat-page">
                        <Link to="/"><img src={logo} id="chat-logo"/></Link>
                        <div className="chat-big-ellipse" id="chat-big-1"></div>
                        <div className="chat-big-ellipse" id="chat-big-2"></div>
                        <div className="chat-ellipse" id="chat-1"></div>
                        <div className="chat-ellipse" id="chat-2"></div>
                        <div className="chat-ellipse" id="chat-3"></div>
                        <div className="chat-ellipse" id="chat-4"></div>
                        <ChatContainer />
                    </div>
        );
    }
}

export default ChatApp;