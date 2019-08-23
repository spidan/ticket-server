package com.dfki.services.dfki_ticket_service.controllers;

import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.services.TicketService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class TicketController {
	private static final String SERVICE_URL = "test";
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
    public String convertTicket(@RequestBody final String input) throws IOException {
        String result = ticketService.convertToRdf(input);
	InputStream rdfStream = new ByteArrayInputStream(result.getBytes("utf-8"));
	RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
	Model model = new LinkedHashModel();
	parser.setRDFHandler(new StatementCollector(model));
	parser.parse(rdfStream, SERVICE_URL);
        // result = ticketService.postToSmartTicketService(result);
        return result;
    }
}
