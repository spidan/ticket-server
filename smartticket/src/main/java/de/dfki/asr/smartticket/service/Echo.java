package de.dfki.asr.smartticket.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Echo {

	@GetMapping("/echo")
	@ResponseBody
	public String echoInput(@RequestParam(name = "message",
					required = false,
					defaultValue = "echoService") final String message) {
		return message;
	}
}
