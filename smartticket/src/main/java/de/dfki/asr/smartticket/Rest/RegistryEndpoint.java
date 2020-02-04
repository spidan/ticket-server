package de.dfki.asr.smartticket.Rest;

import java.net.URI;
import de.dfki.asr.smartticket.service.TemplateRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistryEndpoint {

    @Autowired
    private ApplicationContext context;

    @RequestMapping(value = "/serviceTemplate",
	    method = RequestMethod.POST)
    public ResponseEntity<?> registerServiceTemplate(@RequestParam final URI serviceUri,
	    @RequestParam final URI templateUri) {
	try {
	    TemplateRegistry registry = (TemplateRegistry) context.getBean("templateRegistry");
	    registry.registerTemplate(serviceUri, templateUri);
	} catch (Exception ex) {
	    return new ResponseEntity<>("Could not register mapping: " + ex.getMessage(),
		    HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(HttpStatus.OK);
    }

}
