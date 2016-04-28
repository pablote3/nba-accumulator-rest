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
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.RosterPlayer;

public class GameResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByTeamAsOfDate_Found() {
		String response = get("/games/2015-04-15/atlanta-hawks").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		Assert.assertEquals("2015-04-15T20:00", JsonPath.read(document, "$.gameDateTime"));

		List<GameOfficial> officials = JsonPath.read(document, "$.gameOfficials");
		Assert.assertEquals(3, officials.size());
		Assert.assertEquals("Kirkland", JsonPath.read(document, "$.gameOfficials[0].official.lastName"));

		List<BoxScore> boxScores = JsonPath.read(document, "$.boxScores");
		Assert.assertEquals(2, boxScores.size());
		Assert.assertEquals("15", JsonPath.read(document, "$.boxScores[0].turnovers"));

		List<BoxScorePlayer> boxScorePlayers = JsonPath.read(document, "$.boxScores[0].boxScorePlayers");
		Assert.assertEquals(12, boxScorePlayers.size());
		Assert.assertEquals("3", JsonPath.read(document, "$.boxScores[0].boxScorePlayers[1].personalFouls"));

		Assert.assertEquals("Atlanta Hawks", JsonPath.read(document, "$.boxScores[0].team.fullName"));
	}

	@Test
	public void findByTeamAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/games/2015-04-16/chicago-bulls");
	}

	@Test
	public void findByTeamAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/games/2015-04/atlanta-hawks");
	}

	@Test
	public void findByAsOfDate_Found() {
		String response = get("/games/2015-04-15").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
	
		List<Game> games = JsonPath.read(document, "$.games");
		Assert.assertEquals(14, games.size());
		Assert.assertEquals("2015-04-15T20:00", JsonPath.read(document, "$.games[0].gameDateTime"));

		List<GameOfficial> officials = JsonPath.read(document, "$.games[0].gameOfficials");
		Assert.assertEquals(3, officials.size());
		Assert.assertEquals("Kirkland", JsonPath.read(document, "$.games[0].gameOfficials[0].official.lastName"));

		List<BoxScore> boxScores = JsonPath.read(document, "$.games[0].boxScores");
		Assert.assertEquals(2, boxScores.size());
		Assert.assertEquals("15", JsonPath.read(document, "$.games[0].boxScores[0].turnovers"));

		List<BoxScorePlayer> boxScorePlayers = JsonPath.read(document, "$.games[0].boxScores[0].boxScorePlayers");
		Assert.assertEquals(12, boxScorePlayers.size());
		Assert.assertEquals("3", JsonPath.read(document, "$.games[0].boxScores[0].boxScorePlayers[1].personalFouls"));

		Assert.assertEquals("Atlanta Hawks", JsonPath.read(document, "$.games[0].boxScores[0].team.fullName"));
	}

	@Test
	public void findByAsOfDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/games/2015-04-16");
	}

	@Test
	public void findByAsOfDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/games/2015-04");
	}

	@Test
	public void createGame_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			body(createJsonGame("2015-04-16T20:00", "atlanta-hawks", "detroit-pistons").toString()).
		when().
			post("/games");
	}

	@Test
	public void createGame_Found() {
		expect().
			statusCode(403).
		given().
			contentType(ContentType.JSON).
			body(createJsonGame("2015-04-15T20:00", "dallas-mavericks", "portland-trail-blazers").toString()).
		when().
			post("/games");
	}

//	@Test
//	public void updatePlayer_Updated() {
//		expect().
//			statusCode(204).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonRosterPlayer("Ennis", "Tyler", "2014-10-29", "2015-02-28").toString()).
//		when().
//			put("/rosterPlayers");
//	}
//
//	@Test
//	public void updatePlayer_NotFound() {
//		expect().
//			statusCode(404).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonRosterPlayer("Onis", "Tyler", "2014-10-29", "2015-02-28").toString()).
//		when().
//			put("/rosterPlayers");
//	}
//
//	@Test
//	public void deletePlayer_Deleted() {
//		expect().
//			statusCode(204).
//		when().
//			delete("/rosterPlayers/Thomas/Isaiah/1989-02-07/2015-03-17");
//	}
//
//	@Test
//	public void deletePlayer_NotFound() {
//		expect().
//			statusCode(404).
//		when().
//			delete("/rosterPlayers/Thomas/Isaiahan/1989-02-07/2015-03-17");
//	}
//
//	@Test
//	public void deletePlayer_BadRequest() {
//		expect().
//			statusCode(400).
//		when().
//			delete("/rosterPlayers/Thomas/Isaiah/1989-02-07/2015-03");
//	}

	private static JsonObject createJsonGame(String gameDateTime, String homeTeamKey, String awayTeamKey) {
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

//	private static JsonObject updateJsonRosterPlayer(String lastName, String firstName, String fromDate, String toDate) {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//				.add("fromDate", fromDate)
//				.add("toDate", toDate)
//				.add("position", "PG")
//				.add("number", "99")
//				.add("player", factory.createObjectBuilder()
//						.add("lastName", lastName)
//						.add("firstName", firstName)
//						.add("birthdate", "1994-08-24"))
//		.build();
//		return value;
//	}
}
