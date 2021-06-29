import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import ReactDOM from 'react-dom';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {Routes} from './Routes'
import firebase from "firebase/app";
import firebaseConfig from "./noaccess/noaccess.js"
firebase.initializeApp(firebaseConfig);

ReactDOM.render(
  <React.StrictMode>
    <Router>
    <Routes />
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
