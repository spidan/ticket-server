package com.dfki.services.netex_vdv_ticket_service.services;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import com.dfki.services.netex_vdv_ticket_service.models.Ticket;
import java.io.IOException;

public class TicketService {

	private static final String DFKI_SERVICE_URL = "http://localhost:8800/ticket/in_xml";

	public void postToDFKITicketService(String text) throws IOException {
		Utils.sendXMLPostRequest(DFKI_SERVICE_URL, text);
	}

	public void postToDFKITicketService(Ticket ticket) throws IOException {
		Utils.sendXMLPostRequest(DFKI_SERVICE_URL, Utils.convertObjectToXML(ticket));
	}

	public Ticket xmlToTicket(String xml) {
		Ticket ticket = Utils.convertXmlToTicket(xml);
		return ticket;
	}
}
