package de.dfki.asr.smartticket;

import de.dfki.asr.smartticket.Rest.RDFMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class AppConfig {

    @Bean
    public HttpMessageConverters customConverters() {
	HttpMessageConverter rdfConverter = new RDFMessageConverter<>();
	return new HttpMessageConverters(rdfConverter);
    }
}
