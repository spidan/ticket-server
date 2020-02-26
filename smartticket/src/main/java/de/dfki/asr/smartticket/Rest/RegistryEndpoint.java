package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.data.RDFServiceRegistry;
import org.eclipse.rdf4j.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistryEndpoint {

    @Autowired
    private RDFServiceRegistry serviceRegistry;

    @RequestMapping(value = "/serviceTemplate",
	    method = RequestMethod.POST,
	    consumes = "text/turtle")
    public ResponseEntity<?> registerRDFTemplate(@RequestBody final Model serviceModel) {
	serviceRegistry.write(serviceModel);
	return new ResponseEntity<>(HttpStatus.OK);
    }
}
