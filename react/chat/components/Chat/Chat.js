import React from 'react';
import './Chat.css';
import MessageFrom from '../MessageFrom/MessageFrom';
import MessageTo from '../MessageTo/MessageTo';
import ChatInput from '../ChatInput/ChatInput';


class Chat extends React.Component {
    constructor(props){
        super(props);
        this.state={messages_number: 0,
            message_value: '',
            messages: []}; 
        // this.connection = new WebSocket('');
    }

    addMessage = () => {
        this.setState(({messages_number})=> ({
            messages_number: messages_number+1,
        }))};
    handleMessageChange = (event) => {
        this.setState({
            message_value:event.target.value,
        });
    }
    onMessageSubmit = (event) => {

        event.preventDefault();
    }
    
    componentDidMount() {
        // this.connection.onmessage = evt => { 
        //     // add the new message to state
        //     this.setState({
        //         messages : this.state.messages.concat([ evt.data ])
        //     })
        // };
        this.scrollToBottom();
    }
    componentDidUpdate(){
        // this.connection.send( Math.random() );
        this.scrollToBottom()
    }
    scrollToBottom = () => {
        this.el.scrollIntoView({behavior:"smooth"});
    }

    render(){
        
        return(
            <div className="chat-container">
                <div className="chat" id="slider-container">
                    
                        <div className="messages-container" id="for-slider" >
                            {[...Array(this.state.messages_number)].map(() => <MessageTo message={this.state.message_value}/>)}
                            <div style={{ float:"left", clear: "both" }}
                            ref={el => {this.el=el;}}>
                            </div>
                        </div>
                </div>
                <ChatInput addMessage={this.addMessage} handleMessageChange={this.handleMessageChange} onMessageSubmit={this.onMessageSubmit}/>
            </div>
        );
    }
}

export default Chat;