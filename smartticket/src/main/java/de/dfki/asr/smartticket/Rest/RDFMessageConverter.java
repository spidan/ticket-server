package de.dfki.asr.smartticket.Rest;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import java.io.IOException;
import java.nio.charset.Charset;

public class RDFMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    private static final String BASE_URI = "http://www.smartmaas.de";

    public RDFMessageConverter() {
	super(new MediaType("text", "turtle", Charset.forName("UTF-8")));
    }

    @Override
    protected boolean supports(final Class<?> clazz) {
	return true;
    }

    @Override
    protected T readInternal(final Class<? extends T> clazz, final HttpInputMessage inputMessage)
	    throws IOException, HttpMessageNotReadableException {
	T inputModel = (T) Rio.parse(inputMessage.getBody(), BASE_URI, RDFFormat.TURTLE);
	return inputModel;
    }

    @Override
    protected void writeInternal(final T t, final HttpOutputMessage outputMessage)
	    throws IOException, HttpMessageNotWritableException {
	outputMessage.getBody().write(t.toString().getBytes("UTF-8"));
    }
}
