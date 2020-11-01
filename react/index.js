import React from 'react';
import ReactDOM from 'react-dom';
import ChatApp from './chat/components/ChatApp/ChatApp';
import ProfileApp from './profile/components/ProfileApp/ProfileApp';
import {BrowserRouter, Route} from 'react-router-dom';
class App extends React.Component{
    render(){
        return (
            <BrowserRouter>
                <Route exact path="/chat" component={ChatApp}/>
                <Route exact path="/" component={ProfileApp}/>
            </BrowserRouter>
        );
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('root')
);