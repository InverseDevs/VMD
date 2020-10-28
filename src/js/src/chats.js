import React from 'react';

import ChatGroup from './chat-group';
import ChatAdd from './chat-add';
import SimpleBarReact from 'simplebar-react';
import 'simplebar/src/simplebar.css';
import './index.css';

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