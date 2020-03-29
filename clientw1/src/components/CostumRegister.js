import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumRegister extends Component {

	registar(){
		let u = document.getElementById("uName").value();
		let pw1 = document.getElementById("pw1").value();
		let pw2 = document.getElementById("pw2").value();
		console.log(u+" "+pw1+" "+pw2);
	}

  render() {
    return (
    	<div className="App">
    		<div className="App horizontalMargin30">
		        <Form>
				  <Form.Group controlId="formBasicEmail">
				    <Form.Label>Enter Your Username:</Form.Label>
				    <Form.Control id="uName" type="text" placeholder="Enter Your username" />
				  </Form.Group>

				  <Form.Group controlId="formBasicEmail">
				    <Form.Label>Enter Your password</Form.Label>
				    <Form.Control id="pw1" type="password" placeholder="Enter Your Password" />
				    <Form.Text className="text-muted">
				      To test
				    </Form.Text>
				  </Form.Group>
				  <Form.Group controlId="formBasicEmail">
				    <Form.Label>Re-Enter Your password</Form.Label>
				    <Form.Control id="pw2" type="password" placeholder="Re-Enter Your Password" />
				    <Form.Text className="text-muted">
				      To test
				    </Form.Text>
				  </Form.Group>
				  <Button variant="primary" type="submit" onClick={this.registar()}>
				    Regist
				  </Button>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumRegister;
