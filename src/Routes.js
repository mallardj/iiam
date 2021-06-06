import React from 'react';
import { TL }from './views/tasklist';
import { SAR } from './views/requestsub';
import { DS} from './views/docstat';
import { RF } from './views/recfax';
import { Settings } from './views/settings';
import { SideNav } from './components/SideNav/';
import { Route, Switch, Redirect } from 'react-router-dom';
import menuicon from "./assets/icons/Hamburger_icon.svg";
import { Fade } from 'react-bootstrap';

export class Routes extends React.Component {
    constructor (props) {
        super(props);
        this.state = {
            width: 20,
            padding: 2.5,
        };
    }   
    render () {
  return (
    <div>
        
        <div><button 
          onClick={() => this.setState({width: 20, padding: 2.5})}><div><img
            src={menuicon}
            alt="Menu"
            height="20"
            width="20"
          />
            </div></button>
            <SideNav name="Clarissa" horizon={this.state} closeNav={() => this.setState({width: 0, padding: 0})}></SideNav></div>
        <div style={{
          paddingLeft: (this.state.width + 2 * this.state.padding) + "%", 
          width: (100 - this.state.width) + "%"}}>
        <Fade><Switch>
        <Route path="/tasklist" ><p>*The following is a list of medical information requested from you and/or your practice. Please click on the patient’s name to submit the appropriate records and complete the task.</p><TL/></Route>
        <Route exact path="/">
          <Redirect to="/tasklist" />
        </Route>
      <Route exact path="/requestsub" component={SAR} />
      <Route exact path="/docstat" component={DS} />
      <Route exact path="/requestfax" component={RF} />
      <Route exact path="/settings" component={Settings} />
      </Switch></Fade>
        </div>

    </div>
  );
};
}