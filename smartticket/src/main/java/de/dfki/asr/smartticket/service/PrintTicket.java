package de.dfki.asr.smartticket.service;

import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrintTicket {
	
	@RequestMapping(value = "/printTicket",
			method = RequestMethod.POST,
			consumes = "application/json")
	@ResponseBody
	public String receiveTicket(@RequestBody TicketConfiguration config) throws IOException {
		Request ticketRequest = new Request(config);
		return ticketRequest.send().toString();
	}
}
