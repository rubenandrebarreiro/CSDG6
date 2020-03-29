import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Navbar, Nav, Form, Button,NavDropdown} from 'react-bootstrap';
import '../login.css'
import '../App.css';

class CostumLogin extends Component {

	login(e){
		e.preventDefault();
		let u = document.getElementById("uName");
		let pw1 = document.getElementById("pw1");
		fetch("/login?username="+u.value+"&password="+pw1.value,{method: "POST"})
		.then((response)=>{location.replace("http://localhost:3000/")})
		.catch((error)=>{pw1.value="";alert(error.text())})
	}
	
  render() {
    return (
    	<div className="div2">
    		<Navbar bg="warning" expand="lg">
			  <Navbar.Brand>Bank</Navbar.Brand>
			  <Navbar.Toggle aria-controls="basic-navbar-nav" />
			  <Navbar.Collapse id="basic-navbar-nav">
			    <Nav className="mr-auto">
			      <Nav.Link href="/login" >Sign-In</Nav.Link>
			      <Nav.Link href="/register" >Register</Nav.Link>
			    </Nav>
			  </Navbar.Collapse>
			</Navbar>
    		<div className="horizontalMargin40">
		        <Form onSubmit={(e)=>this.login(e)}>
				  <Form.Group >
				    <Form.Label>EMAIL</Form.Label>
				    <Form.Control id="uName" type="email" placeholder="Enter Your Username" />
				  </Form.Group>
				  <Form.Group >
				    <Form.Label>PASSWORD</Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter Your Password" />
				  </Form.Group>
				  <center>
				  <Button variant="warning" type="submit" >
				    Login
				  </Button>
				  </center>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumLogin;
