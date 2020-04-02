import React, { Component } from 'react';
import {BrowserRouter,Route,Switch} from 'react-router-dom';

import './App.css';

import CostumNavBar from './components/CostumNavBar';
import Home from './components/Home';
import CostumCreateMoney from './components/CostumCreateMoney';
import CostumTransfereMoney from './components/CostumTransfereMoney';
import CostumRegister from './components/CostumRegister';
import CostumLogin from './components/CostumLogin';
import All from './components/All';

class App extends Component {
  render() {
    return (
        <BrowserRouter>
            <Switch>
              <Route exact path="/">
                <Home />
              </Route>
              <Route path="/create">
                <CostumCreateMoney />
              </Route>
              <Route path="/transfere">
                <CostumTransfereMoney />
              </Route>
              <Route path="/register">
                <CostumRegister />
              </Route>
              <Route path="/signin">
                <CostumLogin />
              </Route>
            <Route path="/all">
              <All />
            </Route>
          </Switch>
        </BrowserRouter>
    );
  }
}

export default App;
