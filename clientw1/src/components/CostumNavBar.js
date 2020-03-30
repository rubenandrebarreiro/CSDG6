import React, { Component } from 'react';
import {Navbar, Nav, Form, Button} from 'react-bootstrap';

class CostumNavBar extends Component {

  constructor(props){
    super(props);
    this.state = {
      money : 0,
      username: localStorage.getItem("username"),
      amount: 0
    }
  }

  componentDidMount(){
    if(localStorage.getItem("username")==="")
      location.replace("/login")
    else{
      this.update()
    }
  }

  update(){
     fetch("amount?who="+localStorage.getItem("username")).then((response)=>{
        return response.text()
      }).then((text) =>this.setState({amount:text}))
      .catch((error)=>{alert(error.text());location.replace("/login")})
  }

  logout(e){
    e.preventDefault();
    localStorage.clear();
    fetch("/logout");
    window.location="/login";
  }

  render() {
    return (
      <Navbar bg="warning" expand="lg">
        <Navbar.Brand>{this.state.username}</Navbar.Brand>
        <Nav className="mr-auto">
          <Nav.Link href="create">Create Money</Nav.Link>
          <Nav.Link href="transfere">Transfere Money</Nav.Link>
        </Nav>
        <Form inline>
          <Nav.Link >{this.state.amount} €</Nav.Link>
          <Button onClick={(e)=>this.update(e)}variant="outline-dark">Update</Button>
          <Button onClick={(e)=>this.logout(e)}variant="outline-dark">LOGOUT</Button>
        </Form>
      </Navbar>
    );
  }
}

export default CostumNavBar;
