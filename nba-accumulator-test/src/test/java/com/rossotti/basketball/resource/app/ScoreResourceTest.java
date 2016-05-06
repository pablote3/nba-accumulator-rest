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
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest/score";
	}

	@Test
	public void scoreGame_Completed() {
		expect().
			statusCode(200).
		given().
			contentType(ContentType.JSON).
			body(postJsonGame("2015-04-16T20:00", "atlanta-hawks", "houston-rockets").toString()).
			when().
			post("/games");
	}

//	@Test
//	public void findByTeamAsOfDate_NotFound() {
//		expect().
//			statusCode(404).
//		when().
//			get("/games/2015-04-16/portland-trail-blazers");
//	}
//
//	@Test
//	public void findByTeamAsOfDate_BadRequest() {
//		expect().
//			statusCode(400).
//		when().
//			get("/games/2015-04/atlanta-hawks");
//	}

	private static JsonObject postJsonGame(String gameDateTime, String homeTeamKey, String awayTeamKey) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("gameDateTime", gameDateTime)
			.add("status", "Scheduled")
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
