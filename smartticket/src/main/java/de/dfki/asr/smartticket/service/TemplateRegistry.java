package de.dfki.asr.smartticket.service;

import java.util.HashMap;

public class TemplateRegistry extends AbstractRegistry {

    public TemplateRegistry() {
	this.setServiceDict(new HashMap<>());
    }
}
