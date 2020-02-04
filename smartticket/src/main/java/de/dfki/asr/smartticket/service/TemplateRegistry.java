package de.dfki.asr.smartticket.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {

    private final Map<URI, URI> serviceDict;

    public TemplateRegistry() {
	serviceDict = new HashMap<>();
    }

    public void registerTemplate(final URI service, final URI mapping) {
	serviceDict.put(service, mapping);
    }

    public URI getTemplate(final URI service) {
	return serviceDict.get(service);
    }
}
