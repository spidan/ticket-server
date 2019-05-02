package com.dfki.services.netex_vdv_ticket_service;

import com.dfki.services.netex_vdv_ticket_service.models.Ticket;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.http.HttpStatus;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	private Utils() {

	}

	public static int sendXMLPostRequest(final String link, final String xmlData) throws IOException {
		URL url = new URL(link);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type", "application/xml");
		urlConnection.setDoOutput(true);
		try (OutputStream outputStream = urlConnection.getOutputStream()) {
			outputStream.write(xmlData.getBytes("UTF-8"));
			outputStream.flush();
		}
		int responseCode = urlConnection.getResponseCode();
		if (responseCode == HttpStatus.SC_OK) {
			StringBuffer response;
			try (
				BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), "UTF-8"))) {
				String inputLine;
				response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			}
			System.out.println(response.toString());
		} else {
			System.out.println("POST REQUEST FAILED!!!");
		}
		return responseCode;
	}

	public static Ticket convertXmlToTicket(final String xml) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(xml);
		return (Ticket) unmarshaller.unmarshal(stringReader);
	}

	public static String convertObjectToXML(final Object object) throws JAXBException {
		String xmlResult = null;
		JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(object, stringWriter);
		xmlResult = stringWriter.toString();
		return xmlResult;
	}

	public static boolean isValidXml(final String xml) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			saxParser.parse(inputStream, new DefaultHandler());
			return true;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			LOG.error(e.getMessage());
			return false;
		}
	}

	public static String getRandomString(final int length) {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRST".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String randomStr = sb.toString();
		return randomStr;
	}

	public static boolean isHttpUriValid(final String uri) {
		URL url;
		try {
			url = new URL(uri);
			return "http".equals(url.getProtocol());
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
			return false;
		}
	}
}
