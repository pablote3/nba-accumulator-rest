package com.rossotti.basketball.client.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonProvider extends JacksonJsonProvider {

	public JsonProvider() {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.info("----------------JsonProvider Loading----------------");
		setMapper(buildObjectMapper());
	}

	public static ObjectMapper buildObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JodaModule());

		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		return mapper;
	}	
}