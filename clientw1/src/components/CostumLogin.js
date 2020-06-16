import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Navbar, Nav, Form, Button, NavDropdown} from 'react-bootstrap';
import '../login.css'
import '../App.css';

class CostumLogin extends Component {

	login(e){
		e.preventDefault();
		let u = document.getElementById("uName");
		let pw1 = document.getElementById("pw1");
		fetch("/login",{
				headers: {
      				'Content-Type': 'application/json'
    			},
				method: "POST",
				body:JSON.stringify({username:u.value,password:pw1.value})})
		.then((response)=>{
			if(response.status >=400)
				return;
			localStorage.setItem("username", u.value);localStorage.setItem("auth",response.headers.get("authorization"));location.replace("http://localhost:3000/")})
		.catch((error)=>{pw1.value="";alert(error.text())})
	}
	
  render() {
    const bgPurple = {backgroundColor: '#7f4764'}
    return (
    	<div className="div2">
    		<Navbar expand="lg" style={bgPurple}>
			  <Navbar.Brand><img src="/nova-coin.png" width="40px" />&nbsp;&nbsp;<font style={{ color: 'green' }}><strong><b>NOVA</b></strong></font><font style={{ color: 'DarkGoldenRod' }}> <b>Crypto Banking Service</b></font></Navbar.Brand>
			  <Navbar.Toggle aria-controls="basic-navbar-nav" />
			  <Navbar.Collapse id="basic-navbar-nav">
			    <Nav className="mr-auto">
			      <Nav.Link href="/login" ><b>Login</b></Nav.Link>
			      <Nav.Link href="/register" ><b>Register</b></Nav.Link>
			    </Nav>
			  </Navbar.Collapse>
			</Navbar>
        	<div className="horizontalMargin40">
		        <img src="/nova-crypto-banking-service.png" width="380px" />
                <br /><br />
                <Form onSubmit={(e)=>this.login(e)}>
				  <Form.Group >
				    <Form.Label><b>E-MAIL</b></Form.Label>
				    <Form.Control id="uName" type="email" placeholder="Enter your username" />
				  </Form.Group>
				  <Form.Group >
				    <Form.Label><b>PASSWORD</b></Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter your password" />
				  </Form.Group>
				  <center>
				  <Button variant="warning" type="submit" >
				    <b>Login</b>
				  </Button>
				  </center>
				</Form>
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

export default CostumLogin;
