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
import com.jayway.restassured.http.ContentType;
import com.rossotti.basketball.dao.model.RosterPlayer;

public class RosterPlayerResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByPlayerNameBirthdateAsOfDate_Found() {
		expect().
			statusCode(200).
			body("toDate", equalTo("2013-06-30")).
			body("position", equalTo("PG")).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10-07/2012-10-30");
	}

	@Test
	public void findByPlayerNameBirthdateAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/player/Price/K.J./1986-10-07/2012-10-30");
	}

	@Test
	public void findByPlayerNameBirthdateAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10-07/2012-10");
	}

	public void findByPlayerNameTeamAsOfDate_Found() {
		expect().
			statusCode(200).
			body("toDate", equalTo("2013-06-30")).
			body("position", equalTo("PG")).
		when().
			get("/rosterPlayers/team/Price/A.J./cleveland-cavaliers/2014-10-30");
	}

	@Test
	public void findByPlayerNameTeamAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/team/Price/A.J./miami-heat/2014-10-30");
	}

	@Test
	public void findByPlayerNameTeamAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/team/Price/A.J./cleveland-cavaliers/2012-10");
	}

	@Test
	public void findByPlayer_Found() {
		String response = get("/rosterPlayers/player/Price/A.J./1986-10-07").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<RosterPlayer> rosterPlayers = JsonPath.read(document, "$.rosterPlayers");
		String fromDate = JsonPath.read(document, "$.rosterPlayers[0].fromDate");
		Assert.assertTrue("Roster players size " + rosterPlayers.size() + " should be at least 2", rosterPlayers.size() >= 2);
		Assert.assertEquals("2012-10-30", fromDate);
	}

	@Test
	public void findByPlayer_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/player/Jones/Prince/1986-10-07");
	}

	@Test
	public void findByPlayer_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/player/Price/A.J./1986-10");
	}

	@Test
	public void findByTeam_Found() {
		String response = get("/rosterPlayers/team/atlanta-hawks/2014-10-29").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<RosterPlayer> rosterPlayers = JsonPath.read(document, "$.rosterPlayers");
		String fromDate = JsonPath.read(document, "$.rosterPlayers[0].fromDate");
		Assert.assertTrue("Roster players size " + rosterPlayers.size() + " should be at least 15", rosterPlayers.size() >= 15);
		Assert.assertEquals("2014-10-29", fromDate);
	}

	@Test
	public void findByTeam_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/rosterPlayers/team/atlanta-falcons/2014-10-30");
	}

	@Test
	public void findByTeam_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/rosterPlayers/team/atlanta-hawks/1986-10");
	}

	@Test
	public void createRosterPlayer_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			body(createJsonRosterPlayer("Brewer", "Corey", "houston-rockets", "2014-12-25", "2015-02-20").toString()).
		when().
			post("/rosterPlayers");
	}

	@Test
	public void createRosterPlayer_RosterPlayerExists() {
		expect().
			statusCode(403).
		given().
			contentType(ContentType.JSON).
			body(createJsonRosterPlayer("Brewer", "Corey", "houston-rockets", "2014-12-21", "2015-02-20").toString()).
		when().
			post("/rosterPlayers");
	}

	@Test
	public void createRosterPlayer_PlayerNotExists() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(createJsonRosterPlayer("Brewery", "Corey", "houston-rockets", "2014-12-21", "2015-02-20").toString()).
		when().
			post("/rosterPlayers");
	}

	@Test
	public void createRosterPlayer_TeamNotExists() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(createJsonRosterPlayer("Brewer", "Corey", "houston-rocketers", "2014-12-21", "2015-02-20").toString()).
		when().
			post("/rosterPlayers");
	}

	@Test
	public void updatePlayer_Updated() {
		expect().
			statusCode(204).
		given().
			contentType(ContentType.JSON).
			body(updateJsonRosterPlayer("Ennis", "Tyler", "2014-10-29", "2015-02-28").toString()).
		when().
			put("/rosterPlayers");
	}

	@Test
	public void updatePlayer_NotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(updateJsonRosterPlayer("Onis", "Tyler", "2014-10-29", "2015-02-28").toString()).
		when().
			put("/rosterPlayers");
	}

	@Test
	public void deletePlayer_Deleted() {
		expect().
			statusCode(204).
		when().
			delete("/rosterPlayers/Thomas/Isaiah/1989-02-07/2015-03-17");
	}

	@Test
	public void deletePlayer_NotFound() {
		expect().
			statusCode(404).
		when().
			delete("/rosterPlayers/Thomas/Isaiahan/1989-02-07/2015-03-17");
	}

	@Test
	public void deletePlayer_BadRequest() {
		expect().
			statusCode(400).
		when().
			delete("/rosterPlayers/Thomas/Isaiah/1989-02-07/2015-03");
	}

	private static JsonObject createJsonRosterPlayer(String lastName, String firstName, String teamKey, String fromDate, String toDate) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("fromDate", fromDate)
			.add("toDate", toDate)
			.add("position", "C")
			.add("number", "21")
			.add("team", factory.createObjectBuilder()
				.add("teamKey", teamKey))
			.add("player", factory.createObjectBuilder()
				.add("lastName", lastName)
				.add("firstName", firstName)
				.add("birthdate", "1986-03-05"))
		.build();
		return value;
	}

	private static JsonObject updateJsonRosterPlayer(String lastName, String firstName, String fromDate, String toDate) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("fromDate", fromDate)
				.add("toDate", toDate)
				.add("position", "PG")
				.add("number", "99")
				.add("player", factory.createObjectBuilder()
						.add("lastName", lastName)
						.add("firstName", firstName)
						.add("birthdate", "1994-08-24"))
		.build();
		return value;
	}
}
