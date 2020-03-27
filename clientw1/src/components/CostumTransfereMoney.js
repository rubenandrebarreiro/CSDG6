import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumTransfereMoney extends Component {
  render() {
    return (
    	<div className="App">
    		<CostumNavBar/>
    		<div className="App horizontalMargin30">
		        <Form>
				  <Form.Group controlId="formBasicEmail">
				    <Form.Label>Destination Username:</Form.Label>
				    <Form.Control type="text" placeholder="Enter username" />
				  </Form.Group>

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

export default CostumTransfereMoney;
