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
import com.rossotti.basketball.dao.model.Player;

public class PlayerResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByNameBirthdate_Found() {
		expect().
			statusCode(200).
			body("weight", equalTo("231")).
			body("birthplace", equalTo("Utėnai, Lithuania")).
		when().
			get("/players/Valančiūnas/Jonas/1992-05-06");
	}

	@Test
	public void findByNameBirthdate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/players/Valančiūnas/Johnny/2009-10-28");
	}

	@Test
	public void findByNameBirthdate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/players/Valančiūnas/Jonas/2009-10");
	}

	@Test
	public void findByName_Found() {
		String response = get("/players/Wright/Chris").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<Player> players = JsonPath.read(document, "$.players");
		String lastName = JsonPath.read(document, "$.players[0].lastName");
		Assert.assertTrue("Players size " + players.size() + " should be at least 2", players.size() >= 2);
		Assert.assertEquals("Wright", lastName);
	}

	@Test
	public void findNyName_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/players/Jones/Prince");
	}

	@Test
	public void createPlayer_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))).
			body(createJsonPlayer("Ginóbili", "Prince").toString()).
		when().
			post("/players");
	}

	@Test
	public void createPlayer_Found() {
		expect().
			statusCode(403).
		given().
			contentType(ContentType.JSON).
			config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"))).
			body(createJsonPlayer("Ginóbili", "Emanuel").toString()).
		when().
			post("/players");
	}

	@Test
	public void updatePlayer_Updated() {
		expect().
			statusCode(204).
		given().
			contentType(ContentType.JSON).
			body(updateJsonPlayer("Collins", "Jason").toString()).
		when().
			put("/players");
	}

	@Test
	public void updatePlayer_NotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(updateJsonPlayer("Collins", "Tom").toString()).
		when().
			put("/players");
	}

	@Test
	public void deletePlayer_Deleted() {
		expect().
			statusCode(204).
		when().
			delete("/players/Carter/Jimmy/1972-01-26");
	}

	@Test
	public void deletePlayer_NotFound() {
		expect().
			statusCode(404).
		when().
			delete("/players/Phillips/Juan/2013-11-01");
	}

	private static JsonObject createJsonPlayer(String lastName, String firstName) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("lastName", lastName)
			.add("firstName", firstName)
			.add("birthdate", "1997-07-01")
			.add("displayName", firstName + " " + lastName)
			.add("height", "79")
			.add("weight", 220)
			.add("birthplace", "Los Angeles, California, USA")
		.build();
		return value;
	}

	private static JsonObject updateJsonPlayer(String lastName, String firstName) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("lastName", lastName)
				.add("firstName", firstName)
				.add("birthdate", "1978-12-02")
				.add("displayName", firstName + " " + lastName)
				.add("height", "79")
				.add("weight", 220)
				.add("birthplace", "Bakersfield, California, USA")
		.build();
		return value;
	}
}
