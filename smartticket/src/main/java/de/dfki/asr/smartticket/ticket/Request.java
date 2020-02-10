package de.dfki.asr.smartticket.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dfki.asr.smartticket.service.ServiceRegistry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Request {

    @Autowired
    private ApplicationContext context;

    private String ticketUrl = "http://localhost:8083/ticket";

    @Getter @Setter
    private TicketConfiguration configuration;

    public Request() {
	this.configuration = new TicketConfiguration();
    }

    public void setUrlForService(final String serviceName) {
	ServiceRegistry services = (ServiceRegistry) context.getBean("serviceRegistry");
	this.ticketUrl = services.getTemplate(serviceName).toString();
    }

    public HttpResponse send() throws JsonProcessingException, IOException {
	CloseableHttpClient client = HttpClients.createDefault();
	HttpPost postRequest = new HttpPost(ticketUrl);
	HttpEntity entity = new StringEntity(configuration.getRequestObject().toString(),
		    ContentType.APPLICATION_JSON);
	postRequest.addHeader("Content-Type", "application/json");
	postRequest.addHeader("Authorization-Key", "46fd1c14-a985-4053-bc22-708f45b7d971");
	postRequest.setEntity(entity);
	return client.execute(postRequest);
    }
}
