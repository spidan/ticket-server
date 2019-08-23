package com.dfki.services.dfki_ticket_service.services;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.exceptions.InvalidInputException;
import com.dfki.services.dfki_ticket_service.exceptions.RmlMappingException;
import com.dfki.services.dfki_ticket_service.exceptions.ServiceConnectionException;
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

    public Ticket save(final String rdfInput) {

        Model model;
        try {
            model = Utils.turtleToRDFConverter(rdfInput);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidInputException("Tutrle", e.getMessage());
        }
//        Map<String, String> prefixes = Utils.parsePrefixes(rdfInput);
        ticketRepo.save(model);
        return Utils.getTicketFromDB(new Ticket(), ticketRepo);
//            ticket.setPrefixes(prefixes);
    }

    public String toJson(final Ticket ticket) {
        return Utils.convertObjectToJson(ticket);
    }

    public String toXml(final Ticket ticket) {
        return Utils.convertObjectToXML(ticket);
    }

    public void postToVdvService(final Ticket ticket) {
        try {
            Utils.sendPostRequest(Utils.VDV_SERVICE_URI, toXml(ticket), new String[]{MediaType.APPLICATION_XML});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceConnectionException("VdvTicket", e.getMessage());
        }
    }

    public String postToSmartTicketService(final String result) {
        try {
            return Utils.sendPostRequest(Utils.SMART_TICKET_URL, result,
                    new String[]{Utils.TURTLE_MEDIA_TYPE});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceConnectionException("SmartTicket", e.getMessage());
        }
    }

    public String xmlToRdf(final String xmlString) throws Exception {
        String mappingFile = "xml_mapping.ttl";
        String fileName = "xml_text.xml";
        Utils.writeTextToFile(fileName, xmlString);
        return Utils.mapToRDF(mappingFile);
    }

    public String jsonToRdf(final String jsonString) throws Exception {
        String mappingFile = "transport_mapping.ttl";
        String fileName = "json_text.json";
        Utils.writeTextToFile(fileName, jsonString);
        return Utils.mapToRDF(mappingFile);
    }

    public String convertToRdf(final String input) {
        try {
            if (Utils.isValidXml(input)) {
                return xmlToRdf(input);
            } else if (Utils.isValidJson(input)) {
                return jsonToRdf(input);
            } else {
                throw new InvalidInputException("XML or JSON", "");
            }
        } catch (InvalidInputException e) {
            throw e;
        } catch (Error | Exception e) {
            e.printStackTrace();
            throw new RmlMappingException(e.getMessage());
        }

    }
}
