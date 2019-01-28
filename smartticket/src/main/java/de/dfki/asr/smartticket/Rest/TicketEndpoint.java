package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
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
	private static final String BASE_URI = "http://www.smartmaas.de";

	@RequestMapping(value = "/ticket",
			method = RequestMethod.POST,
			consumes = "text/turtle")
	@ResponseBody
	public String receiveTicket(@RequestBody final String model) {
		try {
			InputStream modelStream = new ByteArrayInputStream(model.getBytes("UTF-8"));
			Model inputModel = Rio.parse(modelStream, BASE_URI, RDFFormat.TURTLE);
			LOG.debug(inputModel.toString());
			return inputModel.toString();
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			return SpringExceptionHandlers.handleIoException(ex);
		} catch (RDFParseException ex) {
			LOG.error("Error parsing RDF: " + ex.getMessage());
			return SpringExceptionHandlers.handleRDFParseException(ex);
		} catch (UnsupportedRDFormatException ex) {
			LOG.error(ex.getMessage());
			return SpringExceptionHandlers.handleRDFFormatException(ex);
		}
	}

}
