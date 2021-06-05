import React from "react";
import { Link } from 'react-router-dom';
import "./SideNav.css";
const SideNav = (props) => {

return (
   <div class="fine" style={{width: props.horizon.width, padding: props.horizon.padding}}>
      <button onClick={props.closeNav}>X</button>
      <h1>
         Welcome, {props.name}!
         </h1><ul class="separator">
         <li>< Link to='/tasklist' class="out">My Task List</Link></li>
         <li><Link to='/requestsub' class="out">Submit Link Request</Link></li>
         <li><Link to='/docstat' class="out">Document Status</Link></li>
         <li><Link to='/requestfax' class="out">Received Faxes</Link></li>
         <li><Link to='settings' class="out">Settings</Link></li>
         <li><a href='/' class="out">Logout</a></li>
         </ul>
   </div>
 );
};

export default SideNav;