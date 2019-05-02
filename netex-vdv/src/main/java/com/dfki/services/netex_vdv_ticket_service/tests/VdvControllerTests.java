package com.dfki.services.netex_vdv_ticket_service.tests;

import com.dfki.services.netex_vdv_ticket_service.controllers.VDV_TicketController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class VdvControllerTests {
    private MockMvc mockMvc;

    @InjectMocks
    private VDV_TicketController vdv_ticketController;
    private String invalidXmlSample = "This is invalid Xml";
    private String validXmlSample = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
            "    </prefixes>\n" +
            "</ticket>";

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vdv_ticketController).build();
    }

    @Test
    public void testInvalidXml_saveTicket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("vdv/ticket")
                .accept(MediaType.APPLICATION_XML)
                .content(invalidXmlSample))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    public void testValidXml_saveTicket() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("vdv/ticket")
                .accept(MediaType.APPLICATION_XML)
                .content(validXmlSample))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
