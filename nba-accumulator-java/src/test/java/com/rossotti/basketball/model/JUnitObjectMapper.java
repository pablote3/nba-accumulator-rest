package com.rossotti.basketball.model;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JUnitObjectMapper {
	private static ObjectMapper mapper = new ObjectMapper();

	@BeforeClass
	public static void onceExecuteBeforeAll() {
		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deserializeJsonToPojo() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("date", "2015-11-12");

			TestPojo container = mapper.readValue(obj.toJSONString(), TestPojo.class);
			Assert.assertEquals(new LocalDate(2015,11,12), container.getDate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void serializeLocalDateToString() {
//		Container container = new Container();
//		container.setDate(new LocalDate(2015,11,11));
		
		LocalDate date = new LocalDate(2015,11,11);
		try {

			Assert.assertEquals("\"2015-11-11\"", mapper.writeValueAsString(date));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
