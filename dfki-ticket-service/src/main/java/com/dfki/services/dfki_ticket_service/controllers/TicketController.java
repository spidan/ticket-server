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

    @PostMapping(value = "dfki/ticket/service/ticket", consumes = "text/turtle")
    public ResponseEntity<?> saveTicketRdf(@RequestBody final String rdfInput) {
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
    public ResponseEntity<?> saveTicket(@RequestBody final String input) {
        String result = "";
        try {
            if (Utils.isValidXml(input)) {
                result = ticketService.xmlToRdf(input);
            } else if (Utils.isValidJson(input)) {
                result = ticketService.jsonToRdf(input);
            } else {
                return new ResponseEntity<>("The input is neither JSON nor XML!!!", HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Mapping process failed. Most likely, mapping file is "
                    + "not proper for the input.\n" + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (result.equals("")) {
            return new ResponseEntity<>("Mapping process failed. Most likely, mapping file is "
                    + "not proper for the input.\n", HttpStatus.NOT_ACCEPTABLE);
        } else {
            try {
                result = Utils.sendPostRequest(Utils.SMART_TICKET_URL, result,
                        new String[]{Utils.TURTLE_MEDIA_TYPE});
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
