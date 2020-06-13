import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {Form,Button} from 'react-bootstrap';
import '../login.css'
import '../App.css';

class CostumSmartContract extends Component {
    componentDidMount(){
        if(localStorage.getItem("username") === "")
            location.replace("/login");
    }

    sendContract(e){
        e.preventDefault();
        var file =document.getElementById("contract").files[0];
        var reader = new FileReader();
        reader.onload = function() {

            var arrayBuffer = this.result,
                array = new Uint8Array(arrayBuffer),
                binaryString = String.fromCharCode.apply(null, array);

            fetch("smartcontract?who="+localStorage.getItem("username")+"&id="+"ServerContract.class",
                {
                    headers: {
                        "authorization":localStorage.getItem("auth")
                    },
                    method:"POST",
                    body : file
                })
                .then((response)=>{return response.text()})
                .then((json)=>window.location = "/")
                .catch((error)=>alert(error.text()))

        }
        var bytes = reader.readAsArrayBuffer(file)
        //console.log(bytes)

    }

    render() {
        return (
            <div className="div2">
                <CostumNavBar/>
                <div className="App horizontalOnlyMargin30">
                    <img src="/nova-crypto-banking-service.png" width="380px" />
                    <br /><br />
                    <Form onSubmit={(e)=>this.sendContract(e)}>
                        <Form.Group>
                            <Form.Label><b>Load Smart Contract</b></Form.Label>
                            <Form.Control style={{"box-sizing":"border-box","border": "1px solid "}} id="contract" type="file" accept=".class" placeholder="0" />
                            <Form.Text className="text-muted">
                                .class File required
                            </Form.Text>
                        </Form.Group>
                        <Button variant="warning" type="submit">
                            <b>Send Smart Contract</b>
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

export default CostumSmartContract;
