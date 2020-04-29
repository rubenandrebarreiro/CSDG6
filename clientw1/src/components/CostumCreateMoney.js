import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../login.css'
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
			fetch("money?who="+localStorage.getItem("username"),
				{
					headers: {'Content-Type': 'application/json',
					"authorization":localStorage.getItem("auth")
				},
				method:"PUT",
				body : JSON.stringify({"amount":money.value})
			})
			.then((response)=>{return response.text()})
			.then((json)=>window.location = "/")
			.catch((error)=>alert(error.text()))
		money.value = 0;
	}
  render() {
    return (
        <div className="div2">
            <CostumNavBar/>
            <div className="App horizontalOnlyMargin30">
                <img src="/nova-crypto-banking-service.png" width="380px" />
                <br /><br />
                <Form onSubmit={(e)=>this.createMoney(e)}>
                  <Form.Group>
                    <Form.Label><b>AMOUNT TO GENERATE</b></Form.Label>
                    <Form.Control id="money" type="number" placeholder="0" />
                    <Form.Text className="text-muted">
                      To test
                    </Form.Text>
                  </Form.Group>
                  <Button variant="warning" type="submit">
                    <b>Generate</b>
                  </Button>
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

export default CostumCreateMoney;
