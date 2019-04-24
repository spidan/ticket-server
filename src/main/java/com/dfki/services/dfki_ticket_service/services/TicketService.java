package com.dfki.services.dfki_ticket_service.services;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import org.eclipse.rdf4j.model.Model;

import java.io.IOException;
import java.util.Map;

public class TicketService {
    private TicketRepo ticketRepo;
    private final static String vdv_ticket_service_url = "http://localhost:8802/vdv/ticket";

    public TicketService() {
        ticketRepo = new TicketRepo();
    }

    public Ticket save(String rdfInput) throws IOException {
        Model model = Utils.turtleToRDFConverter(rdfInput);
        Map<String, String> prefixes = Utils.parsePrefixes(rdfInput);
        if (ticketRepo.save(model)) {
            Ticket ticket = Utils.getTicketFromDB(new Ticket(), ticketRepo);
            ticket.setPrefixes(prefixes);
            return ticket;
        }
        return null;
    }

    public String toJson(Ticket ticket) {
        return Utils.convertObjectToJson(ticket);
    }

    public String toXml(Ticket ticket) {
        return Utils.convertObjectToXML(ticket);
    }

    public void postToVdvService(Ticket ticket) throws IOException {

        Utils.sendXMLPostRequest(vdv_ticket_service_url, toXml(ticket));
    }
}
