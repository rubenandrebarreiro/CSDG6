package fct.unl.pt.csdw1.Controler;

import fct.unl.pt.csdw1.Daos.BankAccountDao;
import fct.unl.pt.csdw1.Entities.BankEntity;
import fct.unl.pt.csdw1.Services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RestControler {
        private final BankService bS;
        @Autowired
        public RestControler(final BankService bS ) {
                this.bS = bS;
        }

        @GetMapping(path ="/")
        public ResponseEntity<String> home(final HttpMethod method,final WebRequest request){
                final String htmlContentString =
                                "<!DOCTYPE html>"
                                + "<html><body>"
                                + "<h1>Welcome to the NOVA Banking Service</h1>"
                                + "<h3>using Spring REST Web Services</h3>"
                                + "<p style='font-size: large;'>"
                                + "Click here for <a href='swagger-ui.html'>REST API documentation</a> "
                                + "powered by <a href='https://swagger.io/'>Swagger</a></p>"
                                + "</body></html>";

                return new ResponseEntity<>(htmlContentString, HttpStatus.OK);
        }

        @RequestMapping(method=POST,value="/create",consumes = "application/json")
        public ResponseEntity<String> createNew(BankAccountDao dao){
                bS.createUser(dao.owner,dao.amount);
                return new ResponseEntity<>("Created a new account for "+ dao.owner , HttpStatus.OK);
        }

        @GetMapping(path="/getAll")
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
}
