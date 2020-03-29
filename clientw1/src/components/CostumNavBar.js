import React, { Component } from 'react';
import {Navbar, Nav, Form, Button} from 'react-bootstrap';

class CostumNavBar extends Component {

  constructor(props){
    super(props);
    this.state = {
      money : 0,
      username: localStorage.getItem("username")
    }
  }

  componentDidMount(){
    fetch("amount").then((response)=>{
      return response.text()
    }).then((text) =>console.log(text))
    .catch((error)=>location.replace("http://localhost:8080/login"))
  }

  render() {
    return (
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand>{this.state.username}</Navbar.Brand>
        <Nav className="mr-auto">
          <Nav.Link href="create">Create Money</Nav.Link>
          <Nav.Link href="transfere">Transfere Money</Nav.Link>
        </Nav>
        <Form inline>
          <Nav.Link >0</Nav.Link>
          <Button variant="outline-info">Update</Button>
        </Form>
      </Navbar>
    );
  }
}

export default CostumNavBar;
