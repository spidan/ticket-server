
package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.service.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfigureMappingServiceEndpoint {

	@RequestMapping(
		value = "/rmlService",
			method = RequestMethod.POST
		)
	public ResponseEntity<?> setRMLService(@RequestParam final String serviceURL) {
		try {
			Utils.setRmlServiceUrl(serviceURL);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
