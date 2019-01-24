package de.dfki.asr.smartticket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
			return handleJsonException(ex);
		} catch (IOException ex) {
			return handleIoException(ex);
		}
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	private String handleIoException(final IOException ex) {
		logger.error("Error in connection: " + ex.getMessage());
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private String handleJsonException(final JsonProcessingException ex) {
		logger.error("Invalid JSON in input: " + ex.getMessage());
		return ex.getMessage();
	}
}
