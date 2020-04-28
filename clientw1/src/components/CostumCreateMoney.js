import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumCreateMoney extends Component {
	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("/login");
	}

	createMoney(e){
		e.preventDefault();
		let money = document.getElementById("money");
		if(money.value > 0)
			fetch("money?who="+localStorage.getItem("username"),{headers: {'Content-Type': 'application/json',"authorization":localStorage.getItem("auth")},method:"PUT",body : JSON.stringify({"amount":amount.value})})

			//fetch("money?who="+localStorage.getItem("username"),{headers: {'Content-Type': 'application/json',"authorization":localStorage.getItem("auth")},method:"PUT",body : JSON.stringify({"amount":parseFloat(money.value)})})
			.then(window.location = "/")
			.catch((error)=>alert(error.text()))
		money.value = 0;
	}
  render() {
    return (
    	<div>
    		<CostumNavBar/>
    		<div className="App horizontalMargin30">
		        <Form onSubmit={(e)=>this.createMoney(e)}>
				  <Form.Group>
				    <Form.Label>Amount to generate:</Form.Label>
				    <Form.Control id="money" type="number" placeholder="0" />
				    <Form.Text className="text-muted">
				      To test
				    </Form.Text>
				  </Form.Group>
				  <Button variant="warning" type="submit">
				    Generate
				  </Button>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumCreateMoney;
