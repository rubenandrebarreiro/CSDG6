package fct.unl.pt.csd.Controller;

import fct.unl.pt.csd.Daos.BankAccountDao;
import fct.unl.pt.csd.Daos.CreateMoneyDao;
import fct.unl.pt.csd.Daos.RegisterDao;
import fct.unl.pt.csd.Repositories.Redis.BankServiceReplicationJedisCluster;
import fct.unl.pt.csd.Services.BankServiceHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fct.unl.pt.csd.Contracts.ClassLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/")
public class RestAPIController {

        //private final BankService bS;
        private final ClientRequestHandler cR;
//        private final BankServiceHelper bH;
        private final BankServiceReplicationJedisCluster jD;

        @Autowired
        public RestAPIController(final ClientRequestHandler cR, final BankServiceHelper bH, final BankServiceReplicationJedisCluster jD) {
                this.cR = cR;
//                this.bH = bH;
                this.jD = jD;
        }

        @RequestMapping(method=POST,value="/register",consumes = "application/json")
        public ResponseEntity<String> createNew(@RequestBody RegisterDao dao) {

                if(cR.invokeCreateNew(dao.userName,dao.password, dao.amount).equals(""))
                        return new ResponseEntity<>("A client already exists with the name "+ dao.userName , HttpStatus.CONFLICT);
                //bH.registerUser(dao.userName, dao.password, dao.amount);
                jD.addNewUser(dao.userName,dao.password,dao.amount);
                return new ResponseEntity<>("Created a new account for "+ dao.userName , HttpStatus.OK);
        }

        @GetMapping(path="/test")
        public ResponseEntity<String> test() {
                jD.test();
                return new ResponseEntity<>("Simple test", HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/all",produces={"application/json"})
        public ResponseEntity<String> getAll() {
//                JSONObject arrAndHash = bH.getJSONArrayAndHash();
                JSONObject arrAndHash = jD.getJSONArrayAndHash();
                JSONObject it = this.cR.invokeListAllBankAccounts(arrAndHash.getInt("hash"));
                if (it.has("arr")) {
                        jD.replaceUsers(it.getJSONArray("arr"));
//                        bH.replaceUsers(it.getJSONArray("arr"));
                        return new ResponseEntity<>(it.get("arr").toString(), HttpStatus.OK);
                } else
                        return new ResponseEntity<>(arrAndHash.getJSONArray("arr").toString(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/amount",params ={"who"})
        public ResponseEntity<String> getAmountOfUser(@RequestParam("who") String who){
                long b = cR.invokeCheckCurrentAmount(who);
                if(b == -1)
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                if(!(b == this.jD.getMoney(who)))
                        this.jD.setAmount(who, b);
                return new ResponseEntity<>(b+"", HttpStatus.OK);
        }

        @RequestMapping(method = PUT, value = "/amount",params ={"from","to"},consumes = "application/json")
        public ResponseEntity<String> transferMoney(@RequestParam("from") String from,@RequestParam("to") String to,@RequestBody BankAccountDao dao){
                long[] transferInfo = this.jD.getTransferInfo(from, to, dao.amount);
                if(transferInfo == null)
                        return new ResponseEntity<>("One of the users wasn't found", HttpStatus.NOT_FOUND);
                JSONObject js = cR.invokeTransferMoney(from,transferInfo[0],to, transferInfo[1], transferInfo[2], transferInfo[3]);
                if(!js.has("error")){
                        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
                }
                this.jD.setAmount("from", js.getLong("fromSaldo"));
                this.jD.setAmount("to", js.getLong("toSaldo"));
                return this.transferMoney(from, to, new BankAccountDao(null, dao.amount));
        }

        @RequestMapping(method = PUT, value = "/money",params ={"who"},consumes = "application/json")
        public ResponseEntity<String> createMoney(@RequestParam("who") String who,@RequestBody CreateMoneyDao amount){
                Long money = this.jD.getMoney(who);
                JSONObject js = cR.invokeCreateMoney(who, money,money+amount.amount);
                jD.createMoney(who, money+amount.amount);
                System.out.println(js.toString());
                if(js.has("error")){
                        jD.setAmount(who, js.getLong("amount"));
//                        bH.setAmount(who, js.getLong("amount"));
                        return this.createMoney(who, new CreateMoneyDao(String.valueOf(amount.amount)));
                }
//                bH.setAmount(who, js.getLong("amount"));
                return new ResponseEntity<>(js.getLong("amount")+"", HttpStatus.OK);
        }

        @RequestMapping(method = POST, value= "/smartcontract", params={"who","id"},consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        public ResponseEntity<String> runSmartContract(@RequestParam("who") String who,@RequestParam("id") String id, @RequestBody byte[] data) throws Exception {
                ClassLoader loader = new ClassLoader();
                src.ContractMethods  cliente = (src.ContractMethods) loader.createObjectFromFile(id,data);
                System.out.println(cliente.toString());
                return new ResponseEntity<>(cliente.toString(),HttpStatus.OK);
        }

        public static boolean saveToFile(byte[] data,String filename){
                try {
                        File myObj = new File(filename);
                        if (myObj.createNewFile()) {
//                                System.out.println("File created: " + myObj.getName());
                        } else {
//                                System.out.println("File already exists.");
                        }
                        try {
                                FileOutputStream fos = new FileOutputStream(filename);
                                fos.write(data);
                                fos.close();
                        }catch(IOException e1) {
                                System.out.println("An error occurred.");
                                e1.printStackTrace();
                        }

                        return true;
                } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                        return false;
                }
        }
}
