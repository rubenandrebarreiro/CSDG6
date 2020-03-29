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
			fetch("register",{
				method: "POST",
				body:{
					"userName": u,
					"passWord":pw1,
					"amount": 0
				}
			}).catch((response) =>{return response.json})
			.catch((json) =>{
				console.log(json)
			})
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
