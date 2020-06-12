package fct.unl.pt.csd.Controller;

import fct.unl.pt.csd.Daos.BankAccountDao;
import fct.unl.pt.csd.Daos.CreateMoneyDao;
import fct.unl.pt.csd.Daos.RegisterDao;
import fct.unl.pt.csd.Entities.BankEntity;
import fct.unl.pt.csd.Repositories.Redis.BankServiceReplicationJedisCluster;
import fct.unl.pt.csd.Services.BankServiceHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/")
public class RestAPIController {

        //private final BankService bS;
        private final ClientRequestHandler cR;
        private final BankServiceHelper bH;
        private final BankServiceReplicationJedisCluster jD;

        @Autowired
        public RestAPIController(final ClientRequestHandler cR, final BankServiceHelper bH, final BankServiceReplicationJedisCluster jD) {
                this.cR = cR;
                this.bH = bH;
                this.jD = jD;
        }

        @RequestMapping(method=POST,value="/register",consumes = "application/json")
        public ResponseEntity<String> createNew(@RequestBody RegisterDao dao) {

                if(cR.invokeCreateNew(dao.userName,dao.password, dao.amount).equals(""))
                        return new ResponseEntity<>("A client already exists with the name "+ dao.userName , HttpStatus.CONFLICT);
                bH.registerUser(dao.userName, dao.password, dao.amount);
                jD.addNewUser(dao.userName,dao.password,dao.amount);
                return new ResponseEntity<>("Created a new account for "+ dao.userName , HttpStatus.OK);
        }

        @GetMapping(path="/test")
        public ResponseEntity<String> test() {
                BankEntity e = this.cR.invokeFindUser("FilipeLindo");
                return new ResponseEntity<>(e.getOwnerName(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/all",produces={"application/json"})
        public ResponseEntity<String> getAll() {
                JSONObject arrAndHash = bH.getJSONArrayAndHash();
                JSONObject it = this.cR.invokeListAllBankAccounts(arrAndHash.getInt("hash"));
                if (it.has("arr")) {
                        bH.replaceUsers(it.getJSONArray("arr"));
                        return new ResponseEntity<>(it.get("arr").toString(), HttpStatus.OK);
                } else
                        return new ResponseEntity<>(arrAndHash.getJSONArray("arr").toString(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/amount",params ={"who"})
        public ResponseEntity<String> getAmountOfUser(@RequestParam("who") String who){
                long b = cR.invokeCheckCurrentAmount(who);
                if(b == -1)
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                if(!(b == this.bH.currentAmount(who)))
                        this.bH.setAmount(who, b);
                return new ResponseEntity<>(b+"", HttpStatus.OK);
        }

        @RequestMapping(method = PUT, value = "/amount",params ={"from","to"},consumes = "application/json")
        public ResponseEntity<String> transferMoney(@RequestParam("from") String from,@RequestParam("to") String to,@RequestBody BankAccountDao dao){
                long[] transferInfo = bH.getTransferInfo(from, to, dao.amount);
                JSONObject js = cR.invokeTransferMoney(from,transferInfo[0],to, transferInfo[1], transferInfo[2], transferInfo[3]);
                if(!js.has("error")){
                        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
                }
                bH.cancelTransfer(from, to, js.getLong("fromSaldo"), js.getLong("toSaldo"));
                //return new ResponseEntity<>(bH.transferMoney(from, to, dao.amount).toString(), HttpStatus.OK);
                return this.transferMoney(from, to, new BankAccountDao(null, dao.amount));
        }

        @RequestMapping(method = PUT, value = "/money",params ={"who"},consumes = "application/json")
        public ResponseEntity<String> createMoney(@RequestParam("who") String who,@RequestBody CreateMoneyDao amount){
                BankEntity b = bH.findUser(who);
                JSONObject js = cR.invokeCreateMoney(who, b.getAmount(),b.getAmount()+amount.amount);
                System.out.println(b.getAmount());
                System.out.println(b.getAmount()+amount.amount);
                if(js.has("error")){
                        bH.setAmount(who, js.getLong("amount"));
                        return this.createMoney(who, new CreateMoneyDao(String.valueOf(amount.amount)));
                }
                bH.setAmount(who, js.getLong("amount"));
                return new ResponseEntity<>(js.getLong("amount")+"", HttpStatus.OK);
        }
}
