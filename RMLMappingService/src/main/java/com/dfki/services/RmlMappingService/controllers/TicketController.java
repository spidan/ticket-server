package com.dfki.services.RmlMappingService.controllers;

import com.dfki.services.RmlMappingService.services.TicketService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TicketController {

	private static final String SERVICE_URL = "test";
	@Autowired
	private TicketService ticketService;

	@PostMapping(value = "/maptordf", consumes = {"application/xml", "application/json"})
	public Model mapToRdf(@RequestBody final String input) throws IOException {
		String result = ticketService.convertToRdf(input);
		InputStream rdfStream = new ByteArrayInputStream(result.getBytes("utf-8"));
		RDFParser parser = Rio.createParser(RDFFormat.TURTLE);
		Model model = new LinkedHashModel();
		parser.setRDFHandler(new StatementCollector(model));
		parser.parse(rdfStream, SERVICE_URL);
		return model;
	}
}
