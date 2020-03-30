import React, { Component } from 'react';
import {BrowserRouter,Route,Switch} from 'react-router-dom';

import './App.css';

import CostumNavBar from './components/CostumNavBar';
import Home from './components/Home';
import CostumCreateMoney from './components/CostumCreateMoney';
import CostumTransfereMoney from './components/CostumTransfereMoney';
import CostumRegister from './components/CostumRegister';
import CostumLogin from './components/CostumLogin';

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
              <Route path="/login">
                <CostumLogin />
              </Route>
            </Switch>
        </BrowserRouter>
    );
  }
}

export default App;
