package com.dfki.services.dfki_ticket_service.exceptions;

public class ServiceConnectionException extends RuntimeException {
    private String serviceName;

    public ServiceConnectionException(final String serviceNameParam, final String exceptionMessage) {
        super(exceptionMessage);
        this.serviceName = serviceNameParam;
    }

    public String getServiceName() {
        return serviceName;
    }

}
