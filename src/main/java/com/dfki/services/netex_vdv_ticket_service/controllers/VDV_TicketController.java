package com.dfki.services.netex_vdv_ticket_service.controllers;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class VDV_TicketController {

    @PostMapping(value = "vdv/ticket", consumes = "application/xml")
    public ResponseEntity<?> savedTicket(@RequestBody String xmlInput) {
//        System.out.println(xmlInput);
        System.out.println("1");
        // Link for the DFKI ticket Service
        String dfki_service_url = "http://localhost:8800/ticket/in_xml";
        Utils.sendXMLPostRequest(dfki_service_url, xmlInput);

        return new ResponseEntity<>("Accepted XML Input is \n" + xmlInput, HttpStatus.ACCEPTED);
    }

}
