package com.rossotti.basketball.resource.dao;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.http.ContentType;
import com.rossotti.basketball.dao.model.Team;

public class OfficialResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByNameDate_Found() {
		expect().
			statusCode(200).
			body("number", equalTo("714")).
			body("fromDate", equalTo("2009-10-29")).
		when().
			get("/officials/Ginóbili/Johnny/2009-10-29");
	}

	@Test
	public void findByNameDate_NotFound() {
		expect().
			statusCode(404).
		when().
		get("/officials/Ginóbili/Johnny/2009-10-28");
	}

	@Test
	public void findByNameDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/officials/Ginóbili/Johnny/2009-10");
	}

	@Test
	public void findByDate_Found() {
		String response = get("/officials/2009-07-01").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<Team> officials = JsonPath.read(document, "$.officials");
		String lastName = JsonPath.read(document, "$.officials[0].lastName");
		Assert.assertTrue("Officials size " + officials.size() + " should be at least 49", officials.size() >= 49);
		Assert.assertEquals("Brown", lastName);
	}

	@Test
	public void findByDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/officials/1988-10-01");
	}

	@Test
	public void findByDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/officials/2012-07");
	}

	@Test
	public void createOfficial_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))).
			body(createJsonOfficial("Ginóbili", "Kevin").toString()).
		when().
			post("/officials");
	}

	@Test
	public void createOfficial_Found() {
		expect().
			statusCode(403).
		given().
			contentType(ContentType.JSON).
			config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))).
			body(createJsonOfficial("Ginóbili", "Johnny").toString()).
		when().
			post("/officials");
	}

	@Test
	public void updateOfficial_Updated() {
		expect().
			statusCode(204).
		given().
			contentType(ContentType.JSON).
			body(updateJsonOfficial("Collins", "Derrick").toString()).
		when().
			put("/officials");
	}

	@Test
	public void updateOfficial_NotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(updateJsonOfficial("Collins", "Tom").toString()).
		when().
			put("/officials");
	}

	@Test
	public void deleteOfficial_Deleted() {
		expect().
			statusCode(204).
		when().
			delete("/officials/Jones/Billy/2009-10-29");
	}

	@Test
	public void deleteOfficial_NotFound() {
		expect().
			statusCode(404).
		when().
			delete("/officials/Taylor/Shirley/2013-11-01");
	}

	private static JsonObject createJsonOfficial(String lastName, String firstName) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("lastName", lastName)
			.add("firstName", firstName)
			.add("fromDate", "2013-07-01")
			.add("toDate", "9999-12-30")
			.add("number", "321")
		.build();
		return value;
	}

	private static JsonObject updateJsonOfficial(String lastName, String firstName) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("lastName", lastName)
				.add("firstName", firstName)
				.add("fromDate", "1999-11-02")
				.add("toDate", "2020-12-31")
				.add("number", "11")
		.build();
		return value;
	}
}
