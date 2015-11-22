package com.rossotti.basketball.util;

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
import com.rossotti.basketball.app.providers.JsonProvider;

public class JsonProviderTest {
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private static JSONObject jsonObject = new JSONObject();

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void onceExecuteBeforeAll() {
		jsonObject.put("asOfDate", "2015-11-12");
	}

	@Test
	public void deserializeJsonToPojo() {
		try {
			TestPojo pojo = mapper.readValue(jsonObject.toJSONString(), TestPojo.class);
			Assert.assertEquals(new LocalDate(2015,11,12), pojo.getAsOfDate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void serializePojoToJson() {
		TestPojo pojo = new TestPojo();
		pojo.setAsOfDate(new LocalDate(2015,11,12));
		
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
		private LocalDate asOfDate;

		public LocalDate getAsOfDate() {
			return asOfDate;
		}
		public void setAsOfDate(LocalDate asOfDate) {
			this.asOfDate = asOfDate;
		}
	}
	
}
