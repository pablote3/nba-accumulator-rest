package com.rossotti.basketball.resource.app;

import static com.jayway.restassured.RestAssured.expect;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class ScoreResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void scoreGame_GameScheduled() {
		expect().
			statusCode(200).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "Scheduled", "atlanta-hawks", "houston-rockets").toString()).
			when().
			post("/score");
	}

	@Test
	public void scoreGame_GameNotFound() {
		expect().
			statusCode(500).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "Scheduled", "portland-trail-blazers", "denver-nuggets").toString()).
			when().
			post("/score");
	}

	@Test
	public void scoreGame_OfficialNotFound() {
		expect().
			statusCode(400).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "Scheduled", "phoenix-suns", "miami-heat").toString()).
			when().
			post("/score");
	}

	@Test
	public void scoreGame_RosterPlayerNotFound() {
		expect().
			statusCode(400).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "Scheduled", "indiana-pacers", "orlando-magic").toString()).
			when().
			post("/score");
	}

	@Test
	public void scoreGame_FileNotFound() {
		expect().
			statusCode(400).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "Scheduled", "toronto-raptors", "new-york-knicks").toString()).
			when().
			post("/score");
	}

	@Test
	public void scoreGame_GameCompleted() {
		expect().
			statusCode(500).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-15T20:00", "Completed", "chicago-bulls", "atlanta-hawks").toString()).
			when().
			post("/score");
	}

	private static JsonObject postJsonGame(String gameDateTime, String status, String homeTeamKey, String awayTeamKey) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("gameDateTime", gameDateTime)
			.add("status", status)
			.add("seasonType", "Regular")
			.add("boxScores", factory.createArrayBuilder()
				.add(factory.createObjectBuilder()
					.add("location", "Home")
					.add("team", factory.createObjectBuilder()
						.add("teamKey", homeTeamKey)))
				.add(factory.createObjectBuilder()
					.add("location", "Away")
					.add("team", factory.createObjectBuilder()
						.add("teamKey", awayTeamKey))))
		.build();
		return value;
	}
}
