package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import org.eclipse.rdf4j.model.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController("TicketController")
public class TicketController {
    private TicketService ticketService;

    @PostMapping(value = "ticket/in_rdf", consumes = "text/turtle")
    public ResponseEntity<?> saveTicket(@RequestBody String rdfInput) {
        try {
            ticketService = new TicketService();
            Ticket ticket = ticketService.save(rdfInput);

            String ticketJson = ticketService.toJson(ticket);

            ticketService.postToVdvService(ticket);

            return new ResponseEntity<>(ticketJson, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Process Failed", HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @PostMapping(value = "ticket/in_xml", consumes = "application/xml")
    public static ResponseEntity<?> saveTicket_XMLFormat(@RequestBody String xmlInput) {

        System.out.println(xmlInput);
        return new ResponseEntity<>(xmlInput, HttpStatus.ACCEPTED);

    }
//    @GetMapping("get/allTickets")
//    public List<Ticket> getAllTickets() {
//        List<Ticket> tickets = (List<Ticket>) ticketRepo.findAll();
//        return tickets;
//    }
//
//    @GetMapping("get/ticket/{id}")
//    public Ticket getTicket(@PathVariable String id) {
//
//        List<Ticket> tickets = (List<Ticket>) ticketRepo.findAll();
//        for (Ticket t : tickets) {
//            if (t.getId().equals(Long.valueOf(id))) {
//                return t;
//            }
//        }
//        return null;
//    }
//
//    @DeleteMapping("delete/ticket/{id}")
//    public ResponseEntity<?> deleteTicket(@PathVariable String id) {
//        try {
//            Ticket t = getTicket(id);
//            if (t != null) {
//                ticketRepo.delete(t);
//            } else {
//                return new ResponseEntity<>("No Item with ID:" + id, HttpStatus.ACCEPTED);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Process Failed", HttpStatus.NOT_ACCEPTABLE);
//        }
//        return new ResponseEntity<>("Data Deleted", HttpStatus.ACCEPTED);
//    }

}
