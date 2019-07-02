package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "/ticket", consumes = "text/turtle")
    public ResponseEntity<?> saveTicketRdf(@RequestBody final String rdfInput) {
        Ticket ticket = ticketService.save(rdfInput);
        String ticketJson = ticketService.toJson(ticket);
        ticketService.postToVdvService(ticket);
        return ResponseEntity.status(HttpStatus.OK).body(ticketJson);
    }

    @PostMapping(value = "/ticket", consumes = {"application/xml", "application/json"})
    public ResponseEntity<?> saveTicket(@RequestBody final String input) {
        String result = ticketService.convertToRdf(input);
        result = ticketService.postToSmartTicketService(result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
