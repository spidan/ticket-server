package com.dfki.services.dfki_ticket_service.services;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import org.eclipse.rdf4j.model.Model;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Component
public class TicketService {
    private TicketRepo ticketRepo;

    public TicketService() {
        ticketRepo = new TicketRepo();
    }

    public Ticket save(final String rdfInput) throws IOException {
        Model model = Utils.turtleToRDFConverter(rdfInput);
//        Map<String, String> prefixes = Utils.parsePrefixes(rdfInput);
        ticketRepo.save(model);
        Ticket ticket = Utils.getTicketFromDB(new Ticket(), ticketRepo);
//            ticket.setPrefixes(prefixes);
        return ticket;
    }

    public String toJson(final Ticket ticket) {
        return Utils.convertObjectToJson(ticket);
    }

    public String toXml(final Ticket ticket) {
        return Utils.convertObjectToXML(ticket);
    }

    public void postToVdvService(final Ticket ticket) throws Exception {

        Utils.sendPostRequest(Utils.VDV_SERVICE_URI, toXml(ticket), new String[]{MediaType.APPLICATION_XML});
    }

    public String xmlToRdf(final String xmlString) throws Exception {
        String mappingFile = "xml_mapping.ttl";
        String fileName = "xml_text.xml";
        Utils.writeTextToFile(fileName, xmlString);
        return Utils.mapToRDF(mappingFile);
    }

    public String jsonToRdf(final String jsonString) throws Exception {
        String mappingFile = "json_mapping.ttl";
        String fileName = "json_text.json";
        Utils.writeTextToFile(fileName, jsonString);
        return Utils.mapToRDF(mappingFile);
    }
}
