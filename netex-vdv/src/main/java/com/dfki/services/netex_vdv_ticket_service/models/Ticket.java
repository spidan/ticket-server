package com.dfki.services.netex_vdv_ticket_service.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Ticket {

	@Getter@Setter
	private final String apiToken = "TICKET_API_TOKEN_3_STRING";
	@Getter@Setter
	private String begin, end;
	@Getter@Setter
	private String name;
	@Getter@Setter
	private final String iata = "ignored field";
	@Setter
	private String includes;
	@XmlElement
	@Setter
	private String accessedBus;
	@XmlElement
	@Getter@Setter
	private String fromStation, toStation;
	@XmlElement
	@Setter
	private String type;
	@XmlElement
	@Getter@Setter
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

	public String getIncludes() {
		if (includes.contains("#")) {
			return includes.substring(includes.indexOf("#") + 1);
		}
		return includes;
	}

	public String getAccessedBus() {
		if (accessedBus.contains("#")) {
			return accessedBus.substring(accessedBus.indexOf("#") + 1);
		}
		return accessedBus;
	}

	public String getType() {
		if (type.contains("#")) {
			return type.substring(type.indexOf("#") + 1);
		}
		return type;
	}

}
