package de.dfki.asr.smartticket.ticket;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TicketConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(TicketConfiguration.class);
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
}
