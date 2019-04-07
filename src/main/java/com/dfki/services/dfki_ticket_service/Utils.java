package com.dfki.services.dfki_ticket_service;

import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static Model turtleToRDFConverter(String input) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Model model = null;
        model = Rio.parse(inputStream, "", RDFFormat.TURTLE);

        return model;
    }

    public static Ticket rdfToTicketConverter(Model model) {
        Ticket ticket = new Ticket();

        for (Statement statement : model) {
            System.out.println(statement);
            IRI property = statement.getPredicate();
            Value value = statement.getObject();
            if (value instanceof Literal) {
                Literal literal = (Literal) value;
//                System.out.println("Data Type: " + literal.getDatatype());
                if (property.getLocalName().equals("name")) {
                    String name = literal.getLabel();
                    ticket.setName(name);
                } else if (property.getLocalName().equals("validFrom")) {
                    String begin = literal.getLabel();
                    ticket.setBegin(begin);
//                    System.out.println("validfrom Date: " + date);
                } else if (property.getLocalName().equals("validThrough")) {
                    String end = literal.getLabel();
                    ticket.setEnd(end);
//                    System.out.println("validThrough Date: " + date);
                }
//
//                System.out.println("Lexical value: '" + literal.getLabel() + "'");
//            }
            }
        }
        return ticket;

    }

    public static Ticket getTicketFromDB(Ticket ticket, TicketRepo ticketRepo) {
        ticket.setName(ticketRepo.getValue("name"));
        ticket.setBegin(ticketRepo.getValue("validFrom"));
        ticket.setEnd(ticketRepo.getValue("validThrough"));
        return ticket;
    }

    public static String convertObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(object, JsonNode.class);

        return jsonNode.toString().replaceAll(",", ",\n");

    }
}
