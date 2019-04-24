package com.dfki.services.netex_vdv_ticket_service.controllers;

import com.dfki.services.netex_vdv_ticket_service.models.Ticket;
import com.dfki.services.netex_vdv_ticket_service.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController()
public class VDV_TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "vdv/ticket", consumes = "application/xml")
    public ResponseEntity<?> saveTicket(@RequestBody Ticket  ticket) {
        try {
            ticketService.postToDFKITicketService(ticket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Accepted XML Input is \n" /*+ xmlInput*/, HttpStatus.CREATED);
    }

}
