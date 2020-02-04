package de.dfki.asr.smartticket.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import org.graalvm.compiler.core.common.SuppressFBWarnings;

public final class Utils {
    private Utils() {

    }

    public static final String DFKI_TICKET_SERVICE_URL = "http://localhost:8801/maptordf";

    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE",
		justification = "Unfixable external code")
    public static String sendPostRequest(final String url, final String data,
                                         final String[] contentTypes) throws Exception {
	String responseString;
	try (CloseableHttpClient closeableHttpClient = HttpClients.createDefault()) {
	    HttpPost httpPost = new HttpPost(url);
	    StringEntity stringEntity = new StringEntity(data);
	    httpPost.setEntity(stringEntity);
	    for (String contentType : contentTypes) {
		httpPost.setHeader("Accept", contentType);
		httpPost.setHeader("Content-type", contentType);
	    }
	    try (CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost)) {
		responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), Charset.defaultCharset());
	    }
	}
        return responseString;
    }
}
