import React, { Component } from 'react';
import CostumNavBar from './CostumNavBar';

class Home extends Component {
	componentDidMount(){
		if(localStorage.getItem("username") === "")
			location.replace("http://localhost:8080/login");
	}
  render() {
    return (
      <div>
        <CostumNavBar />
      </div>
    );
  }
}

export default Home;
