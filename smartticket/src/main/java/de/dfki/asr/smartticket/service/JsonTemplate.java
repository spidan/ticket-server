package de.dfki.asr.smartticket.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTemplate {

	private static final Logger LOG = LoggerFactory.getLogger(JsonTemplate.class);

	private static final String ACCESSOR = "accessor";
	private static final String DEFAULT = "default";

	private final String templateAsString;
	private JSONObject templateAsJson;
	private JSONObject resultJson;

	public void doStuffWithJSON(final String schemaString) {
		templateAsJson = new JSONObject(schemaString);
	public JsonTemplate(final String template) {
		this.templateAsString = template;
	}

		JSONObject properties = templateAsJson.getJSONObject("properties");
		JSONObject result = generateJsonFromProperties(properties);
		LOG.info(result.toString());
		templateAsJson.keys().forEachRemaining(key -> {
			if ("accessor".equals(key)) {
				String queryString = templateAsJson.get(key).toString();
				LOG.info(queryString);
			}
			LOG.info(key);
		});
	}

	private JSONObject generateJsonFromProperties(final JSONObject properties) {
		resultJson = new JSONObject();
		properties.keys().forEachRemaining(key -> {
			JSONObject propertyObject = properties.getJSONObject(key);
			appendPropertyToResult(key, propertyObject);
			LOG.info(key + ": " + propertyObject.toString());
		});
		return resultJson;
	}

	private void appendPropertyToResult(final String key, final JSONObject property) {
		if (!property.has(ACCESSOR)) {
			resultJson.append(key, property.get(DEFAULT));
		}
		resultJson.append(key, property.get(ACCESSOR));
	}
}
