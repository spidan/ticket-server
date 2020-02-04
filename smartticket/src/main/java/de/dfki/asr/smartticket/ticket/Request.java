package de.dfki.asr.smartticket.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Request {

	private String ticketUrl = "http://localhost:8083/ticket";
	private final TicketConfiguration configuration;

	public Request(final TicketConfiguration config, final String targetServerUri) {
		this.ticketUrl = targetServerUri;
		this.configuration = config;
	}

	public HttpResponse send() throws JsonProcessingException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(ticketUrl);
		ObjectMapper mapper = new ObjectMapper();
		String configAsString = mapper.writeValueAsString(configuration);
		HttpEntity entity = new StringEntity(configAsString, ContentType.APPLICATION_JSON);
		postRequest.addHeader("Content-Type", "application/json");
		postRequest.addHeader("Authorization-Key", "46fd1c14-a985-4053-bc22-708f45b7d971");
		postRequest.setEntity(entity);
		return client.execute(postRequest);
	}
}
