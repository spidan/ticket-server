package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.service.ServiceRegistry;
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
    public ResponseEntity<?> registerServiceTemplate(@RequestParam final String serviceName,
	    @RequestParam final URI serviceUri,
	    @RequestParam final URI templateUri) {
	try {
	    TemplateRegistry templates = (TemplateRegistry) context.getBean("templateRegistry");
	    ServiceRegistry services = (ServiceRegistry) context.getBean("serviceRegistry");
	    templates.registerTemplate(serviceName, templateUri);
	    services.registerTemplate(serviceName, serviceUri);
	} catch (Exception ex) {
	    return new ResponseEntity<>("Could not register mapping: " + ex.getMessage(),
		    HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/serviceTemplate",
		method = RequestMethod.GET)
    public ResponseEntity<?> getTemplateForService(@RequestParam final String serviceName) {
	TemplateRegistry registry = (TemplateRegistry) context.getBean("templateRegistry");
	try {
	    URI templateUri = registry.getTemplate(serviceName);
	    return new ResponseEntity<>(templateUri.toString(), HttpStatus.OK);
	} catch (Exception ex) {
	    return new ResponseEntity<>("Could not get mapping: " + ex.getMessage(),
		    HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
