package com.rossotti.basketball;

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

public class UtilObjectMapper {
	private static ObjectMapper mapper = new ObjectMapper();
	private static final LocalDate localDate = new LocalDate(2015,11,12);
	private static JSONObject jsonObject = new JSONObject();

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void onceExecuteBeforeAll() {
		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		jsonObject.put("date", "2015-11-12");
	}

	@Test
	public void deserializeJsonToPojo() {
		try {
			ZTestPojo container = mapper.readValue(jsonObject.toJSONString(), ZTestPojo.class);
			Assert.assertEquals(localDate, container.getLocalDate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void serializePojoToJson() {
		ZTestPojo obj = new ZTestPojo();
		obj.setLocalDate(localDate);
		
		try {
			Assert.assertEquals(jsonObject.toString(), mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void serializeLocalDateToString() {
		try {
			Assert.assertEquals("\"2015-11-12\"", mapper.writeValueAsString(localDate));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
