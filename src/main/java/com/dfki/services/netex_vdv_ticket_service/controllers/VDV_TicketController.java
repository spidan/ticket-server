package com.dfki.services.netex_vdv_ticket_service.controllers;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import com.dfki.services.netex_vdv_ticket_service.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController()
public class VDV_TicketController {
    //    @Autowired
    private TicketService ticketService;

    @RequestMapping
    @PostMapping(value = "vdv/ticket", consumes = "application/xml")
    public ResponseEntity<?> saveTicket(@RequestBody String ticket) {
        if (!Utils.isValidXml(ticket)) {
            return new ResponseEntity<>("Xml is not valid!!!", HttpStatus.NOT_ACCEPTABLE);
        }
        ticketService = new TicketService();
        try {
            ticketService.postToDFKITicketService(ticketService.xmlToTicket(ticket));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
