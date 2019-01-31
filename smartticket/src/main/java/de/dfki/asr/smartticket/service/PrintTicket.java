package de.dfki.asr.smartticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintTicket {
	private final Logger logger = LoggerFactory.getLogger(PrintTicket.class);

	public String receiveTicket(final TicketConfiguration config) {
		Request ticketRequest = new Request(config);
		try {
			HttpResponse response = ticketRequest.send();
			return response.toString();
		} catch (JsonProcessingException ex) {
			logger.error("Invalid JSON in input: " + ex.getMessage());
			return SpringExceptionHandlers.handleJsonException(ex);
		} catch (IOException ex) {
			logger.error("Error in connection: " + ex.getMessage());
			return SpringExceptionHandlers.handleIoException(ex);
		}
	}
}
