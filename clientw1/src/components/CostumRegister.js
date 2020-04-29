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
			alert("Passwords don't match!")
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
				location.replace("/login");
			}).catch((error)=>{alert(error)})
		}
	}

  render() {
    const bgPurple = {backgroundColor: '#7f4764'}
    return (
    	<div className="div2">
    		<Navbar expand="lg" style= {bgPurple} >
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
		        <Form onSubmit={(e)=>this.registar(e)}>
				  <Form.Group >
				    <Form.Label><b>E-MAIL</b></Form.Label>
				    <Form.Control id="uName" type="email" placeholder="Enter your username" />
				  </Form.Group>

				  <Form.Group>
				    <Form.Label><b>PASSWORD</b></Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter your password" />
				  </Form.Group>
				  <Form.Group >
				    <Form.Label><b>RE-ENTER YOUR PASSWORD</b></Form.Label>
				    <Form.Control id="pw2" type="password" placeholder="Re-enter your password" />
				  </Form.Group>
				  <center>
				  <Button variant="warning" type="submit" >
				    <b>Register</b>
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

export default CostumRegister;
