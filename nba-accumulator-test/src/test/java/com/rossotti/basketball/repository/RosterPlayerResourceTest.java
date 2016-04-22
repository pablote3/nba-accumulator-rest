package com.rossotti.basketball.repository;

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
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Team;

public class RosterPlayerResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByPlayer_NameBirthdateAsOfDate_Found() {
		expect().
			statusCode(200).
			body("toDate", equalTo("2013-06-30")).
			body("position", equalTo("PG")).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10-07/2012-10-30");
	}

	@Test
	public void findByPlayer_NameBirthdateAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/player/Price/K.J./1986-10-07/2012-10-30");
	}

	@Test
	public void findByPlayer_NameBirthdateAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10-07/2012-10");
	}

	public void findByTeam_NameTeamAsOfDate_Found() {
		expect().
			statusCode(200).
			body("toDate", equalTo("2013-06-30")).
			body("position", equalTo("PG")).
		when().
			get("/rosterPlayers/team/Price/A.J./cleveland-cavaliers/2014-10-30");
	}

	@Test
	public void findByTeam_NameTeamAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/team/Price/A.J./miami-heat/2014-10-30");
	}

	@Test
	public void findByTeam_NameTeamAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/team/Price/A.J./cleveland-cavaliers/2012-10");
	}

	@Test
	public void findByPlayer_NameBirthdate_Found() {
		String response = get("/rosterPlayers/player/Price/A.J./1986-10-07").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<RosterPlayer> rosterPlayers = JsonPath.read(document, "$.rosterPlayers");
		String fromDate = JsonPath.read(document, "$.rosterPlayers[0].fromDate");
		Assert.assertTrue("Roster players size " + rosterPlayers.size() + " should be at least 2", rosterPlayers.size() >= 2);
		Assert.assertEquals("2012-10-30", fromDate);
	}

	@Test
	public void findByPlayer_NameBirthdate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/player/Jones/Prince/1986-10-07");
	}

	@Test
	public void findByPlayer_NameBirthdate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10");
	}

	@Test
	public void findByTeam_KeyAsOfDate_Found() {
		String response = get("/rosterPlayers/team/atlanta-hawks/2014-10-29").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<RosterPlayer> rosterPlayers = JsonPath.read(document, "$.rosterPlayers");
		String fromDate = JsonPath.read(document, "$.rosterPlayers[0].fromDate");
		Assert.assertTrue("Roster players size " + rosterPlayers.size() + " should be at least 15", rosterPlayers.size() >= 15);
		Assert.assertEquals("2014-10-29", fromDate);
	}

	@Test
	public void findByTeam_KeyAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/team/atlanta-falcons/2014-10-30");
	}

	@Test
	public void findByTeam_KeyAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/team/atlanta-hawks/1986-10");
	}

//	@Test
//	public void createPlayer_Found() {
//		expect().
//			statusCode(400).
//		given().
//			contentType(ContentType.JSON).
//			body(createJsonPlayer("Ginóbili", "Emanuel").toString()).
//		when().
//			post("/players");
//	}
//
//	@Test
//	public void updatePlayer_Updated() {
//		expect().
//			statusCode(204).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonPlayer("Collins", "Jason").toString()).
//		when().
//			put("/players");
//	}
//
//	@Test
//	public void updatePlayer_NotFound() {
//		expect().
//			statusCode(404).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonPlayer("Collins", "Tom").toString()).
//		when().
//			put("/players");
//	}
//
//	@Test
//	public void deletePlayer_Deleted() {
//		expect().
//			statusCode(204).
//		when().
//			delete("/players/Carter/Jimmy/1972-01-26");
//	}
//
//	@Test
//	public void deletePlayer_NotFound() {
//		expect().
//			statusCode(404).
//		when().
//			delete("/players/Phillips/Juan/2013-11-01");
//	}
//
//	private static JsonObject createJsonPlayer(String lastName, String firstName) {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//			.add("lastName", lastName)
//			.add("firstName", firstName)
//			.add("birthdate", "1997-07-01")
//			.add("displayName", firstName + " " + lastName)
//			.add("height", "79")
//			.add("weight", 220)
//			.add("birthplace", "Los Angeles, California, USA")
//		.build();
//		return value;
//	}
//
//	private static JsonObject updateJsonPlayer(String lastName, String firstName) {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//				.add("lastName", lastName)
//				.add("firstName", firstName)
//				.add("birthdate", "1978-12-02")
//				.add("displayName", firstName + " " + lastName)
//				.add("height", "79")
//				.add("weight", 220)
//				.add("birthplace", "Bakersfield, California, USA")
//		.build();
//		return value;
//	}
}
