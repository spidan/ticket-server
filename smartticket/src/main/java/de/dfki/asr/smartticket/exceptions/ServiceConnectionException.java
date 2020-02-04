package de.dfki.asr.smartticket.exceptions;

public class ServiceConnectionException extends RuntimeException {
    private final String serviceName;

    public ServiceConnectionException(final String serviceNameParam, final String exceptionMessage) {
        super(exceptionMessage);
        this.serviceName = serviceNameParam;
    }

    public String getServiceName() {
        return serviceName;
    }
}
