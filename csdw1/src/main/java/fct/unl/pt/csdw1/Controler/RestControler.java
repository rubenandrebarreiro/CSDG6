package fct.unl.pt.csdw1.Controler;

import fct.unl.pt.csdw1.Daos.BankAccountDao;
import fct.unl.pt.csdw1.Daos.LoginDao;
import fct.unl.pt.csdw1.Daos.RegisterDao;
import fct.unl.pt.csdw1.Entities.BankEntity;
import fct.unl.pt.csdw1.Services.BankService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/")
public class RestControler {
        private final BankService bS;
        @Autowired
        public RestControler(final BankService bS ) {
                this.bS = bS;
        }

        @RequestMapping(method=POST,value="/register",consumes = "application/json")
        public ResponseEntity<String> createNew(@RequestBody RegisterDao dao){
                System.out.println("Uname "+dao.userName+ " pass "+dao.password+" amount "+dao.amount);
                if(bS.registerUser(dao.userName,dao.password, dao.amount) == null)
                        return new ResponseEntity<>("A client already exists with the name "+ dao.userName , HttpStatus.CONFLICT);
                return new ResponseEntity<>("Created a new account for "+ dao.userName , HttpStatus.OK);
        }

        @RequestMapping(method=POST,value="/login",consumes = "application/json")
        public ResponseEntity<String> createNew(@RequestBody LoginDao dao){
                return null;
        }

        @GetMapping(path="/all")
        public ResponseEntity<String> getAll(){
                String response="";
                Iterator<BankEntity> it = this.bS.getAllBankAcc().iterator();
                BankEntity bankEntity = null;
                while(it.hasNext()) {
                        bankEntity = it.next();
                        response += bankEntity.getOwnerName() + " -> " + bankEntity.getAmount() + "€\n";
                }
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @RequestMapping(method = GET, value = "/amount",params ={"who"})
        public ResponseEntity<String> getAmountOfUser(@RequestParam("who") String who){
                long b = bS.currentAmount(who);
                if(b == -1)
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(b+"", HttpStatus.OK);
        }

        @RequestMapping(method = PUT, value = "/amount",params ={"from","to"})
        public ResponseEntity<String> transferMoney(@RequestParam("from") String from,@RequestParam("to") String to,@RequestBody BankAccountDao dao){
                JSONObject js = bS.transferMoney(from,to,dao.amount);
                if(!js.has("error"))
                        return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
                int err = js.getInt("errorID");
                switch(err){
                        case 0:
                                return new ResponseEntity<>("Client not found "+from, HttpStatus.NOT_FOUND);
                        case 1:
                                return new ResponseEntity<>("Client not found "+to, HttpStatus.NOT_FOUND);
                        default:
                                return new ResponseEntity<>(js.getString("error"), HttpStatus.CONFLICT);
                }
        }

        @RequestMapping(method = PUT, value = "/money",params ={"who"})
        public ResponseEntity<String> createMoney(@RequestParam("who") String who,@RequestBody BankAccountDao dao){
                JSONObject js = bS.createMoney(who,dao.amount);
                if(js.has("error"))
                        return new ResponseEntity<>(js.getString("error"), HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(js.getString("amount"), HttpStatus.OK);
        }

}
