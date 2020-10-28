import React from 'react';
import './index.css';
import close from './images/close.png';
const Tab = ({closeTab}) => {
    return(
        <div className="tab">
                <p className="chat-name">Chat Name</p>
                <div onClick={closeTab} className="close-div">
                    <img src={close} className="close-icon" />
                </div>
        </div>
    );
}


export default Tab;