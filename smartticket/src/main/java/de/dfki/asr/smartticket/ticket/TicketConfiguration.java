package de.dfki.asr.smartticket.ticket;

import lombok.Getter;
import lombok.Setter;

public class TicketConfiguration {
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
