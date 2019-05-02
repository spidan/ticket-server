package com.dfki.services.netex_vdv_ticket_service.services;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import com.dfki.services.netex_vdv_ticket_service.models.Ticket;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.slf4j.LoggerFactory;

public class TicketService {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TicketService.class);

	private static final String DFKI_SERVICE_URL = "http://localhost:8800/ticket/in_xml";

	public void postToDFKITicketService(final String text) throws IOException {
		Utils.sendXMLPostRequest(DFKI_SERVICE_URL, text);
	}

	public void postToDFKITicketService(final Ticket ticket) throws IOException {
		try {
			Utils.sendXMLPostRequest(DFKI_SERVICE_URL, Utils.convertObjectToXML(ticket));
		} catch (JAXBException ex) {
			LOG.error("Request to ticket service failed: " + ex.toString());
		}
	}

	public Ticket xmlToTicket(final String xml) {
		try {
			Ticket ticket = Utils.convertXmlToTicket(xml);
			return ticket;
		} catch (JAXBException ex) {
			LOG.error("Could not create ticket from XML: " + ex.toString());
		}
		return null;
	}
}
