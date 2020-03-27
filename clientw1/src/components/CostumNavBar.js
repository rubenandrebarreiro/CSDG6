import React, { Component } from 'react';
import {Navbar, Nav, Form, Button, FormControl} from 'react-bootstrap';

class CostumNavBar extends Component {

  render() {
    return (
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand>AWMan</Navbar.Brand>
        <Nav className="mr-auto">
          <Nav.Link href="create">Create Money</Nav.Link>
          <Nav.Link href="transfere">Transfere Money</Nav.Link>
        </Nav>
        <Form inline>
          <Nav.Link >100000€</Nav.Link>
          <Button variant="outline-info">Update</Button>
        </Form>
      </Navbar>
    );
  }
}

export default CostumNavBar;
