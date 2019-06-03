package com.dfki.services.dfki_ticket_service;

import be.ugent.rml.DataFetcher;
import be.ugent.rml.Executor;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.RDF4JStore;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

public class Utils {

    public static Model turtleToRDFConverter(String input) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Model model = null;
        model = Rio.parse(inputStream, "", RDFFormat.TURTLE);

        return model;
    }

    public static Ticket rdfToTicketConverter(Model model) {
        Ticket ticket = new Ticket();

        for (Statement statement : model) {
            System.out.println(statement);
            IRI property = statement.getPredicate();
            Value value = statement.getObject();
            if (value instanceof Literal) {
                Literal literal = (Literal) value;
//                System.out.println("Data Type: " + literal.getDatatype());
                if (property.getLocalName().equals("name")) {
                    String name = literal.getLabel();
                    ticket.setName(name);
                } else if (property.getLocalName().equals("validFrom")) {
                    String begin = literal.getLabel();
                    ticket.setBegin(begin);
//                    System.out.println("validfrom Date: " + date);
                } else if (property.getLocalName().equals("validThrough")) {
                    String end = literal.getLabel();
                    ticket.setEnd(end);
//                    System.out.println("validThrough Date: " + date);
                }
//
//                System.out.println("Lexical value: '" + literal.getLabel() + "'");
//            }
            }
        }
        return ticket;

    }

    public static Ticket getTicketFromDB(Ticket ticket, TicketRepo ticketRepo) {
        String name = ticketRepo.getObject("sm", "offer1", "gr", "name");
        name = convertFromWithUrlToWithoutUrl("gr", name);
        ticket.setName(name);

        String begin = ticketRepo.getObject("sm", "offer1", "gr", "validFrom");
        begin = convertFromWithUrlToWithoutUrl("gr", begin);
        ticket.setBegin(begin);

        String end = ticketRepo.getObject("sm", "offer1", "gr", "validThrough");
        end = convertFromWithUrlToWithoutUrl("gr", end);
        ticket.setEnd(end);

        String includes = ticketRepo.getObject("sm", "offer1", "gr", "includes");
        includes = convertFromWithUrlToWithoutUrl("gr", includes);
        ticket.setIncludes(includes);
        includes = includes.substring(includes.indexOf(":") + 1);

        String accessedBus = ticketRepo.getObject("sm", includes, "tio", "accessTo");
        accessedBus = convertFromWithUrlToWithoutUrl("tio", accessedBus);
        ticket.setAccessedBus(accessedBus);
        accessedBus = accessedBus.substring(accessedBus.indexOf(":") + 1);

        String fromStation = ticketRepo.getObject("sm", accessedBus, "tio", "from");
        fromStation = convertFromWithUrlToWithoutUrl("tio", fromStation);
        ticket.setFromStation(fromStation);

        String toStation = ticketRepo.getObject("sm", accessedBus, "tio", "to");
        toStation = convertFromWithUrlToWithoutUrl("tio", toStation);
        ticket.setToStation(toStation);

        String type = ticketRepo.getType("sm", includes, "tio");
        type = convertFromWithUrlToWithoutUrl("tio", type);
        ticket.setType(type);

        return ticket;
    }

    public static String convertFromWithUrlToWithoutUrl(String prefix, String value) {
        if (value.contains("#")) {
            return prefix + ":" + value.substring(value.indexOf("#") + 1);
        }
        return prefix + ":" + value;
    }

    public static String convertObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(object, JsonNode.class);

        return jsonNode.toString().replaceAll(",", ",\n");

    }

    public static String convertObjectToXML(Object object) {
        String xmlResult = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            xmlResult = stringWriter.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlResult;
    }

    public static void sendXMLPostRequest(String link, String xmlData) throws IOException {
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
        System.out.println("POST Response Code :  " + responseCode);

        System.out.println("POST Response Message : " + urlConnection.getResponseMessage());
        if (responseCode == HttpStatus.SC_OK) { //success

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("POST NOT WORKED");
        }
    }

    public static Map<String, String> parsePrefixes(String rdf_input) {

        Map<String, String> prefixes = new HashMap<>();
        while (rdf_input.contains("@prefix ")) {
            int prefixIndex = rdf_input.indexOf("@prefix ") + 8;
            int semicolonIndex = rdf_input.indexOf(":");
            int endOfLineIndex = rdf_input.indexOf("> .");
            String prefix = rdf_input.substring(prefixIndex, semicolonIndex);
            String url = rdf_input.substring(semicolonIndex, endOfLineIndex);
            url = url.substring(url.indexOf("<") + 1);
            prefixes.put(prefix, url);
            rdf_input = rdf_input.substring(endOfLineIndex + 3);
        }
        return prefixes;
    }

    public static boolean isXmlValid(String xml) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            saxParser.parse(inputStream, new DefaultHandler());
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    public static boolean isJsonValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void writeTextToFile(String fileName, String text) {
        try {
            Path file = Paths.get(fileName);
            Files.write(file, Collections.singleton(text), Charset.forName("UTF-8"));

//            Set<PosixFilePermission> ownerWritable = PosixFilePermissions.fromString("rw-r--r--");
//            FileAttribute<?> permissions = PosixFilePermissions.asFileAttribute(ownerWritable);
//            Files.setPosixFilePermissions(file,ownerWritable);

//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
//            bufferedWriter.write(text);
//            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static QuadStore mapToRDF(String mappingFile) throws IOException {
        String cwd = "C:/Workspaces/DFKI_Ticket_Service/src/main/resources"; //path to default directory for local files
        mappingFile = cwd + "/" + mappingFile;
        InputStream mappingStream = new FileInputStream(mappingFile);
        Model model = Rio.parse(mappingStream, "", RDFFormat.TURTLE);
        RDF4JStore rmlStore = new RDF4JStore(model);
        Executor executor = new Executor(rmlStore, new RecordsFactory(new DataFetcher(cwd, rmlStore)));
        QuadStore result = executor.execute(null, true);


        return result;
    }

}
