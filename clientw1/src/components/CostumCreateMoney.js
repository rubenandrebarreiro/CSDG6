import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumCreateMoney extends Component {
	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("http://localhost:8080/login");
	}
  render() {
    return (
    	<div>
    		<CostumNavBar/>
    		<div className="App horizontalMargin30">
		        <Form>
				  <Form.Group controlId="formBasicEmail">
				    <Form.Label>Amount to generate:</Form.Label>
				    <Form.Control type="number" placeholder="0" />
				    <Form.Text className="text-muted">
				      To test
				    </Form.Text>
				  </Form.Group>
				  <Button variant="primary" type="submit">
				    Submit
				  </Button>
				</Form>
			</div>
		</div>
    );
  }
}

export default CostumCreateMoney;
