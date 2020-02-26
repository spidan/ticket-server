package de.dfki.asr.smartticket.ticket;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import de.dfki.asr.smartticket.data.RDFServiceRegistry;
import de.dfki.asr.smartticket.service.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
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
	JSONObject tAsJson = new JSONObject(templateAsString);
	tAsJson.keys().forEachRemaining(key -> {
	    Object value = tAsJson.get(key);
	    if (value instanceof String) {
		value = replaceTemplatedValue(repo, (String) value);
		tAsJson.put(key, value);
	    }
	});
	this.requestObject = tAsJson;
	LOG.debug(tAsJson.toString());
    }

    private String replaceTemplatedValue(final InMemoryRepo repo, final String value) {
	if (!('@' == value.charAt(0))) {
	    return value;
	}
	String rdfString = value.substring(value.indexOf('{') + 1, value.indexOf('}'));
	String rdfValue = repo.getValue(rdfString);
	LOG.debug(value);
	return rdfValue;
    }
}
