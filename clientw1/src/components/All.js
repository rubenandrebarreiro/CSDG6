import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';
import {ListGroup} from 'react-bootstrap';
import '../App.css';

class All extends Component {
  constructor(props){
    super(props);
    this.state = {
      all: []
    }
  }

	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("/login");
    else{
      this.getAll();
    }
	}

  getAll(){
    fetch("/all",{headers: {'Content-Type': 'application/json',"authorization":localStorage.getItem("auth")},method:"GET"})
    .then((response)=>{return response.json()})
    .then((json)=>{console.log(json);this.setState({all:json})})
    .catch((error)=>{alert(error.text())})
  }

  render() {
    return (
    	<div>
    		<CostumNavBar/>
    		<div className="App horizontalOnlyMargin30">
        <ListGroup>
        {this.state.all.map((ell,key)=>{
          return(
            <ListGroup.Item key={key}>{ell.username +" has "+ell.amount+"€"}</ListGroup.Item>
          )
        })}
        </ListGroup>
			</div>
		</div>
    );
  }
}

export default All;
