package com.rossotti.basketball.mapper.pub;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;

public class JsonProviderTest {
	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void deserializeLocalDate_JsonToPojo() {
		JsonObject jsonObject = createJsonObjectLocalDate();
		
		try {
			TestPojo pojo = mapper.readValue(jsonObject.toString(), TestPojo.class);
			Assert.assertEquals(new LocalDate(2015,11,12), pojo.getAsOfDate());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("IOException");
		}
	}

	@Test
	public void serializeLocalDate_PojoToJson() {
		TestPojo pojo = createTestPojoLocalDate();
		JsonObject jsonObject = createJsonObjectLocalDate();
		
		try {
			Assert.assertEquals(jsonObject.toString(), mapper.writeValueAsString(pojo));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail("JsonProcessingException");
		}
	}

	@Test
	public void deserializeArray_JsonToPojo() {
		JsonObject jsonObject = createJsonObjectArray();
		
		try {
			TestPojo pojo = mapper.readValue(jsonObject.toString(), TestPojo.class);
			Skill[] skills = pojo.getSkills();
			Assert.assertEquals("Java", skills[0].getType());
			Assert.assertEquals("developer", skills[1].getTitle());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("IOException");
		}
	}

	@Test
	public void serializeArray_PojoToJson() {
		TestPojo pojo = createTestPojoArray();
		JsonObject jsonObject = createJsonObjectArray();

		try {
			String generatedJson = mapper.writeValueAsString(pojo);
			Assert.assertEquals(jsonObject.toString(), generatedJson);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail("JsonProcessingException");
		}
	}

	private TestPojo createTestPojoArray() {
		TestPojo pojo = new TestPojo();
		Skill[] skills = new Skill[2];
		skills[0] = new Skill("Java", "programmer");
		skills[1] = new Skill("Python", "developer");
		pojo.setSkills(skills);
		return pojo;
	}
	
	private TestPojo createTestPojoLocalDate() {
		TestPojo pojo = new TestPojo();
		pojo.setAsOfDate(new LocalDate(2015,11,12));
		return pojo;
	}

	private JsonObject createJsonObjectLocalDate() {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject jsonObject = factory.createObjectBuilder()
			.add("asOfDate", "2015-11-12")
		.build();
		return jsonObject;
	}
	
	private JsonObject createJsonObjectArray() {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject jsonObject = factory.createObjectBuilder()
			.add("skills", factory.createArrayBuilder()
				.add(factory.createObjectBuilder()
					.add("type", "Java")
					.add("title", "programmer")
				)
				.add(factory.createObjectBuilder()
					.add("type", "Python")
					.add("title", "developer")
				)
			)
		.build();
		return jsonObject;
	}
	
	public static class TestPojo {
		@JsonCreator
		public TestPojo() {}

		@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
		private LocalDate asOfDate;
		private Skill[] skills;

		public LocalDate getAsOfDate() {
			return asOfDate;
		}
		public void setAsOfDate(LocalDate asOfDate) {
			this.asOfDate = asOfDate;
		}
		public Skill[] getSkills() {
			return skills;
		}
		public void setSkills(Skill[] skills) {
			this.skills = skills;
		}
	}
	
	public static class Skill {
		public Skill() {}
		public Skill(String type, String title) {
			this.type = type;
			this.title = title;
		}
		private String type;
		private String title;

		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}
}
