import React from "react";
import Form from "react-bootstrap/Form";
import { useState } from "react";
import firebase from "firebase/app";
import "firebase/database";
import LoaderButton from "../../components/fancybutton/LoaderButton"
var https = require('https');

const firebaseConfig = {
    apiKey: "AIzaSyAdhI8wTwjlxFgqHFrFffcf2DaYkdbevOk",
    authDomain: "iiam-app.firebaseapp.com",
    databaseURL: "https://iiam-app.firebaseio.com",
    projectId: "iiam-app",
    storageBucket: "iiam-app.appspot.com",
    messagingSenderId: "299696792624",
    appId: "1:299696792624:web:6e5b99805d63b7492879f5",
    measurementId: "G-DRHX9NYQ7E"
  };

  
  function handleSubmission (user, pass) {
      
      const options = {
          hostname: 'https://iiam-app.firebaseio.com',
          method: 'GET',
          path: '/users/' + user
        }
        options.agent = new https.Agent(options);
        const req = https.request(options, (res) => {
            
            console.log(res);
        });
    }
    
    export default function Login (props) {
        function validateForm() {
        return (
            fields.email.length > 0 &&
            fields.password.length > 0 
        );
        }
        const [fields, handleFieldChange] = useState({
        username: "",
        password: "",
        confirmPassword: "",
        confirmationCode: "",
      });
    
    return <Form onSubmit={() => handleSubmission(fields.username, fields.password)}>
        
        <Form.Group controlId="username" size="lg">
          <Form.Label>Username</Form.Label>
          <Form.Control
            autoFocus
            type="username"
            value={fields.username}
            onChange={handleFieldChange}
          />
        </Form.Group>
        <Form.Group controlId="password" size="lg">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            value={fields.password}
            onChange={handleFieldChange}
          />
        </Form.Group>
        <input type="submit" value="Submit" />
    </Form>
}
