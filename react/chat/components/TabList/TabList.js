import React from 'react';
import './TabList.css';
import Tab from '../Tab/Tab';
const TabList = ({tabs,closeTab}) => {
    return (
        <div className="tab-list">
            {[...Array(tabs)].map(()=> <Tab closeTab={closeTab}/>)}
        </div>
    )
} 


export default TabList;