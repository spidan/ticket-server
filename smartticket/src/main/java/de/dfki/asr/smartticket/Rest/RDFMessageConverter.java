package de.dfki.asr.smartticket.Rest;

import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class RDFMessageConverter<T> extends AbstractHttpMessageConverter<T> {

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
