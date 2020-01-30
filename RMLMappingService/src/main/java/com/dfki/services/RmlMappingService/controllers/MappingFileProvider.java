package com.dfki.services.RmlMappingService.controllers;

import io.micrometer.core.instrument.util.IOUtils;
import java.io.InputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MappingFileProvider {

	@GetMapping(value = "/mappingfile", produces = {"text/turtle"})
	public String getMappingFile(@RequestParam final String filename) {
		InputStream mappingStream = ClassLoader.getSystemResourceAsStream(filename);
		return IOUtils.toString(mappingStream);
	}
}
