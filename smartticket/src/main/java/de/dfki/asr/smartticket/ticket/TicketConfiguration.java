package de.dfki.asr.smartticket.ticket;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.data.RDFServiceRegistry;
import de.dfki.asr.smartticket.service.JsonTemplate;
import de.dfki.asr.smartticket.service.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(TicketConfiguration.class);

    private byte[] template;

    @Autowired
    private RDFServiceRegistry serviceRegistry;

    @Getter
    @Setter
    private Object requestObject;

    public TicketConfiguration() {

    }

    public void getTemplateForService(final String serviceName) throws UnsupportedEncodingException, Exception {
		URI templateUri = serviceRegistry.getTemplateUriForService(serviceName);
		this.template = Utils.sendGetRequest(templateUri.toString()).getBytes("utf-8");
    }

    public void getData(final InMemoryRepo repo) throws UnsupportedEncodingException {
		processJSONTemplate(repo);
    }

    private void processJSONTemplate(final InMemoryRepo repo) throws UnsupportedEncodingException {
		String templateAsString = new String(this.template, "utf-8");
		JsonTemplate tAsJson = new JsonTemplate(templateAsString);
		this.requestObject = tAsJson.generateOutputRequest(repo);
		LOG.debug(tAsJson.toString());
    }
}
