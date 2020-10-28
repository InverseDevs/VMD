import React from 'react';
import './index.css';
import logo from './images/logo_opacity.png';
import ChatContainer from './chat-container';
import {BrowserRouter, Route} from 'react-router-dom';

class ChatApp extends React.Component{
    render(){
        return (
            <BrowserRouter>
                <Route path="/">
                    <div className="chat-page">
                        <a href="chat.html"><img src={logo} id="chat-logo"/></a>
                        <div className="chat-big-ellipse" id="chat-big-1"></div>
                        <div className="chat-big-ellipse" id="chat-big-2"></div>
                        <div className="chat-ellipse" id="chat-1"></div>
                        <div className="chat-ellipse" id="chat-2"></div>
                        <div className="chat-ellipse" id="chat-3"></div>
                        <div className="chat-ellipse" id="chat-4"></div>
                        <ChatContainer />
                    </div>
                </Route>
            </BrowserRouter>
        );
    }
}

export default ChatApp;