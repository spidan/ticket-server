package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.Utils;
import com.dfki.services.dfki_ticket_service.exceptions.CustomException;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController()
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping(value = "/ticket", consumes = "text/turtle")
    public ResponseEntity<?> saveTicketRdf(@RequestBody final String rdfInput) {
        Ticket ticket = null;
        try {
            ticket = ticketService.save(rdfInput);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(Utils.INVALID_TURTLE_ERROR_MSG + "Additional info:"
                    + e.getMessage());
        }

        String ticketJson = ticketService.toJson(ticket);
        try {
            ticketService.postToVdvService(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(Utils.VDV_SERVICE_CONN_ERR_MSG + " Additional info:"
                    + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ticketJson);

    }

    @PostMapping(value = "/ticket", consumes = {"application/xml", "application/json"})
    public ResponseEntity<?> saveTicket(@RequestBody final String input) {
        String result = "";
        try {
            if (Utils.isValidXml(input)) {
                result = ticketService.xmlToRdf(input);
            } else if (Utils.isValidJson(input)) {
                result = ticketService.jsonToRdf(input);
            } else {
                throw new CustomException(Utils.JSON_XML_ERR_MSG);
            }
        } catch (Error | Exception e) {
            e.printStackTrace();
            throw new CustomException(Utils.MAPPING_ERR_MSG + "Additional info:"
                    + e.getMessage());
        }
        if (result.equals("")) {
            throw new CustomException(Utils.MAPPING_ERR_MSG);
        } else {
            try {
                result = Utils.sendPostRequest(Utils.SMART_TICKET_URL, result,
                        new String[]{Utils.TURTLE_MEDIA_TYPE});
            } catch (Exception e) {
                e.printStackTrace();

                throw new CustomException(Utils.SMART_TICKET_CONN_ERR_MSG + "Additional info:"
                        + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
