package com.dfki.services.dfki_ticket_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
public class DfkiTicketServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DfkiTicketServiceApplication.class, args);
        System.out.println("PROJECT is Running!!!");
    }

}
