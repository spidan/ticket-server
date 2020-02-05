package de.dfki.asr.smartticket.ticket;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.service.TemplateRegistry;
import de.dfki.asr.smartticket.service.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class TicketConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TicketConfiguration.class);
    private static final String API_TOKEN = "TICKET_API_TOKEN_3_STRING";
    private static final String NAME = "Dummyticket zum Servicetesten";
    private static final String IATA = "aktuell ignoriertes Feld";

    private byte[] template;

    @Autowired
    private ApplicationContext context;

    @Getter
    @Setter
    private String apiToken;
    @Getter
    @Setter
    private String begin;
    @Getter
    @Setter
    private String end;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String iata;

    public TicketConfiguration() {

    }

    public void getTemplateForService(final URI serviceUri) throws UnsupportedEncodingException, Exception {
	TemplateRegistry registry = (TemplateRegistry) context.getBean("templateRegistry");
	URI templateUri = registry.getTemplate(serviceUri);
	this.template = Utils.sendGetRequest(templateUri.toString()).getBytes("utf-8");
	JSONObject tAsJson = new JSONObject(new String(this.template, "utf-8"));
	LOG.debug(tAsJson.toString());
    }

    public void getData(final InMemoryRepo repo) {
	begin = repo.getValue("validFrom");
	end = repo.getValue("validThrough");
	apiToken = API_TOKEN;
	name = NAME;
	iata = IATA;
    }
}
