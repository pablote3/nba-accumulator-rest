package com.rossotti.basketball;

import java.io.IOException;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ProviderJson {
	private static ObjectMapper mapper = new ObjectMapper();
	private static final LocalDate localDate = new LocalDate(2015,11,12);
	private static JSONObject jsonObject = new JSONObject();

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void onceExecuteBeforeAll() {
		mapper.registerModule(new JodaModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		jsonObject.put("localDate", "2015-11-12");
	}

	@Test
	public void deserializeJsonToPojo() {
		try {
			TestPojo pojo = mapper.readValue(jsonObject.toJSONString(), TestPojo.class);
			Assert.assertEquals(localDate, pojo.getLocalDate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void serializePojoToJson() {
		TestPojo pojo = new TestPojo();
		pojo.setLocalDate(localDate);
		
		try {
			Assert.assertEquals(jsonObject.toString(), mapper.writeValueAsString(pojo));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public static class TestPojo {
		@JsonCreator
		public TestPojo() {}

		@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
		public LocalDate localDate;
		public LocalDate getLocalDate() {
			return localDate;
		}
		public void setLocalDate(LocalDate localDate) {
			this.localDate = localDate;
		}
	}
	
}