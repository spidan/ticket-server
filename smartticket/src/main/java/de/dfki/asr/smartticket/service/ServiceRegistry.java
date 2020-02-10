package de.dfki.asr.smartticket.service;

import java.util.HashMap;

public class ServiceRegistry extends AbstractRegistry {

    public ServiceRegistry() {
	this.setServiceDict(new HashMap<>());
    }
}
