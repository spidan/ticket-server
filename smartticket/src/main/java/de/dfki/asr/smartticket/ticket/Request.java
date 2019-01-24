package de.dfki.asr.smartticket.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Request {
	
	private final String TICKET_URL = "http://localhost:8080/ticket";
	private final TicketConfiguration configuration;
	
	public Request(TicketConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public HttpResponse send() throws JsonProcessingException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(TICKET_URL);
		ObjectMapper mapper = new ObjectMapper();
		String configAsString = mapper.writeValueAsString(configuration);
		HttpEntity entity = new StringEntity(configAsString, ContentType.APPLICATION_JSON);
		postRequest.addHeader("Content-Type", "application/json");
		postRequest.addHeader("Authorization-Key", "46fd1c14-a985-4053-bc22-708f45b7d971");
		postRequest.setEntity(entity);
		return client.execute(postRequest);
	}
}
