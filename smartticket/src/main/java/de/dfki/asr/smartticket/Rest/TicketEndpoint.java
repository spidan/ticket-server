package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TicketEndpoint {
	private static final Logger LOG = LoggerFactory.getLogger(TicketEndpoint.class);

	@RequestMapping(value = "/ticket",
			method = RequestMethod.POST,
			consumes = "text/turtle")
	@ResponseBody
	public String receiveTicket(@RequestBody final Model model) {
		try {
			LOG.debug(model.toString());
			return model.toString();
		} catch (RDFParseException ex) {
			LOG.error("Error parsing RDF: " + ex.getMessage());
			return SpringExceptionHandlers.handleRDFParseException(ex);
		} catch (UnsupportedRDFormatException ex) {
			LOG.error(ex.getMessage());
			return SpringExceptionHandlers.handleRDFFormatException(ex);
		}
	}

}
