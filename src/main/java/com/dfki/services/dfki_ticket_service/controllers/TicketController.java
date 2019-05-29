package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("TicketController")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "ticket/in_rdf", consumes = "text/turtle")
    public ResponseEntity<?> saveTicket(@RequestBody String rdfInput) {
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


    @PostMapping(value = "ticket/in_xml", consumes = "application/xml")
    public static ResponseEntity<?> saveTicket_XMLFormat(@RequestBody String xmlInput) {
        if (!Utils.isValidXml(xmlInput)) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        System.out.println(xmlInput);
        return new ResponseEntity<>(xmlInput, HttpStatus.OK);

    }

}
