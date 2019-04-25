package com.dfki.services.netex_vdv_ticket_service.tests;

import com.dfki.services.netex_vdv_ticket_service.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;


public class UtilTests {
    @Test
    public void postRequestTest() {
        String validURI = "http://localhost:8800/ticket/in_xml";
        String validXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        String randomXml = Utils.getRandomString(70);
        try {
            Assert.assertEquals(HttpStatus.OK.value(), Utils.sendXMLPostRequest(validURI, validXml));

            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), Utils.sendXMLPostRequest(validURI, randomXml));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
