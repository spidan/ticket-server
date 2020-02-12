package de.dfki.asr.smartticket;

import de.dfki.asr.smartticket.data.RDFServiceRegistry;
import de.dfki.asr.smartticket.service.ServiceRegistry;
import de.dfki.asr.smartticket.service.TemplateRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
public class SmartTicketApplication {

    public static void main(final String[] args) {
	SpringApplication.run(SmartTicketApplication.class, args);
    }

    @Bean
    @Scope("singleton")
    public TemplateRegistry templateRegistry() {
	return new TemplateRegistry();
    }

    @Bean
    @Scope("singleton")
    public ServiceRegistry serviceRegistry() {
	return new ServiceRegistry();
    }

    @Bean
    @Scope("singleton")
    public RDFServiceRegistry rdfServiceRegistry() {
	return new RDFServiceRegistry();
    }
}
