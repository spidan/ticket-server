package de.dfki.asr.smartticket.ticket;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(TicketConfiguration.class);
	private static final String API_TOKEN = "TICKET_API_TOKEN_3_STRING";
	private static final String NAME = "Dummyticket zum Servicetesten";
	private static final String IATA = "aktuell ignoriertes Feld";

	@Getter@Setter
	private String apiToken;
	@Getter@Setter
	private String begin;
	@Getter@Setter
	private String end;
	@Getter@Setter
	private String name;
	@Getter@Setter
	private String iata;

	public void getData(final InMemoryRepo repo) {
	    begin = repo.getValue("validFrom");
	    end = repo.getValue("validThrough");
	    apiToken = API_TOKEN;
	    name = NAME;
	    iata = IATA;
	}
}
