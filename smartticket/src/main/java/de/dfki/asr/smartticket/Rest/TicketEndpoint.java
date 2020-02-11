package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.service.BookingProcess;
import de.dfki.asr.smartticket.service.TicketWrapper;
import de.dfki.asr.smartticket.service.Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TicketEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(TicketEndpoint.class);
    private static final String SERVICE_URL = "test";

    @Autowired
    private TicketWrapper ticket;

    private Map<String, String> originalHeaders = new HashMap<>();

    @RequestMapping(value = "/ticket",
	    method = RequestMethod.POST,
	    consumes = "text/turtle",
	    produces = "image/png")
    @ResponseBody
    public ResponseEntity<?> receiveTicket(@RequestHeader final Map<String, String> headers,
	    @RequestParam final String targetService,
	    @RequestBody final Model model) {
	try {
	    this.originalHeaders = headers;
	    return createTicketFromModel(model, targetService);
	} catch (Exception ex) {
	    return new ResponseEntity("Could not get ticket: " + ex.getMessage(),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    private ResponseEntity<?> createTicketFromModel(final Model model, final String targetService) throws Exception {
	BookingProcess booking = new BookingProcess();
	booking.writeRequestToRepo(model);
	byte[] ticketResult = null;
	ticket.setRepo(booking.getRepo());
	try {
	    ticketResult = ticket.receiveTicket(originalHeaders, targetService);
	} catch (IOException ex) {
	    LOG.error("Error contacting target service: " + ex.getMessage());
	    return new ResponseEntity<>("Error contacting target service: " + ex.getMessage(),
		    HttpStatus.BAD_GATEWAY);
	}
	return new ResponseEntity<>(ticketResult, HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket",
	    method = RequestMethod.POST,
	    consumes = {"application/xml", "application/json"})
    @ResponseBody
    public ResponseEntity<?> receiveXmlOrJsonTicket(@RequestHeader final Map<String, String> headers,
	    @RequestParam final String targetService,
	    @RequestParam final String mappingFileName,
	    @RequestBody final String input) throws UnsupportedEncodingException, IOException {
	try {
	    this.originalHeaders = headers;
	    String mappingEndpoint = Utils.RML_SERVICE_URL
			    .concat("?mappingFile=")
			    .concat(mappingFileName);
	    String response = Utils.sendPostRequest(mappingEndpoint, input,
		    headers.get("content-type"));
	    Model model = parseToTurtle(response);
	    return createTicketFromModel(model, targetService);
	} catch (Exception ex) {
	   return new ResponseEntity("Could not get ticket: " + ex.getMessage(),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    private Model parseToTurtle(final String response) throws RDFHandlerException,
	    UnsupportedRDFormatException, UnsupportedEncodingException, IOException, RDFParseException {
	InputStream rdfStream = new ByteArrayInputStream(response.getBytes("utf-8"));
	RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
	Model model = new LinkedHashModel();
	parser.setRDFHandler(new StatementCollector(model));
	parser.parse(rdfStream, SERVICE_URL);
	return model;
    }
}
