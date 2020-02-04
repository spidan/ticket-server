package de.dfki.asr.smartticket.service;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.ByteArrayOutputStream;
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

	public byte[] receiveTicket(final String targetServerUri) throws IOException {
		TicketConfiguration config = new TicketConfiguration();
		config.getData(repo);
		Request ticketRequest = new Request(config, targetServerUri);
		HttpResponse response = ticketRequest.send();
		ByteArrayOutputStream ticketOutputStream = new ByteArrayOutputStream();
		response.getEntity().getContent().transferTo(ticketOutputStream);
		return ticketOutputStream.toByteArray();
	}
}
