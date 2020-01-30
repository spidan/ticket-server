package com.dfki.services.RmlMappingService;

import be.ugent.rml.Executor;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.RDF4JStore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.graalvm.compiler.core.common.SuppressFBWarnings;

public final class Utils {
    private Utils() {

    }

    public static final String TURTLE_MEDIA_TYPE = "text/turtle";

    private static final String WORKING_DIRECTORY =
            new File(Utils.class.getResource("/application.properties").getFile()).getParentFile().getAbsolutePath();

    public static Model turtleToRDFConverter(final String input) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return Rio.parse(inputStream, "", RDFFormat.TURTLE);
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
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes(Charset.defaultCharset()));
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

        Executor executor = new Executor(rmlStore, new RecordsFactory(WORKING_DIRECTORY),
                functionLoader, outputStore,
                be.ugent.rml.Utils.getBaseDirectiveTurtle(mappingStream));

        QuadStore result = executor.execute(executor.getTriplesMaps());
        result.removeDuplicates();
        result.setNamespaces(rmlStore.getNamespaces());

        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8);
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

	@SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE",
				justification = "Unfixable external code")
	public static String sendGetRequest(final String uri) throws IOException {
		HttpGet request = new HttpGet(uri);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		request.addHeader("Accept", "text/turtle");
		try (CloseableHttpResponse response = httpClient.execute(request)) {
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
		return result;
	    }
	}
}

