import React, { Component } from 'react';
import {Navbar, Nav, Form, Button} from 'react-bootstrap';
import '../login.css';
import '../App.css';

class CostumNavBar extends Component {

  constructor(props){
    super(props);
    this.state = {
      money : 0,
      username: localStorage.getItem("username"),
      amount: 0
    }
  }

  componentDidMount(){
    if(localStorage.getItem("username")==="")
      location.replace("/login")
    else{
      this.update()
    }
  }

  update(){
     fetch("amount?who="+localStorage.getItem("username"),{headers:{"authorization":localStorage.getItem("auth")}}).then((response)=>{
        return response.text()
      }).then((text) =>this.setState({amount:text}))
      .catch((error)=>{alert(error.text());location.replace("/login")})
  }

  logout(e){
    e.preventDefault();
    localStorage.clear();
    fetch("/logout");
    window.location="/login";
  }

  render() {
    const bgPurple = {backgroundColor: '#7f4764'}
    return (
      <Navbar expand="lg" style={bgPurple}>
        <Navbar.Brand><img src="/nova-coin.png" width="40px" />&nbsp;&nbsp;<font style={{ color: 'green' }}><strong><b>NOVA</b></strong></font><font style={{ color: 'DarkGoldenRod' }}> <b>Crypto Banking Service</b>&nbsp;&nbsp;&nbsp;&nbsp;</font></Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
    
        <Navbar.Brand>Welcome, <b>{this.state.username}</b></Navbar.Brand>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <Nav className="mr-auto">
          <Nav.Link href="create"><b>Create Money</b></Nav.Link>
          <Nav.Link href="transfer"><b>Transfer Money</b></Nav.Link>
          <Nav.Link href="all"><b>List of All Users</b></Nav.Link>
          <Nav.Link href="smartcontract"><b>Smart Contract</b></Nav.Link>
        </Nav>
        <Form inline>
          <Nav.Link><font style={{ color: 'DarkGoldenRod' }}><b><strong>Balance:</strong> {this.state.amount} NOVA Coins</b></font></Nav.Link>
          <Button onClick={(e)=>this.update(e)}variant="outline-dark"><b>Update</b></Button>&nbsp;&nbsp;
          <Button onClick={(e)=>this.logout(e)}variant="outline-dark"><b>Logout</b></Button>
        </Form>
      </Navbar>
    );
  }
}

export default CostumNavBar;
