package de.dfki.asr.smartticket.service;

import de.dfki.asr.smartticket.data.InMemoryRepo;
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

	public JsonTemplate(final String template) {
		this.templateAsString = template;
	}

	public JSONObject generateOutputRequest(final InMemoryRepo repo) {
		templateAsJson = new JSONObject(templateAsString);
		JSONObject properties = templateAsJson.getJSONObject("properties");
		JSONObject result = generateJsonFromProperties(properties, repo);
		LOG.info(result.toString());
		return resultJson;
	}

	private JSONObject generateJsonFromProperties(final JSONObject properties,
							final InMemoryRepo repo) {
		resultJson = new JSONObject();
		properties.keys().forEachRemaining(key -> {
			JSONObject propertyObject = properties.getJSONObject(key);
			appendPropertyToResult(key, propertyObject, repo);
			LOG.info(key + ": " + propertyObject.toString());
		});
		return resultJson;
	}

	private void appendPropertyToResult(final String key,
										final JSONObject property,
										final InMemoryRepo repo) {
		if (!property.has(ACCESSOR)) {
			resultJson.put(key, property.get(DEFAULT));
		} else {
			resultJson.put(key, replaceTemplatedValue(repo, property.getString(ACCESSOR)));
		}
	}

    private String replaceTemplatedValue(final InMemoryRepo repo, final String value) {
		String rdfValue = repo.getValue(value);
		LOG.debug(value);
		return rdfValue;
    }
}
