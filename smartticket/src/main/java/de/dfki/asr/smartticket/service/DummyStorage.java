package de.dfki.asr.smartticket.service;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

public final class DummyStorage {
    private DummyStorage() {

    }

    public static final String DFKI_TICKET_SERVICE_URL = "http://localhost:8801/dfki/ticket/service/ticket";
    public static final String XML_MEDIA_TYPE = String.valueOf(MediaType.APPLICATION_XML);
    public static final String JSON_MEDIA_TYPE = String.valueOf(MediaType.APPLICATION_JSON);
    public static final String CHARSET = String.valueOf(Charset.defaultCharset());
}
