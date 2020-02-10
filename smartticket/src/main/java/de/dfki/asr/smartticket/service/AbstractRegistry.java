package de.dfki.asr.smartticket.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractRegistry {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Map<String, URI> serviceDict;

    public AbstractRegistry() {
	serviceDict = new HashMap<>();
    }

    public void registerTemplate(final String service, final URI mapping) {
	serviceDict.put(service, mapping);
    }

    public URI getTemplate(final String service) {
	return serviceDict.get(service);
    }
}
