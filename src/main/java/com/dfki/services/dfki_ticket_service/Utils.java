package com.dfki.services.dfki_ticket_service;

import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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

        ticket.setName(ticketRepo.getObject("sm", "offer1", "gr", "name"));
        ticket.setBegin(ticketRepo.getObject("sm", "offer1", "gr", "validFrom"));
        ticket.setEnd(ticketRepo.getObject("sm", "offer1", "gr", "validThrough"));
        ticket.setIncludes(ticketRepo.getObject("sm", "offer1", "gr", "includes"));
        ticket.setAccessedBus(ticketRepo.getObject("sm", ticket.getIncludes(), "tio", "accessTo"));
        ticket.setFromStation(ticketRepo.getObject("sm", ticket.getAccessedBus(), "tio", "from"));
        ticket.setToStation(ticketRepo.getObject("sm", ticket.getAccessedBus(), "tio", "to"));
        ticket.setType(ticketRepo.getType("sm", ticket.getIncludes(), "tio"));
        return ticket;
    }

    public static String convertObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(object, JsonNode.class);

        return jsonNode.toString().replaceAll(",", ",\n");

    }

    public static String convertObjectToXML(Object object) {
        String xmlResult = null;
        try {


            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            xmlResult = stringWriter.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlResult;
    }

    public static ResponseEntity<String> postXmlData(String url, String xmlData) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("ticket_xml", xmlData);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response;
    }
}
