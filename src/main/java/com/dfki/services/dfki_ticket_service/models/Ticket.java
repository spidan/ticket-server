package com.dfki.services.dfki_ticket_service.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Ticket {

    private String apiToken = "TICKET_API_TOKEN_3_STRING";
    private String begin, end;
    private String name;
    private String iata = "ignored field";
    @JsonIgnore
    private String includes;
    @JsonIgnore
    @XmlElement
    private String accessedBus;
    @JsonIgnore
    @XmlElement
    private String fromStation, toStation;
    @JsonIgnore
    @XmlElement
    private String type;


    @Override
    public String toString() {
        return "Ticket{" +
                "apiToken='" + apiToken + '\'' +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", name='" + name + '\'' +
                ", iata='" + iata + '\'' +
                ", includes='" + includes + '\'' +
                ", accessedBus='" + accessedBus + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getIata() {
        return iata;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIncludes() {
        if (includes.contains("#")) {
            return includes.substring(includes.indexOf("#") + 1);
        }
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getAccessedBus() {
        if (accessedBus.contains("#")) {
            return accessedBus.substring(accessedBus.indexOf("#") + 1);
        }
        return accessedBus;
    }

    public void setAccessedBus(String accessedBus) {
        this.accessedBus = accessedBus;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getType() {
        if (type.contains("#")) {
            return type.substring(type.indexOf("#") + 1);
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
