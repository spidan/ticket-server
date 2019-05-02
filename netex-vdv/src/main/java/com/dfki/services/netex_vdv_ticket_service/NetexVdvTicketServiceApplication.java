package com.dfki.services.netex_vdv_ticket_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class NetexVdvTicketServiceApplication {
	public static void main(final String[] args) {
		SpringApplication.run(NetexVdvTicketServiceApplication.class, args);
		System.out.println("PROJECT READY!!!");
	}
}
