package com.dfki.services.RmlMappingService.controllers;

import io.micrometer.core.instrument.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MappingFileProvider {

	@GetMapping(value = "/mappingfile", produces = {"text/turtle"})
	public ResponseEntity<?> getMappingFile(@RequestParam final String filename) {
		File mappingFile = new File(getTargetDir().concat(filename));
		try (InputStream mappingStream = new FileInputStream(mappingFile)) {
			return new ResponseEntity<>(IOUtils.toString(mappingStream), HttpStatus.OK);
		} catch (FileNotFoundException ex) {
			return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
		} catch (IOException ex) {
			return new ResponseEntity<>("Error processing file: " + ex.getMessage(),
							HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	private String getTargetDir() {
		String workingDirPath = System.getProperty("user.dir");
		String targetDirPath = workingDirPath.concat("/target/");
		return targetDirPath;
	}
}
