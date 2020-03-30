import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumTransfereMoney extends Component {
	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("/login");
	}

	transferMoney(){
		let dest = document.getElementById("dest");
		let amount = document.getElementById("amount");
		if(amount.value > 0 && dest.value!=="")
			fetch("amount?from="+localStorage.getItem("username")+"&to="+dest.value,{headers: {'Content-Type': 'application/json'},method:"PUT",body : JSON.stringify({"amount":amount.value})})
			.then(window.location = "/")
			.catch((error)=>alert(error.text()))
		dest.value = "";
		amount.value = 0;
	}

  render() {
    return (
    	<div className="App">
    		<CostumNavBar/>
    		<div className="App horizontalMargin30">
		        <Form onSubmit={(e)=>this.createMoney(e)>
				  <Form.Group >
				    <Form.Label>Destination Username:</Form.Label>
				    <Form.Control id="dest"type="text" placeholder="Enter username" />
				  </Form.Group>

				  <Form.Group >
				    <Form.Label>Amount to generate:</Form.Label>
				    <Form.Control id="amount" type="number" placeholder="0" />
				  </Form.Group>
				  <Button variant="warning" type="submit">
				    Submit
				  </Button>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumTransfereMoney;
