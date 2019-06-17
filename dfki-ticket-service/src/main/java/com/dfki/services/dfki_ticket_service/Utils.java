package com.dfki.services.dfki_ticket_service;

import be.ugent.rml.DataFetcher;
import be.ugent.rml.Executor;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.RDF4JStore;
import com.dfki.services.dfki_ticket_service.models.Ticket;
import com.dfki.services.dfki_ticket_service.repositories.TicketRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Utils {
    private Utils() {

    }

    public static final String RDF_REPO_ERR_MSG = "Couldn't create repository for RDF models.";
    public static final String VDV_SERVICE_CONN_ERR_MSG = "Connection to Vdv service failed.";
    public static final String INVALID_TURTLE_ERROR_MSG = "Turtle input is probably invalid."
            + " It is not converted to a RDF model successfully.";
    public static final String JSON_XML_ERR_MSG = "The input is neither JSON nor XML.";
    public static final String SMART_TICKET_CONN_ERR_MSG = "Connection to SmartTicket "
            + "service failed. ";
    public static final String MAPPING_ERR_MSG = "Mapping process failed. " +
            "Most likely, mapping file is not proper for the input.";
    public static final String CHARSET = String.valueOf(Charset.defaultCharset());
    public static final String XML_MEDIA_TYPE = String.valueOf(MediaType.APPLICATION_XML);
    public static final String SMART_TICKET_URL = "http://localhost:8090/ticket";
    public static final String TURTLE_MEDIA_TYPE = "text/turtle";
    private static final String[] VDV_SERVICE_URIS = {"http://localhost:8802/vdv/ticket",
            "http://192.168.99.100:8802/vdv/ticket"};
    public static final String VDV_SERVICE_URI = VDV_SERVICE_URIS[0];

    private static final String WORKING_DIRECTORY =
            new File(Utils.class.getResource("/application.properties").getFile()).getParentFile().getAbsolutePath();

    public static Model turtleToRDFConverter(final String input) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return Rio.parse(inputStream, "", RDFFormat.TURTLE);
    }

    public static Ticket rdfToTicketConverter(final Model model) {
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

    public static Ticket getTicketFromDB(final Ticket ticket, final TicketRepo ticketRepo) {
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

    public static String convertFromWithUrlToWithoutUrl(final String prefix, final String value) {
        if (value.contains("#")) {
            return prefix + ":" + value.substring(value.indexOf("#") + 1);
        }
        return prefix + ":" + value;
    }

    public static String convertObjectToJson(final Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(object, JsonNode.class);

        return jsonNode.toString().replaceAll(",", ",\n");

    }

    public static String convertObjectToXML(final Object object) {
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

    public static String sendPostRequest(final String url, final String data,
                                         final String[] contentTypes) throws Exception {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(data);
        httpPost.setEntity(stringEntity);
        for (String contentType : contentTypes) {
            httpPost.setHeader("Accept", contentType);
            httpPost.setHeader("Content-type", contentType);
        }

        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
        int responseCode = closeableHttpResponse.getStatusLine().getStatusCode();
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), CHARSET);
        closeableHttpClient.close();
        if (responseCode != org.springframework.http.HttpStatus.OK.value()) {
            throw new Exception("Response code: " + responseCode + "\n" + responseString);
        }
        System.out.println("DFKI->Response code and string:");
        System.out.println(responseCode);
        System.out.println(responseString);

        return responseString;
    }

    public static Map<String, String> parsePrefixes(final String rdfInput) {
        final String prefixTitle = "@prefix ";
        Map<String, String> prefixes = new HashMap<>();
        while (rdfInput.contains(prefixTitle)) {
            int prefixIndex = rdfInput.indexOf(prefixTitle) + prefixTitle.length();
            int semicolonIndex = rdfInput.indexOf(":");
            int endOfLineIndex = rdfInput.indexOf("> .");
            String prefixValue = rdfInput.substring(prefixIndex, semicolonIndex);
            String url = rdfInput.substring(semicolonIndex, endOfLineIndex);
            url = url.substring(url.indexOf("<") + 1);
            prefixes.put(prefixValue, url);
        }
        return prefixes;
    }

    public static boolean isValidXml(final String xml) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(CHARSET));
            saxParser.parse(inputStream, new DefaultHandler());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return false;
        }
        return true;

    }

    public static boolean isValidJson(final String json) {
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

    public static void writeTextToFile(final String fileName, final String text) {
        try {
            File file = new File(WORKING_DIRECTORY + "/" + fileName);

            Path path = Paths.get(file.getPath());
            Files.write(path, Collections.singleton(text), Charset.forName("UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String mapToRDF(final String mappingFileName) throws Exception {
        String outputFilePath = WORKING_DIRECTORY + "/output.ttl";
        String mappingFile = WORKING_DIRECTORY + "/" + mappingFileName;

        InputStream mappingStream = new FileInputStream(mappingFile);
        Model model = Rio.parse(mappingStream, "", RDFFormat.TURTLE);
        RDF4JStore rmlStore = new RDF4JStore(model);

        FunctionLoader functionLoader = new FunctionLoader(null, null, new HashMap<>());

        RDF4JStore outputStore = new RDF4JStore();

        Executor executor = new Executor(rmlStore, new RecordsFactory(
                new DataFetcher(WORKING_DIRECTORY, rmlStore)),
                functionLoader, outputStore,
                be.ugent.rml.Utils.getBaseDirectiveTurtle(mappingStream));

        QuadStore result = executor.execute(executor.getTriplesMaps());
        result.removeDuplicates();
        result.setNamespaces(rmlStore.getNamespaces());

        FileWriter fileWriter = new FileWriter(outputFilePath, Charset.defaultCharset(), false);
        try {
            result.write(fileWriter, "turtle");
        } catch (Throwable throwable) {
            try {
                fileWriter.close();
            } catch (IOException ioEx) {
                if (throwable != ioEx) {
                    throwable.addSuppressed(ioEx);
                }
            }
            throw throwable;
        } finally {
            fileWriter.close();
        }

        Path outputPath = Paths.get(outputFilePath);
        byte[] encoded = Files.readAllBytes(outputPath);
        return new String(encoded, Charset.defaultCharset());
    }

}
