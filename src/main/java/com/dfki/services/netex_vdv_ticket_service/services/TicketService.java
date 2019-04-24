package com.dfki.services.netex_vdv_ticket_service.services;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import com.dfki.services.netex_vdv_ticket_service.models.Ticket;

import java.io.IOException;

public class TicketService {
    private static final String dfki_service_url = "http://localhost:8800/ticket/in_xml";

    public void postToDFKITicketService(String text) throws IOException {
        Utils.sendXMLPostRequest(dfki_service_url, text);
    }

    public Ticket xmlToTicket(String xml) {
        Ticket ticket = Utils.convertXmlToTicket(xml);
        return ticket;
    }

}
