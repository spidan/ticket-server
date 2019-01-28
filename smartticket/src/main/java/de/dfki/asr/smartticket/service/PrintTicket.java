package de.dfki.asr.smartticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrintTicket {
	private final Logger logger = LoggerFactory.getLogger(PrintTicket.class);

	@RequestMapping(value = "/printTicket",
			method = RequestMethod.POST,
			consumes = "application/json")
	@ResponseBody
	public String receiveTicket(@RequestBody final TicketConfiguration config) {
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
