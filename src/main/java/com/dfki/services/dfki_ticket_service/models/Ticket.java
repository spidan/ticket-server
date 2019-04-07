package com.dfki.services.dfki_ticket_service.models;


public class Ticket {

    private String apiToken = "TICKET_API_TOKEN_3_STRING";
    private String  begin, end;
    private String name;
    private String iata = "ignored field";

    public String getApiToken() {
        return apiToken;
    }

    public String getIata() {
        return iata;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String  begin) {
        this.begin = begin;
    }

    public String  getEnd() {
        return end;
    }

    public void setEnd(String  end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "begin=" + begin +
                ", end=" + end +
                ", name='" + name + '\'' +
                '}';
    }
}
