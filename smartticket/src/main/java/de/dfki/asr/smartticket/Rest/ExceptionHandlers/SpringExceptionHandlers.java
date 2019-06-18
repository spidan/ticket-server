package de.dfki.asr.smartticket.Rest.ExceptionHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Component
public final class SpringExceptionHandlers {

	private SpringExceptionHandlers() {

	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public static String handleIoException(final IOException ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static String handleJsonException(final JsonProcessingException ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static String handleRDFFormatException(final UnsupportedRDFormatException ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static String handleRDFParseException(final RDFParseException ex) {
		return ex.getMessage();
	}
}
