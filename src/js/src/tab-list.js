import React from 'react';
import './index.css';
import Tab from './tab';
const TabList = ({tabs,closeTab}) => {
    return (
        <div className="tab-list">
            {[...Array(tabs)].map(()=> <Tab closeTab={closeTab}/>)}
        </div>
    )
} 


export default TabList;