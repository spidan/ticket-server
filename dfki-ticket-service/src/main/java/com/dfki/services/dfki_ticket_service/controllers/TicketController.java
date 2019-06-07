package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import java.io.IOException;


@RestController("TicketController")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "dfki/ticket/service/ticket", consumes = "text/turtle")
    public ResponseEntity<?> saveTicketRdf(@RequestBody String rdfInput) {
        try {
            Ticket ticket = ticketService.save(rdfInput);

            String ticketJson = ticketService.toJson(ticket);

            ticketService.postToVdvService(ticket);

            return new ResponseEntity<>(ticketJson, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Process Failed", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "dfki/ticket/service/ticket", consumes = {"application/xml", "application/json"})
    public ResponseEntity<?> saveTicket(@RequestBody String input) {
        System.out.println("------------saveTicket-------------");
        System.out.println(input);
        System.out.println("------------saveTicket-------------");
        String result = "null";
        if (Utils.isValidXml(input)) {
            try {
                result = ticketService.xmlToRdf(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("---------------------");
            System.out.println(input);
            System.out.println("Above Xml is converted into below RDF:");
            System.out.println(result);
            System.out.println("---------------------");

        } else if (Utils.isValidJson(input)) {
            try {
                result = ticketService.jsonToRdf(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("---------------------");
            System.out.println(input);
            System.out.println("Above Json is converted into below RDF:");
            System.out.println(result);
            System.out.println("---------------------");

        } else {
            return new ResponseEntity<>("The input is not valid!!!", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            String response = Utils.sendPostRequest("http://localhost:8090/ticket", result, "text/turtle");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
