# NOVA Crypto Banking Service
## A Banking Service with Cryptocurrency for New University of Lisbon

### Description
* The **_NOVA Crypto Banking Service_** it’s a **Banking Service with Cryptocurrencies for New University of Lisbon**.
* It’s a **Byzantine Fault-Tolerant** platform, using distributed **Consensus Proofs Agreements** on all the operations/transactions of the system.

***

### Introduction
* This platform it’s implemented in [**_Java_**](https://www.java.com/), and uses some **Frameworks/Libraries** such as [**_Node.js_**](https://nodejs.org/), [**_React.js_**](https://reactjs.org/) and [**_JavaScript_**](https://www.javascript.com/), for **Web Front-end**, [**_H2_**](https://www.h2database.com/) for **Back-end Persistent Databases**, [**_Spring_**](https://spring.io/) and [**_RESTful/REST (Representational State Transfer)_**](https://restfulapi.net/) for **Web Services**, [**_JSON (JavaScript Object Notation)_**](https://www.json.org/json-en.html), [**_JWT (JSON Web Tokens)_**](https://jwt.io/) for **Objects’ Serialization**, and [**_Bft-SMaRt_**](https://bft-smart.github.io/library/) for **Byzantine Fault-Tolerant State Machine Replication**.

* The platform also uses [**_HTTPS (HTTP 1.1)_**](https://en.wikipedia.org/wiki/HTTPS) using **Web Connections** over [**_TLS Protocol 1.2 (Transport Layer Security Protocol 1.2)_**](https://en.wikipedia.org/wiki/Transport_Layer_Security) for **Server-Only Authentication**, using **Self-signed Certificates**.

* Our platform it’s intended to **support _multiple machines (replicas)_**, in a **distributed** (and **decentralized**) fashion, even with **faulty nodes**, guaranteeing the **Dependability** and **Reliability** of the **System**.

***

### Instructions
* #### Platform's Components (Modules):
  * Our platform it's composed by **3 (three) main components (or modules)**:
  
  
    * **_Client Web App_**:
      * A Web App/Interface for the Clients/Users, built in [**_Node.js_**](https://nodejs.org/), [**_React.js_**](https://reactjs.org/) and [**_JavaScript_**](https://www.javascript.com/);
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/clientw1) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/clientw1_**](https://github.com/fmpisantos/CSDG6/tree/master/clientw1)
    
    
    * **_Server's (Proxy's) REST API_**:
      * A [**_Spring_**](https://spring.io/) **aplication** using [**_RESTful/REST (Representational State Transfer)_**](https://restfulapi.net/) **API** for the interactions between the **_Client/User_** and the **_Server (Proxy)_**, responsible for the **mapping of the correct and valid states** of the **System**, derived from the operations of the **_Clients/Users_**;
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/csd) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/csd_**](https://github.com/fmpisantos/CSDG6/tree/master/csd)
    
    
    * **_Byzantine Fault-Tolerant Server (State Machine Replica)_**:
      * A **_Byzantine Fault-Tolerant Server_**, **replicated** and **distributed** for all the **heterogeneous nodes** of the **System**, in a **decentralized** fashion, **keeping the current valid state and data of the global system**, storing it at **Back-end Persistent Databases**, using [**_H2_**](https://www.h2database.com/);
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer_**](https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer)

***

