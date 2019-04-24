package com.dfki.services.netex_vdv_ticket_service.tests;

import com.dfki.services.netex_vdv_ticket_service.services.TicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VdvControllerTest.class,secure = false)
public class VdvControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    private String ticketXmlSample = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ticket>\n" +
            "    <accessedBus>tio:linie124</accessedBus>\n" +
            "    <fromStation>tio:http://resource.for/SaarVV_Uni_Campus</fromStation>\n" +
            "    <toStation>tio:http://resource.for/SaarVV_Hauptbahnhof</toStation>\n" +
            "    <type>tio:TicketPlaceholder</type>\n" +
            "    <prefixes>\n" +
            "        <entry>\n" +
            "            <key>rdf</key>\n" +
            "            <value>http://www.w3.org/1999/02/22-rdf-syntax-ns#</value>\n" +
            "        </entry>\n" +
            "        <entry>\n" +
            "            <key>sm</key>\n" +
            "            <value>http://www.smartmaas.de/sm-ns#</value>\n" +
            "        </entry>\n" +
            "        <entry>\n" +
            "            <key>rdfs</key>\n" +
            "            <value>http://www.w3.org/2000/01/rdf-schema#</value>\n" +
            "        </entry>\n" +
            "        <entry>\n" +
            "            <key>tio</key>\n" +
            "            <value>http://purl.org/tio/ns#</value>\n" +
            "        </entry>\n" +
            "        <entry>\n" +
            "            <key>time</key>\n" +
            "            <value>http://www.w3.org/2006/time#</value>\n" +
            "        </entry>\n" +
            "        <entry>\n" +
            "            <key>gr</key>\n" +
            "            <value>http://purl.org/goodrelations/v1#</value>\n" +
            "        </entry>\n" +
            "    </prefixes>\n" +
            "</ticket>";
    @Test
    public void saveTicketTest(){

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8802/vdv/ticket")
                .accept(MediaType.APPLICATION_XML).content(ticketXmlSample)
                .contentType(MediaType.APPLICATION_XML);

        MvcResult result = null;
        try {
            result = mockMvc.perform(requestBuilder).andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

//        assertEquals("http://localhost/students/Student1/courses/1",
//                response.getHeader(HttpHeaders.LOCATION));
    }
}
