import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Navbar, Nav, Form, Button,NavDropdown} from 'react-bootstrap';
import '../login.css'
import '../App.css';

class CostumRegister extends Component {

	registar(e){
		e.preventDefault();
		let u = document.getElementById("uName");
		let pw1 = document.getElementById("pw1");
		let pw2 = document.getElementById("pw2");
		if(pw1.value !== pw2.value){
			pw1.value = ""
			pw2.value = ""
			alert("Passwords dont match!")
		}else{
			localStorage.setItem("username", u.value);
			let obj = {
				"userName": u.value,
				"password":pw1.value,
				"amount": 0
			}
			console.log(obj)
			fetch("register",{
				headers: {
      				'Content-Type': 'application/json'
    			},
				method: "POST",
				body:JSON.stringify(obj)
			}).then((response) =>{
				return response.json
			}).then((json) =>{
				location.replace("/signin");
			}).catch((error)=>{alert(error)})
		}
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
		        <Form onSubmit={(e)=>this.registar(e)}>
				  <Form.Group >
				    <Form.Label>Email</Form.Label>
				    <Form.Control id="uName" type="email" placeholder="Enter Your Username" />
				  </Form.Group>

				  <Form.Group >
				    <Form.Label>Password</Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter Your Password" />
				  </Form.Group>
				  <Form.Group >
				    <Form.Label>Re-Enter Your password</Form.Label>
				    <Form.Control id="pw2" type="password" placeholder="Re-Enter Your Password" />
				  </Form.Group>
				  <center>
				  <Button variant="warning" type="submit" >
				    Regist
				  </Button>
				  </center>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumRegister;
