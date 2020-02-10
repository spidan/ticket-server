package de.dfki.asr.smartticket.service;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.ticket.Request;
import de.dfki.asr.smartticket.ticket.TicketConfiguration;
import java.io.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketWrapper {

    @Autowired
    private TicketConfiguration ticketConfig;

    @Autowired
    private Request ticketRequest;

    private static final Logger LOG = LoggerFactory.getLogger(TicketWrapper.class);
    @Getter @Setter
    private InMemoryRepo repo;

    public TicketWrapper() {
    }

    public byte[] receiveTicket(final String targetServerName) throws IOException, URISyntaxException, Exception {
	ticketConfig.getTemplateForService(targetServerName);
	ticketConfig.getData(repo);
	ticketRequest.setUrlForService(targetServerName);
	ticketRequest.setConfiguration(ticketConfig);
	HttpResponse response = ticketRequest.send();
	ByteArrayOutputStream ticketOutputStream = new ByteArrayOutputStream();
	response.getEntity().getContent().transferTo(ticketOutputStream);
	return ticketOutputStream.toByteArray();
    }
}
