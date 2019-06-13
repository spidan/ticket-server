package de.dfki.asr.smartticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class TicketWrapper {
	private static final Logger LOG = LoggerFactory.getLogger(TicketWrapper.class);
	private final InMemoryRepo repo;

	public TicketWrapper(final InMemoryRepo processRepo) {
	    this.repo = processRepo;
	}

	public String receiveTicket() {
		TicketConfiguration config = new TicketConfiguration();
		config.getData(repo);
		Request ticketRequest = new Request(config);
		try {
			HttpResponse response = ticketRequest.send();
			return response.toString();
		} catch (JsonProcessingException ex) {
			LOG.error("Invalid JSON in input: " + ex.getMessage());
			return SpringExceptionHandlers.handleJsonException(ex);
		} catch (IOException ex) {
			LOG.error("Error in connection: " + ex.getMessage());
			return SpringExceptionHandlers.handleIoException(ex);
		}
	}
}
