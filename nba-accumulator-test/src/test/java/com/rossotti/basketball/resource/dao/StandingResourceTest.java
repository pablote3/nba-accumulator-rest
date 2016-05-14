package com.rossotti.basketball.resource.dao;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;

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
import com.rossotti.basketball.dao.model.Standing;

public class StandingResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByTeamDate_Found() {
		String response = get("/standings/chicago-bulls/2015-04-14").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		Assert.assertEquals("Chicago Bulls", JsonPath.read(document, "$.team.fullName"));
		Assert.assertEquals(8174, JsonPath.read(document, "$.pointsFor"));
	}

	@Test
	public void findByTeamDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/standings/chicago-bulls/2011-11-06");
	}

	@Test
	public void findByTeamDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/standings/chicago-bulls/2014-11");
	}

	@Test
	public void findByDate_Found() {
		String response = get("/standings/2015-04-14").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<Standing> standings = JsonPath.read(document, "$.standings");
		Assert.assertEquals(30, standings.size());
		Assert.assertEquals("Atlanta Hawks", JsonPath.read(document, "$.standings[0].team.fullName"));
	}

	@Test
	public void findByDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/standings/2010-11-06");
	}

	@Test
	public void findByDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/standings/2014-11");
	}

	@Test
	public void createStanding_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			body(buildJsonStanding("utah-jazz", "2015-04-16").toString()).
		when().
			post("/standings");
	}

	@Test
	public void createStanding_Found() {
		expect().
			statusCode(403).
		given().
			contentType(ContentType.JSON).
			body(buildJsonStanding("boston-celtics", "2015-04-15").toString()).
		when().
			post("/standings");
	}

	@Test
	public void createStanding_TeamNotExists() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(buildJsonStanding("boston-celticks", "2015-04-16").toString()).
		when().
			post("/standings");
	}

	@Test
	public void updateStanding_Updated() {
		expect().
			statusCode(204).
		given().
			contentType(ContentType.JSON).
			body(buildJsonStanding("boston-celtics", "2015-04-15").toString()).
		when().
			put("/standings");
	}

	@Test
	public void updateStanding_NotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(buildJsonStanding("boston-celticks", "2015-04-16").toString()).
		when().
			put("/standings");
	}

	@Test
	public void deleteStanding_Deleted() {
		expect().
			statusCode(204).
		when().
			delete("/standings/boston-celtics/2015-04-16");
	}

	@Test
	public void deleteStanding_NotFound() {
		expect().
			statusCode(404).
		when().
			delete("/standings/chicago-bulls/2015-04-16");
	}

	@Test
	public void deleteStanding_BadRequest() {
		expect().
			statusCode(400).
		when().
			delete("/standings/chicago-bulls/2015-04");
	}

	private static JsonObject buildJsonStanding(String teamKey, String standingDate) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("standingDate", standingDate)
			.add("rank", "5")
			.add("ordinalRank", "5th")
			.add("gamesWon", "10")
			.add("gamesLost", "20")
			.add("streak", "W2")
			.add("streakType", "win")
			.add("streakTotal", "2")
			.add("gamesBack", "4.5")
			.add("pointsFor", "1792")
			.add("pointsAgainst", "1474")
			.add("homeWins", "5")
			.add("homeLosses", "10")
			.add("awayWins", "5")
			.add("awayLosses", "10")
			.add("conferenceWins", "4")
			.add("conferenceLosses", "6")
			.add("lastFive", "2")
			.add("lastTen", "4")
			.add("gamesPlayed", "30")
			.add("pointsScoredPerGame", "102")
			.add("pointsAllowedPerGame", "108")
			.add("winPercentage", "0.333")
			.add("pointDifferential", "60")
			.add("pointDifferentialPerGame", "6")
			.add("opptGamesWon", "42")
			.add("opptGamesPlayed", "65")
			.add("opptOpptGamesWon", "275")
			.add("opptOpptGamesPlayed", "369")
			.add("team", factory.createObjectBuilder()
				.add("teamKey", teamKey))
		.build();
		return value;
	}
}
