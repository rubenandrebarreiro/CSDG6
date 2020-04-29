import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../App.css';

class CostumTransferMoney extends Component {
	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("/login");
	}

	transferMoney(e){
		e.preventDefault();
		let dest = document.getElementById("dest");
		let amount = document.getElementById("amount");
		if(amount.value > 0 && dest.value!=="")
			fetch("amount?from="+localStorage.getItem("username")+"&to="+dest.value,
				{
					headers: {'Content-Type': 'application/json',
					"authorization":localStorage.getItem("auth")
				},
					method:"PUT",
					body : JSON.stringify({"amount":amount.value})
			})
			.then((response)=>{return response.text()})
			.then((text)=>window.location = "/")
			.catch((error)=>alert(error.error()))
		dest.value = "";
		amount.value = 0;
	}

  render() {
    return (
        <div className="div2">
    	   <CostumNavBar/> 
           <div className="App horizontalOnlyMargin30">
                <img src="/nova-crypto-banking-service.png" width="380px" />
                <br /><br />
                <Form onSubmit={(e)=>this.transferMoney(e)}>
                  <Form.Group >
                    <Form.Label><b>DESTINATION USERNAME</b></Form.Label>
                    <Form.Control id="dest" type="text" placeholder="Enter username" />
                  </Form.Group>

                  <Form.Group >
                    <Form.Label><b>AMOUNT TO TRANSFER</b></Form.Label>
                    <Form.Control id="amount" type="number" placeholder="0" />
                  </Form.Group>
                  <Button variant="warning" type="submit">
                    <b>Transfer</b>
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

export default CostumTransferMoney;
