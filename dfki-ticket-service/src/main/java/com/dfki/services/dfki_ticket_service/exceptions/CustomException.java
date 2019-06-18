package com.dfki.services.dfki_ticket_service.exceptions;

public class CustomException extends RuntimeException {
    private final String message;

    public CustomException(final String messageParam) {
        this.message = messageParam;
    }

    public String getMessage() {
        return this.message;
    }
}
