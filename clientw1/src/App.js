import React, { Component } from 'react';
import {BrowserRouter,Route,Switch} from 'react-router-dom';

import './App.css';

import CostumNavBar from './components/CostumNavBar';
import Home from './components/Home';
import CostumCreateMoney from './components/CostumCreateMoney';
import CostumTransferMoney from './components/CostumTransferMoney';
import CostumRegister from './components/CostumRegister';
import CostumLogin from './components/CostumLogin';
import All from './components/All';
import CostumSmartContract from "./components/CostumSmartContract";
import AllAuctions from "./components/AllAuctions";
import CloseAuction from "./components/CloseAuction";

class App extends Component {
  render() {
    return (
        <BrowserRouter>
            <Switch>
              <Route exact path="/">
                <CostumLogin />
              </Route>
              <Route path="/create">
                <CostumCreateMoney />
              </Route>
              <Route path="/transfer">
                <CostumTransferMoney />
              </Route>
              <Route path="/register">
                <CostumRegister />
              </Route>
              <Route path="/login">
                <CostumLogin />
              </Route>
              <Route path="/smartcontract">
                <CostumSmartContract/>
              </Route>
              <Route path="/openauctions">
                <AllAuctions/>
              </Route>
              <Route path="/closeauction">
                <CloseAuction/>
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
