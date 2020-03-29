import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
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
				console.log(json)
				location.replace("http://localhost:8080/login");
			}).catch((error)=>{console.err(error)})
		}
	}

  render() {
    return (
    		<div className="App horizontalMargin30">
		        <Form onSubmit={(e)=>this.registar(e)}>
				  <Form.Group >
				    <Form.Label>Enter Your Username:</Form.Label>
				    <Form.Control id="uName" type="text" placeholder="Enter Your Username" />
				  </Form.Group>

				  <Form.Group >
				    <Form.Label>Enter Your password</Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter Your Password" />
				  </Form.Group>
				  <Form.Group >
				    <Form.Label>Re-Enter Your password</Form.Label>
				    <Form.Control id="pw2" type="password" placeholder="Re-Enter Your Password" />
				  </Form.Group>
				  <Button variant="primary" type="submit" >
				    Regist
				  </Button>
				</Form>
			</div>
    );
  }
}

export default CostumRegister;
