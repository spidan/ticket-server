package de.dfki.asr.smartticket.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;

public final class Utils {
    private Utils() {

    }

    public static final String DFKI_SERVICE_CONN_ERR_MSG = "Connection to Dfki Ticket Service failed.";
    public static final String DFKI_TICKET_SERVICE_URL = "http://localhost:8801/ticket";
    public static final String XML_MEDIA_TYPE = String.valueOf(MediaType.APPLICATION_XML);
    public static final String JSON_MEDIA_TYPE = String.valueOf(MediaType.APPLICATION_JSON);
    public static final String CHARSET = String.valueOf(Charset.defaultCharset());

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
//        int responseCode = closeableHttpResponse.getStatusLine().getStatusCode();
        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), CHARSET);
        closeableHttpClient.close();
        closeableHttpResponse.close();

//        if (responseCode != HttpStatus.OK.value()) {
//            throw new Exception("Response code: " + responseCode + "\n" + responseString);
//        }

        return responseString;
    }
}
