package com.dfki.services.dfki_ticket_service.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

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
    @JsonIgnore
    @XmlElement
    private Map<String, String> prefixes;

    @Override
    public String toString() {
        return "Ticket{"
                + "apiToken='" + apiToken + '\''
                + ", begin='" + begin + '\''
                + ", end='" + end + '\''
                + ", name='" + name + '\''
                + ", iata='" + iata + '\''
                + ", includes='" + includes + '\''
                + ", accessedBus='" + accessedBus + '\''
                + ", fromStation='" + fromStation + '\''
                + ", toStation='" + toStation + '\''
                + ", type='" + type + '\''
                + '}';
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

    public void setBegin(final String beginStr) {
        this.begin = beginStr;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(final String endStr) {
        this.end = endStr;
    }

    public String getName() {
        return name;
    }

    public void setName(final String nameStr) {
        this.name = nameStr;
    }

    public String getIncludes() {
        if (includes != null && includes.contains("#")) {
            return includes.substring(includes.indexOf("#") + 1);
        }
        return includes;
    }

    public void setIncludes(final String includesStr) {
        this.includes = includesStr;
    }

    public String getAccessedBus() {
        if (accessedBus != null && accessedBus.contains("#")) {
            return accessedBus.substring(accessedBus.indexOf("#") + 1);
        }
        return accessedBus;
    }

    public void setAccessedBus(final String accessedBusStr) {
        this.accessedBus = accessedBusStr;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(final String toStationStr) {
        this.toStation = toStationStr;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(final String fromStationStr) {
        this.fromStation = fromStationStr;
    }

    public String getType() {
        if (type != null && type.contains("#")) {
            return type.substring(type.indexOf("#") + 1);
        }
        return type;
    }

    public void setType(final String typeStr) {
        this.type = typeStr;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(final Map<String, String> prefixesMap) {
        this.prefixes = prefixesMap;
    }
}
