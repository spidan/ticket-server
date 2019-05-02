package com.dfki.services.netex_vdv_ticket_service.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Ticket {

	private final String apiToken = "TICKET_API_TOKEN_3_STRING";
	private String begin, end;
	private String name;
	private final String iata = "ignored field";
	private String includes;
	@XmlElement
	private String accessedBus;
	@XmlElement
	private String fromStation, toStation;
	@XmlElement
	private String type;
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
			+ ", prefixes=" + prefixes
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

	public Map<String, String> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(Map<String, String> prefixes) {
		this.prefixes = prefixes;
	}
}
