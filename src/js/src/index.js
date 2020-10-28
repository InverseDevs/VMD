import React from 'react';
import ReactDOM from 'react-dom';
import ChatApp from './chat-app';
import {BrowserRouter, Route} from 'react-router-dom';
class App extends React.Component{
    render(){
        return (
            <ChatApp />
        );
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('root')
);