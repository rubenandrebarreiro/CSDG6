package fct.unl.pt.csdw1.Controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class RestControler {

        @Autowired
        public RestControler( ) {
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
}
