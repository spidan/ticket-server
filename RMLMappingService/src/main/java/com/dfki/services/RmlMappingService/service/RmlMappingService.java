package com.dfki.services.RmlMappingService.service;

import com.dfki.services.RmlMappingService.Utils;
import com.dfki.services.RmlMappingService.exceptions.InvalidInputException;
import com.dfki.services.RmlMappingService.exceptions.RmlMappingException;
import com.dfki.services.RmlMappingService.models.Ticket;
import org.springframework.stereotype.Component;


@Component
public class RmlMappingService {

    public RmlMappingService() {

    }

    public String toJson(final Ticket ticket) {
        return Utils.convertObjectToJson(ticket);
    }

    public String toXml(final Ticket ticket) {
        return Utils.convertObjectToXML(ticket);
    }

    public String xmlToRdf(final String xmlString) throws Exception {
        String mappingFile = "xml_mapping.ttl";
        String fileName = "xml_text.xml";
        Utils.writeTextToFile(fileName, xmlString);
        return Utils.mapToRDF(mappingFile);
    }

    public String jsonToRdf(final String jsonString) throws Exception {
        String mappingFile = "transport_mapping.ttl";
        String fileName = "json_text.json";
        Utils.writeTextToFile(fileName, jsonString);
        return Utils.mapToRDF(mappingFile);
    }

    public String convertToRdf(final String input) {
        try {
            if (Utils.isValidXml(input)) {
                return xmlToRdf(input);
            } else if (Utils.isValidJson(input)) {
                return jsonToRdf(input);
            } else {
                throw new InvalidInputException("XML or JSON", "");
            }
        } catch (InvalidInputException e) {
            throw e;
        } catch (Error | Exception e) {
            e.printStackTrace();
            throw new RmlMappingException(e.getMessage());
        }

    }
	public Model mapJSONWithCarml(InputStream mappingFileStream, InputStream resourceStream) {
		Set<TriplesMap> mapping
			= RmlMappingLoader
				.build()
				.load(RDFFormat.TURTLE, mappingFileStream);
		RmlMapper mapper
			= RmlMapper
				.newBuilder()
				.setLogicalSourceResolver(Rdf.Ql.JsonPath, new JsonPathResolver())
				.build();
		mapper.bindInputStream(resourceStream);
		Model result = mapper.map(mapping);
		return result;
	}

}
