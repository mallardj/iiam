import React from 'react';
import "./index.css";
import { withRouter, useHistory} from 'react-router-dom';

const { MongoClient } = require('mongodb');

function TLR (props) {
    const history = useHistory();
    return <tr onClick={() => history.push("/tasklist/fulfill", {case: props.case})}>
      <td>{props.case.name}</td>
      <td>{props.case.org}</td>
      <td>{props.case.info}</td>
      <td>{props.case.date}</td>
    </tr>;
}

class TLT extends React.Component {
  constructor (props) {
    super(props);
    const uri = "mongodb+srv://admin:cWYHElXqPvRqn5UY@cluster0.vcqzz.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
    const client = MongoClient(uri);
    // add stuff from database.
    
    this.elements = [];
    for (let i = 0; i < 5; i ++) {
      this.elements.push(
        {
          name: "Mrs. " + i,
          org: "Organization on " + i + 'th street',
          info: "Summary from Hospital " + i,
          date: i + "/" + i + "/2021"
        }
      )
    }




  }
  render () {
    return <table>
    <th>
      <td>Patient Name</td>
      <td>Requesting Organization</td>
      <td>Requested Information</td>
      <td>Request Date</td>
    </th>
    <tr>
    <TLR case={this.elements[0]} />
    </tr>
    <tr>
    <TLR case={this.elements[1]} />
    </tr>
    <tr>
    <TLR case={this.elements[2]} />
    </tr>
    <tr>
    <TLR case={this.elements[3]} />
    </tr>
    <tr>
    <TLR case={this.elements[4]} />
    </tr>
    
  </table>;
  }
}

export class TL extends React.Component {

  render () {
    return (<div>
    <TLT></TLT>
  </div>);
  } 
}

export default withRouter(TL);