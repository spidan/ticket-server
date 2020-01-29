package com.dfki.services.RmlMappingService.service;

import com.dfki.services.RmlMappingService.Utils;
import com.dfki.services.RmlMappingService.exceptions.InvalidInputException;
import com.dfki.services.RmlMappingService.exceptions.RmlMappingException;
import com.dfki.services.RmlMappingService.models.Ticket;
import com.taxonic.carml.engine.RmlMapper;
import com.taxonic.carml.logical_source_resolver.JsonPathResolver;
import com.taxonic.carml.model.TriplesMap;
import com.taxonic.carml.util.RmlMappingLoader;
import com.taxonic.carml.vocab.Rdf;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
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

	public Model jsonToRdf(final String jsonString) throws Exception {
		String mappingFile = "transport_mapping.ttl";
		InputStream recordStream = new ByteArrayInputStream(jsonString.getBytes("utf-8"));
		InputStream mappingStream = ClassLoader.getSystemResourceAsStream(mappingFile);
		String fileName = "json_text.json";
		Model carmlModel = mapJSONWithCarml(mappingStream, recordStream);
		Utils.writeTextToFile(fileName, jsonString);
		return carmlModel;
	}

	public Model mapJSONWithCarml(final InputStream mappingFileStream, final InputStream resourceStream) {
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
}
