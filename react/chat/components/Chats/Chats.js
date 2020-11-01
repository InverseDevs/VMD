import React from 'react';

import ChatGroup from '../ChatGroup/ChatGroup';
import ChatAdd from '../ChatAdd/ChatAdd';
import SimpleBarReact from 'simplebar-react';
import 'simplebar/src/simplebar.css';
import './Chats.css';

const Chats = ({groups,addGroup,addTab}) => {
    
    return (
        
        <div className="chats">
            <SimpleBarReact style={{maxHeight: 700}}>
                <div className="chat-group-container">
                    <div className="chat-group-scroll">
                        {[...Array(groups)].map(()=> <ChatGroup addTab={addTab}/>)}
                    </div>
                </div>
            </SimpleBarReact>
            <ChatAdd addGroup={addGroup}/>
        </div>
    );
}


export default Chats;