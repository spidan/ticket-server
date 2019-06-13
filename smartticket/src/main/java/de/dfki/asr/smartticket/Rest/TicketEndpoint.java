package de.dfki.asr.smartticket.Rest;

import de.dfki.asr.smartticket.Rest.ExceptionHandlers.SpringExceptionHandlers;
import de.dfki.asr.smartticket.service.BookingProcess;
import de.dfki.asr.smartticket.service.TicketWrapper;
import de.dfki.asr.smartticket.service.Utils;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TicketEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(TicketEndpoint.class);

    @RequestMapping(value = "/ticket",
            method = RequestMethod.POST,
            consumes = "text/turtle")
    @ResponseBody
    public String receiveTicket(@RequestBody final Model model) {
        BookingProcess booking = new BookingProcess();
        booking.writeRequestToRepo(model);
        TicketWrapper ticket = new TicketWrapper(booking.getRepo());
        return ticket.receiveTicket();
    }

    @RequestMapping(value = "/ticket",
            method = RequestMethod.POST,
            consumes = {"application/xml", "application/json"})
    @ResponseBody
    public ResponseEntity<?> receiveXmlOrJsonTicket(@RequestBody final String input) {
        String response = "";
        try {
            response = Utils.sendPostRequest(Utils.DFKI_TICKET_SERVICE_URL, input,
                    new String[]{Utils.XML_MEDIA_TYPE, Utils.JSON_MEDIA_TYPE});
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(SpringExceptionHandlers.handleIoException(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
