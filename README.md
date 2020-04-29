# NOVA Crypto Banking Service
## A Banking Service with Cryptocurrencies for New University of Lisbon

![https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/banner/JPGs/banner-1.jpg?token=AIXAYR3EBPLQT452CHOL2YC6WGPBY](https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/banner/JPGs/banner-1.jpg?token=AIXAYR3EBPLQT452CHOL2YC6WGPBY)
######  NOVA Crypto Banking Service A Banking Service with Cryptocurrencies for New University of Lisbon) - Banner #1

***

### Description
* The **_NOVA Crypto Banking Service_** it’s a **Banking Service with Cryptocurrencies for New University of Lisbon**.
* It’s a **Byzantine Fault-Tolerant** platform, using distributed **Consensus Proofs Agreements** on all the **operations/transactions** of the system.

***

### Introduction
* This platform it’s implemented in [**_Java_**](https://www.java.com/), and uses some **Frameworks/Libraries** such as [**_Node.js_**](https://nodejs.org/), [**_React.js_**](https://reactjs.org/) and [**_JavaScript_**](https://www.javascript.com/), for **Web Front-end**, [**_H2_**](https://www.h2database.com/) for **Back-end Persistent Databases**, [**_Spring_**](https://spring.io/) and [**_RESTful/REST (Representational State Transfer)_**](https://restfulapi.net/) for **Web Services**, [**_JSON (JavaScript Object Notation)_**](https://www.json.org/json-en.html), [**_JWT (JSON Web Tokens)_**](https://jwt.io/) for **Objects’ Serialization**, and [**_Bft-SMaRt_**](https://bft-smart.github.io/library/) for **Byzantine Fault-Tolerant State Machine Replication**.

* The platform also uses [**_HTTPS (HTTP 1.1)_**](https://en.wikipedia.org/wiki/HTTPS) using **Web Connections** over [**_TLS Protocol 1.2 (Transport Layer Security Protocol 1.2)_**](https://en.wikipedia.org/wiki/Transport_Layer_Security) for **Server-Only Authentication**, using **Self-signed Certificates**.

* Our platform it’s intended to **support _multiple machines (replicas)_**, in a **distributed** (and **decentralized**) fashion, even with **faulty nodes**, guaranteeing the **Dependability** and **Reliability** of the **System**.

***

### Composition/Services of the System
* #### Platform's Components (Modules):
  * Our platform it's composed by **3 (three) main components (or modules)**:
  
  
    * **_Client/User Web App_**:
      * A [**_React.js_**](https://reactjs.org/) **Web Application/Interface** for the **Clients/Users**, built in [**_Node.js_**](https://nodejs.org/), and [**_JavaScript_**](https://www.javascript.com/);
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/clientw1) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/clientw1_**](https://github.com/fmpisantos/CSDG6/tree/master/clientw1)
    
    
    * **_Server's (Proxy's) REST API_**:
      * A [**_Spring_**](https://spring.io/) **Application/Interface** using [**_RESTful/REST (Representational State Transfer)_**](https://restfulapi.net/) **API** for the interactions between the **_Client/User_** and the **_Server (Proxy)_**, responsible for the **mapping of the correct and valid states** of the **System**, derived from the operations of the **_Clients/Users_**;
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/csd) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/csd_**](https://github.com/fmpisantos/CSDG6/tree/master/csd)
    
    
    * **_Byzantine Fault-Tolerant Server (State Machine Replica)_**:
      * A [**_Bft-SMaRt_**](https://bft-smart.github.io/library/) based **_Byzantine Fault-Tolerant Server_**, **replicated** and **distributed** for all the **heterogeneous nodes** of the **System**, in a **decentralized** fashion, **keeping the current valid state and data of the global system**, storing it at **Back-end Persistent Databases**, using [**_H2_**](https://www.h2database.com/);
      * Click [**_here_**](https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer) to see its content:
        * [**_https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer_**](https://github.com/fmpisantos/CSDG6/tree/master/CSDBftServer)

***

### Instructions
1. **Cloning the project**:   
   * Clone the project, doing the following command in a **_Terminal_**/**_Prompt_** (e.g., [**_Git Bash_**](https://git-scm.com/)):
     * ```
       git clone https://github.com/fmpisantos/CSDG6.git
       ```
   * You should see the following output:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/clone-project-1.jpg?token=AIXAYR4KIUU2GN2Z44XWERS6WGPSK" alt="Instructions - Cloning the Project #1" height="250" width="375">&nbsp;&nbsp;
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/clone-project-2.jpg?token=AIXAYR525U2TKPY6MX7GSQC6WGQH4" alt="Instructions - Cloning the Project #2" height="250" width="375">
   * This will create a **Project's Structure** with **3 (three) main components**, similar to the following:
     ``` bash
     CSDG6
     ├── clientw1
     ├── csd
     ├── CSDBftServer
     ```
   * The previously mentioned **3 (three) main components** of the **Project's Structure**, are the following:
     * **_Byzantine Fault-Tolerant Server (State Machine Replica)_**;
     * **_Server's (Proxy's) REST API_**;
     * **_Client's/User's React.js/Node.js Web App_**;
  
2. **Starting the _Byzantine Fault-Tolerant Server (State Machine Replica)_**:
   * Open the **_CSDBftServer_**, in the **Project's Structure**'s folder, as a **Project**, in your **IDE** (e.g., [**_JetBrains' IntelliJ IDEA_**](https://www.jetbrains.com/idea/)), as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-1.jpg?token=AIXAYRZ27KQPJTWFPF5RYYS6WGW7I" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #1" height="250" width="210">
   
   * Open the **_BankService_** **_Java_** class, as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-2.jpg?token=AIXAYR7SJXVMZZWCKFUIOWS6WGXOW" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #2" height="250" width="210">
   
   * Create a **Main configuration** to the **_BankService_** **_Java_** class, as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-3.jpg?token=AIXAYR6KB6235KA3SLDM2DS6WGXYA" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #3" height="81" width="288"> <br> <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-4.jpg?token=AIXAYR6IDYPKW2SQWJ77MXS6WGXYM" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #4" height="81" width="288"> <br> <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-5.jpg?token=AIXAYR4MQ3IYTZVROUXGBE26WGXY2" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #5" height="81" width="288">
   * Configure a custom **Main Configuration**, giving as **Program argument**, one of the **Replica's ID** defined in the **_Hosts' Configuration_** ([**_Hosts.config_**](https://github.com/fmpisantos/CSDG6/blob/master/CSDBftServer/config/hosts.config) file), click **Apply** and then, **OK**, as demonstrated next:
   <br><br>
   <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-6.jpg?token=AIXAYR5ANPP2ZW3NYITLMSS6WGZFG" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #6" height="200" width="150"> &nbsp;&nbsp; <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-7.jpg?token=AIXAYR3X4RK74WADZ4U4KYS6WGZGW" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #7" height="200" width="150"> &nbsp;&nbsp; <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-8.jpg?token=AIXAYR6SCACGXJLFEQQG5WS6WGZIK" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #8" height="200" width="150"> &nbsp;&nbsp; <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-9.jpg?token=AIXAYR3MFTSERFBYI3B7ZKK6WGZJW" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #9" height="200" width="150">
   
   * Run the **Main Configuration** for the **_BankService_** **_Java_** class, created previously;
   
   * The output should look something similar to the following:
     ```batch
     -- Using view stored on disk
     -- Using view stored on disk
     -- ID = 0
     -- N = 4
     -- F = 1
     -- Port = 11000
     -- requestTimeout = 2000
     -- maxBatch = 400
     -- Using MACs
     -- In current view: ID:0; F:1; Processes:0(/127.0.0.1:11000),1(/127.0.0.1:11010),2(/127.0.0.1:11020),3(/127.0.0.1:11030),
     ```
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-10.jpg?token=AIXAYR2QG72KVY6SDWQDALC6WGZNO" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #10" height="150" width="400">
  
  * Repeat the process for the number of **Replicas** of the **System**, which you want to create, as long as they are defined in the **_Hosts' Configuration_** ([**_Hosts.config_**](https://github.com/fmpisantos/CSDG6/blob/master/CSDBftServer/config/hosts.config) file), always providing the **Replica's ID**, as a **Program Argument** to the **Main Configuration** of the **_BankService_** **_Java_** class;
  
  * As you can see, the **_Key Exchange/Agreement_** for the **Consensus Proofs Agreements** between the active **Replicas**, are made, using the **_Diffie-Hellman Key Exchange/Agreement_**:
     ```batch
     -- Using view stored on disk
     -- Using view stored on disk
     -- Diffie-Hellman complete with 0
     -- ID = 1
     -- N = 4
     -- F = 1
     -- Port = 11010
     -- requestTimeout = 2000
     -- maxBatch = 400
     -- Using MACs
     -- In current view: ID:0; F:1; Processes:0(/127.0.0.1:11000),1(/127.0.0.1:11010),2(/127.0.0.1:11020),3(/127.0.0.1:11030),
     ```
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-bft-server-11.jpg?token=AIXAYRYJ3DMB2AY2BHKGX7C6WG472" alt="Instructions - Starting the Byzantine Fault-Tolerant Server (State Machine Replica) #11" height="150" width="400">
  
3. **Starting the _Server's (Proxy's) REST API_**:  
  * Open the **_csd_**, in the **Project's Structure**'s folder, as a **Project**, in your **IDE** (e.g., [**_JetBrains' IntelliJ IDEA_**](https://www.jetbrains.com/idea/)), as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-1.jpg?token=AIXAYRYX5BJATULUADEHEZS6WHDCG" alt="Instructions - Starting the Server's (Proxy's) REST API #1" height="250" width="210">
   
   * Open the **_CsdApplication_** **_Java_** class, as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-2.jpg?token=AIXAYR6QUUR4ZXQUCS4NXD26WHDGM" alt="Instructions - Starting the Server's (Proxy's) REST API #2" height="250" width="210">
   
   * Run the **Main configuration** to the **_CsdApplication_** **_Java_** class, as demonstrated following:
     <br><br>
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-3.jpg?token=AIXAYR53U542JF464HANNYS6WHDL6" alt="Instructions - Starting the Server's (Proxy's) REST API #3" height="81" width="288"> <br> <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-4.jpg?token=AIXAYR5ZL7F7PFMIQIJA7Q26WHDNY" alt="Instructions - Starting the Server's (Proxy's) REST API #4" height="81" width="288"> <br> <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-5.jpg?token=AIXAYR477UYG62TIGJRBCTS6WHDR2" alt="Instructions - Starting the Server's (Proxy's) REST API #5" height="81" width="288">
   
   * During the resulting output, you should be able to see the **Server's (Proxy's) REST API** connecting to the **Byzantine Fault-Tolerant Replicas** previously created:
     ```batch
     (...)
     Connecting to replica 0 at /127.0.0.1:11000
     Channel active
     Connecting to replica 1 at /127.0.0.1:11010
     Channel active
     Connecting to replica 2 at /127.0.0.1:11020
     Channel active
     Connecting to replica 3 at /127.0.0.1:11030
     Channel active
     (...)
     ```
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-rest-proxy-server-6.jpg?token=AIXAYR2FHAV4JEOLGVFRYO26WHHMO" alt="Instructions - Starting the Server's (Proxy's) REST API #6" height="150" width="400">

4. **Starting the _Client/User Web App_**:
   * Open the **_clientw1_**, in the **Project's Structure**'s folder;
   
   * Run the 2 (two) following commands in a **_Terminal_**/**_Prompt_** (e.g., [**_Git Bash_**](https://git-scm.com/)):
   
     * ```
       npm install
       ```
     * ```
       npm start
       ```
   
   * You should see an output similar to the following:
   
     <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/screenshots/instructions/JPGs/starting-reactjs-web-app-1.jpg?token=AIXAYR7QXKEIS3TPVXWC4FS6WKF7E" alt="Instructions - Starting the Client/User Web App #1" height="250" width="400">
   
***

### Our Currency - The NOVA Coin
* Our system uses a **cryptocurrency**, called **_NOVA Coin_**, represented as following:

<img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/assets/PNGs/nova-coin-1.png?token=AIXAYR5HTADQBYCWFMUQQSK6WGOXW" alt="NOVA Coin" height="200" width="200"> &nbsp;&nbsp; <img src="https://raw.githubusercontent.com/fmpisantos/CSDG6/master/imgs/assets/PNGs/nova-coin-2.png?token=AIXAYR3MUTSSX7TFFSL3B5S6WG7OQ" alt="NOVA Coin" height="200" width="200">

   * _Forged in [**_FCT NOVA (Faculty of Sciences and Technology of New University of Lisbon / NOVA School of Science and Technology)_**](https://www.fct.unl.pt/), in 2020_;
   * _Our Motto_:
     * **_Omnis Civitas Contra Se Divisa Non Stabit_** (_Latin_);
     * **_Any city divided against itself will not remain_** (_English_);

#### And now, my friends, let's start to make money transactions with our _NOVA Coin_!!!
#### Good business for all!!!

***

### Authors

* #### Bruno Vicente Santos
  * **_E-mail_**:
    * [**_bv.santos@campus.fct.unl.pt_**](mailto:bv.santos@campus.fct.unl.pt)

* #### Filipe Miguel Santos
  * **_E-mail_**:
    * [**_fmpi.santos@campus.fct.unl.pt_**](mailto:fmpi.santos@campus.fct.unl.pt)

* #### Rúben André Barreiro
  * **_Personal Page_**:
    * [**_http://rubenandrebarreiro.github.io/_**](http://rubenandrebarreiro.github.io/)
  * **_E-mail_**:
    * [**_r.barreiro@campus.fct.unl.pt_**](mailto:r.barreiro@campus.fct.unl.pt)

***

### Supervisor

* #### Henrique João Domingos
  * **_Personal Page_**:
    * [**_https://asc.di.fct.unl.pt/~hj/_**](https://asc.di.fct.unl.pt/~hj/)
  * **_E-mail_**:
    * [**_hj@fct.unl.pt_**](mailto:hj@fct.unl.pt)
