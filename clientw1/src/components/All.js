import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {ListGroup} from 'react-bootstrap';
import '../login.css'
import '../App.css';

class All extends Component {
  constructor(props){
    super(props);
    this.state = {
      all: []
    }
  }

	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("/login");
    else{
      this.getAll();
    }
	}

  getAll(){
    fetch("/all",{headers: {'Content-Type': 'application/json',"authorization":localStorage.getItem("auth")},method:"GET"})
    .then((response)=>{return response.json()})
    .then((json)=>{console.log(json);this.setState({all:json})})
    .catch((error)=>{alert(error.text())})
  }

  render() {
    return (
    	<div className="div2">
    		<CostumNavBar/>
    		<div className="App horizontalOnlyMargin30">
                <img src="/nova-crypto-banking-service.png" width="380px" />
                <br /><br />
                <b>LIST OF ALL USERS REGISTERED IN THE SYSTEM</b>
                <br/> <br/>
                <ListGroup>
                {this.state.all.map((ell,key)=>{
                  return(
                    <ListGroup.Item key={key}><b>{ell.username + " has " + ell.amount + " NOVA Coins"}</b></ListGroup.Item>
                  )
                })}
                </ListGroup>
                <br /><br />
                <center>
                    <b><strong>CONTRIBUTORS</ strong></ b>:<br />
                    <b>Bruno Vicente dos Santos</ b><br />
                    <b>Filipe Miguel Santos</ b><br />
                <b>Rùben André Barreiro </ b>
                </center>
			</div>
		</div>
    );
  }
}

export default All;
