package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import org.eclipse.rdf4j.model.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("TicketController")
public class TicketController {
    private TicketRepo ticketRepo;

    @PostMapping(value = "ticket"/*,consumes = "text/turtle"*/)
    public ResponseEntity<?> saveTicket(@RequestBody String rdfInput) {
        try {
            ticketRepo = new TicketRepo();
            Model model = Utils.turtleToRDFConverter(rdfInput);

            ticketRepo.save(model);
            Ticket ticket = Utils.getTicketFromDB(new Ticket(), ticketRepo);
            String jsonResult = Utils.convertObjectToJson(ticket);

            String xmlResult = Utils.convertObjectToXML(ticket);
            System.out.println(xmlResult);
            String dummyServiceURL = "";
//            ResponseEntity<String> responseEntity = Utils.postXmlData(dummyServiceURL, xmlResult);
            return new ResponseEntity<>(jsonResult, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Process Failed", HttpStatus.NOT_ACCEPTABLE);
        }
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
