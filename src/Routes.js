import React from 'react';
import { TL } from './views/tasklist';
import { SAR } from './views/requestsub';
import { DS} from './views/docstat';
import { RF } from './views/recfax';
import { Settings } from './views/settings';
import { SideNav } from './components/SideNav/';
import { Route, Switch, Redirect } from 'react-router-dom';

export class Routes extends React.Component {
    constructor (props) {
        super(props);
        this.state = {
            width: "15%",
            padding: "5%",
        };
    }   
    render () {
  return (
    <div>
        <button onClick={() => this.setState({in: true})}>Open</button>
        <SideNav name="Yeehaw" horizon={this.state} closeNav={() => this.setState({in: false})}></SideNav>
      <Switch>
        <Route exact path="/tasklist" component={TL} />
        <Route exact path="/">
          <Redirect to="/tasklist" />
        </Route>
      <Route exact path="/requestsub" component={SAR} />
      <Route exact path="/docstat" component={DS} />
      <Route exact path="/requestfax" component={RF} />
      <Route exact path="/settings" component={Settings} />
      </Switch>
    </div>
  );
};
}