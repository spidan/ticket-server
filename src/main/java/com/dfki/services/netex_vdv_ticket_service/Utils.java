package com.dfki.services.netex_vdv_ticket_service;

import com.dfki.services.netex_vdv_ticket_service.models.Ticket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static void sendXMLPostRequest(String link, String xmlData) {
        try {

            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/xml");

            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(xmlData.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = urlConnection.getResponseCode();
//            System.out.println("POST Response Code :  " + responseCode);
//
//            System.out.println("POST Response Message : " + urlConnection.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_ACCEPTED) { //success

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
//                System.out.println(response.toString());
            } else {
                System.out.println("POST NOT WORKED");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Ticket convertXmlToTicket(String xml) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            return (Ticket) unmarshaller.unmarshal(stringReader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
