import React from "react";
import Form from "react-bootstrap/Form";
import { useState } from "react";
import firebase from "firebase/app";
// import admin from "firebase-admin";
var https = require('https');



  
  function handleSubmission (user, pass) {
        console.log(user, pass);
        const db = firebase.database();
        const snapshot = db.collection('users').get().then(
        () => snapshot.forEach((doc) => {
          console.log(doc.id, '=>', doc.data());
        }));
    }
    
    export default class Login extends React.Component {

      constructor (props) {
        super(props);
        this.state = {
          username: "",
          password: "",
        };
      }
      validateForm() {
        return (
            this.state.username.length > 0 &&
            this.state.password.length > 0 
        );
        }

    
    render () { return <div><form>
        
          <div>Username: <input
            name="username"
            value={this.state.username}
            onChange={(e) => {
              const yipe = e.target.name;
              this.setState({[yipe]: e.target.value});
            }}
          />
  </div>
  <div>
          <input
            name="password"
            value={this.state.password}
            onChange={(e) => {
              const yipe = e.target.name;
              this.setState({[yipe]: e.target.value});
            }}
          /></div>
    </form>
    <button onClick={() => handleSubmission(this.state.username, this.state.password)} >Submit</button></div>;} 

}
