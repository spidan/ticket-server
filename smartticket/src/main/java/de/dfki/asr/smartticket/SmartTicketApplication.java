package de.dfki.asr.smartticket;

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
}
