package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.exceptions.ServiceConnectionException;
import de.dfki.asr.smartticket.service.BookingProcess;
import de.dfki.asr.smartticket.service.TicketWrapper;
import de.dfki.asr.smartticket.service.Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TicketEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(TicketEndpoint.class);
    private static final String SERVICE_URL = "test";

    @RequestMapping(value = "/ticket",
	    method = RequestMethod.POST,
	    consumes = "text/turtle",
	    produces = "image/png")
    @ResponseBody
    public ResponseEntity<?> receiveTicket(@RequestParam final String targetService,
	    @RequestBody final Model model) {
	try {
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
	TicketWrapper ticket = new TicketWrapper(booking.getRepo());
	try {
	    ticketResult = ticket.receiveTicket(targetService);
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
    public ResponseEntity<?> receiveXmlOrJsonTicket(@RequestParam final String targetService,
	    @RequestBody final String input)
	    throws UnsupportedEncodingException, IOException {
	try {
	    String response = "";
	    try {
		response = Utils.sendPostRequest(Utils.DFKI_TICKET_SERVICE_URL, input,
			new String[]{String.valueOf(MediaType.APPLICATION_XML),
			    String.valueOf(MediaType.APPLICATION_JSON)});
	    } catch (Exception e) {
		e.printStackTrace();
		throw new ServiceConnectionException("DfkiTicket", e.getMessage());
	    }
	    InputStream rdfStream = new ByteArrayInputStream(response.getBytes("utf-8"));
	    RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
	    Model model = new LinkedHashModel();
	    parser.setRDFHandler(new StatementCollector(model));
	    parser.parse(rdfStream, SERVICE_URL);
	    return createTicketFromModel(model, targetService);
	} catch (Exception ex) {
	   return new ResponseEntity("Could not get ticket: " + ex.getMessage(),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
