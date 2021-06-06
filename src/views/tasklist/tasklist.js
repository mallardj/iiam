import React from 'react';
import "./index.css";

class TLR extends React.Component {
  constructor (props) {
    super(props);
    this.case = props.case;
  }
  render () {
    return <tr>
      <td>{this.case.name}</td>
      <td>{this.case.org}</td>
      <td>{this.case.info}</td>
      <td>{this.case.date}</td>
    </tr>;
  }
}

class TLT extends React.Component {
  constructor (props) {
    super(props);
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
  constructor (props) {
    super(props);
    
  }
  render () {
    return (<div>
    <TLT></TLT>
  </div>);
  } 
}

export default TL;