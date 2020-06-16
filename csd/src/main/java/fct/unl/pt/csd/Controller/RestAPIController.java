package fct.unl.pt.csd.Controller;

import fct.unl.pt.csd.Daos.BankAccountDao;
import fct.unl.pt.csd.Daos.CreateMoneyDao;
import fct.unl.pt.csd.Daos.RegisterDao;
import fct.unl.pt.csd.Entities.BidEntity;
import fct.unl.pt.csd.Repositories.Redis.BankServiceReplicationJedisCluster;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/")
public class RestAPIController {

        private final ClientRequestHandler cR;
        private final BankServiceReplicationJedisCluster jD;

        @Autowired
        public RestAPIController(final ClientRequestHandler cR,  final BankServiceReplicationJedisCluster jD) {
                this.cR = cR;
                this.jD = jD;
        }

        @RequestMapping(method=POST,value="/register",consumes = "application/json")
        public ResponseEntity<String> createNew(@RequestBody RegisterDao dao) {
                if(cR.invokeCreateNew(dao.userName,dao.password, dao.amount, dao.roles).equals(""))
                        return new ResponseEntity<>("A client already exists with the name "+ dao.userName , HttpStatus.CONFLICT);
                jD.addNewUser(dao.userName,dao.password,dao.amount);
                return new ResponseEntity<>("Created a new account for "+ dao.userName , HttpStatus.OK);
        }

        @GetMapping(path="/test")
        public ResponseEntity<String> test(@RequestHeader("Authorization") String bearer) {
                String username = this.jD.getSubject(bearer);
//                return new ResponseEntity<>("hey you",HttpStatus.OK);
                return new ResponseEntity<>(cR.invokeFindUser(username).getStringRoles(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/all",produces={"application/json"})
        public ResponseEntity<String> getAll() {
                JSONObject arrAndHash = jD.getJSONArrayAndHash();
                JSONObject it = this.cR.invokeListAllBankAccounts(arrAndHash.getInt("hash"));
                if (it.has("arr")) {
                        jD.replaceUsers(it.getJSONArray("arr"));
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
                        return this.createMoney(who, new CreateMoneyDao(String.valueOf(amount.amount)));
                }
                jD.setAmount(who, js.getLong("amount"));
                return new ResponseEntity<>(js.getLong("amount")+"", HttpStatus.OK);
        }

        @RequestMapping(method = POST, value = "/smartcontract",consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        public ResponseEntity<String> smartContract(@RequestHeader("Authorization") String bearer, @RequestBody byte[] data){
                String username = this.jD.getSubject(bearer);
                JSONObject js = cR.invokeCreateSmartContract(username,data);
                HttpStatus t = HttpStatus.OK;
                if(js.has("error")){
                        t = HttpStatus.LOOP_DETECTED;
                }else
                        return new ResponseEntity<>(js.toString(),t);
                return null;
        }

        @RequestMapping(method = DELETE, value = "/closeauction",params ={"id"},consumes = "application/json")
        public ResponseEntity<String> closeAuction(@RequestHeader("Authorization") String bearer, @RequestParam("id") Long id){
                String username = this.jD.getSubject(bearer);
                JSONObject j = this.cR.invokeCloseAuction(username, id);
                if(j.has("error"))
                        return new ResponseEntity<>(j.get("error").toString(), HttpStatus.FORBIDDEN);
                return new ResponseEntity<>("Auction Closed", HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/openauctions", produces={"application/json"})
        public ResponseEntity<String> getOpenAuctions(){
                Iterable j = this.cR.invokeListAllCurrentOpenedAuctions();
                JSONArray js = new JSONArray().put(j);
                if(js.isEmpty())
                        return new ResponseEntity<>("No auctions active: ", HttpStatus.OK);
                return new ResponseEntity<>(js.toString(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/closedauctions", produces={"application/json"})
        public ResponseEntity<String> getClosedAuctions(){
                Iterable j = this.cR.invokeListAllCurrentClosedAuctions();
                JSONArray js = new JSONArray(j);
                if(js.isEmpty())
                        return new ResponseEntity<>("No auctions active: ", HttpStatus.OK);
                return new ResponseEntity<>(js.toString(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/getbid", produces={"application/json"})
        public ResponseEntity<String> getBidFromUser(@RequestHeader("Authorization") String bearer){
                String username = this.jD.getSubject(bearer);
                Iterable j = this.cR.invokeListAllBidsFromUser(username);
                JSONArray js = new JSONArray(j);
                if(js.isEmpty())
                        return new ResponseEntity<>("No bids from user +"+username, HttpStatus.OK);
                return new ResponseEntity<>(js.toString(), HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/getbidfromauction", produces={"application/json"})
        public ResponseEntity<String> getBidsFromAuction(@RequestParam("id") Long id){
                Iterable j = this.cR.invokeListAllBidsFromAuction(id);
                JSONArray js = new JSONArray(j);
                if(js.isEmpty())
                        return new ResponseEntity<>("No bids in this auction with id: "+id, HttpStatus.OK);
                return new ResponseEntity<>(js.toString(), HttpStatus.OK);
        }

        @RequestMapping(method = POST, value = "/createbid",consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
        public ResponseEntity<String> createBid(@RequestHeader("Authorization") String bearer, @RequestBody byte[] data){
                String username = this.jD.getSubject(bearer);
                //TODO: send code to bft to run
                JSONObject js = cR.invokeCreateSmartContract(username,data);
                HttpStatus t = HttpStatus.OK;
                if(js.has("error")){
                        t = HttpStatus.LOOP_DETECTED;
                }else
                        return new ResponseEntity<>(js.toString(),t);
                return null;
        }

        @RequestMapping(method = GET, value = "/checkbidclosed", produces={"application/json"})
        public ResponseEntity<String> checkBidClosedAuction(@RequestParam("id") Long id){
                BidEntity bid = this.cR.invokeCheckWinnerBidClosedAuction(id);
                if(bid == null)
                        return new ResponseEntity<>("No bid", HttpStatus.OK);
                return new ResponseEntity<>(bid.getJSON().toString(), HttpStatus.OK);
        }


}
