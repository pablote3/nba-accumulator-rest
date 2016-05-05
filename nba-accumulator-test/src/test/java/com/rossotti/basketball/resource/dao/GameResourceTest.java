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
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;

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
			get("/games/2015-04-16/miami-heat");
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
			get("/games/2015-04-17");
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
			body(createJsonGame("2015-04-16T20:00", "atlanta-hawks", "houston-rockets").toString()).
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

	@Test
	public void createGame_TeamNotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
			body(createJsonGame("2015-04-15T20:00", "dallas-maverickos", "potland-trail-blazers").toString()).
		when().
			post("/games");
	}

	@Test
	public void updateGame_Updated() {
		expect().
			statusCode(204).
		given().
			contentType(ContentType.JSON).
			body(updateJsonGame("2015-04-16T21:00", "sacramento-kings", "utah-jazz", "Guthrie", "David", "Booker", "Trevor", "1987-11-25").toString()).
		when().
			put("/games");
	}

	@Test
	public void updateGame_GameNotFound() {
		expect().
			statusCode(404).
			given().
			contentType(ContentType.JSON).
			body(updateJsonGame("2015-04-15T21:00", "sacramento-kings", "utah-jazz", "Guthrie", "David", "Booker", "Trevor", "1987-11-25").toString()).
		when().
			put("/games");
	}

	@Test
	public void updateGame_TeamNotFound() {
		expect().
			statusCode(404).
			given().
			contentType(ContentType.JSON).
			body(updateJsonGame("2015-04-16T21:00", "sacramento-klings", "utah-jazz", "Guthrie", "David", "Booker", "Trevor", "1987-11-25").toString()).
		when().
			put("/games");
	}

	@Test
	public void updateGame_OfficialNotFound() {
		expect().
			statusCode(404).
			given().
			contentType(ContentType.JSON).
			body(updateJsonGame("2015-04-16T21:00", "sacramento-kings", "utah-jazz", "Guthrie", "Arlo", "Booker", "Trevor", "1987-11-25").toString()).
		when().
			put("/games");
	}

	@Test
	public void updateGame_RosterPlayerNotFound() {
		expect().
			statusCode(404).
			given().
			contentType(ContentType.JSON).
			body(updateJsonGame("2015-04-16T21:00", "sacramento-kings", "utah-jazz", "Guthrie", "David", "Booker", "Meatloaf", "1987-11-25").toString()).
		when().
			put("/games");
	}

	@Test
	public void deleteGame_Deleted() {
		expect().
			statusCode(204).
		when().
			delete("/games/2015-04-16/detroit-pistons");
	}

	@Test
	public void deleteGame_NotFound() {
		expect().
			statusCode(404).
		when().
			delete("/games/2015-04-17/detroit-pistons");
	}

	@Test
	public void deleteGame_BadRequest() {
		expect().
			statusCode(400).
		when().
			delete("/games/2015-04/detroit-pistons");
	}

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

	private static JsonObject updateJsonGame(String gameDateTime, String homeTeamKey, String awayTeamKey, String officialLastName, String officialFirstName, String playerLastName, String playerFirstName, String playerBirthdate) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("gameDateTime", gameDateTime)
			.add("status", "Completed")
			.add("seasonType", "Regular")
			.add("gameOfficials", factory.createArrayBuilder()
				.add(factory.createObjectBuilder()
					.add("official", factory.createObjectBuilder()
						.add("lastName", "Davis")
						.add("firstName", "Marc")
					)
				)
				.add(factory.createObjectBuilder()
					.add("official", factory.createObjectBuilder()
						.add("lastName", "Salvatore")
						.add("firstName", "Bennett")
					)
				)
				.add(factory.createObjectBuilder()
					.add("official", factory.createObjectBuilder()
						.add("lastName", officialLastName)
						.add("firstName", officialFirstName)
					)
				)
			)
			.add("boxScores", factory.createArrayBuilder()
				.add(factory.createObjectBuilder()
					.add("location", "Home")
					.add("result", "Loss")
					.add("minutes", "240")
					.add("points", "94")
					.add("assists", "22")
					.add("turnovers", "15")
					.add("steals", "9")
					.add("blocks", "4")
					.add("fieldGoalAttempts", "82")
					.add("fieldGoalMade", "37")
					.add("fieldGoalPercent", "0.451")
					.add("threePointAttempts", "27")
					.add("threePointMade", "8")
					.add("threePointPercent", "0.296")
					.add("freeThrowAttempts", "15")
					.add("freeThrowMade", "12")
					.add("freeThrowPercent", "0.8")
					.add("reboundsOffense", "7")
					.add("reboundsDefense", "33")
					.add("personalFouls", "25")
					.add("pointsPeriod1", "25")
					.add("pointsPeriod2", "18")
					.add("pointsPeriod3", "23")
					.add("pointsPeriod4", "28")
					.add("pointsPeriod5", "null")
					.add("pointsPeriod6", "null")
					.add("pointsPeriod7", "null")
					.add("pointsPeriod8", "null")
					.add("daysOff", "0")
					.add("boxScorePlayers", factory.createArrayBuilder()
						.add(factory.createObjectBuilder()
							.add("position", "SG")
							.add("starter", "true")
							.add("minutes", "14")
							.add("points", "3")
							.add("assists", "4")
							.add("turnovers", "1")
							.add("steals", "0")
							.add("blocks", "0")
							.add("fieldGoalAttempts", "3")
							.add("fieldGoalMade", "1")
							.add("fieldGoalPercent", "0.333")
							.add("threePointAttempts", "1")
							.add("threePointMade", "0")
							.add("threePointPercent", "0.0")
							.add("freeThrowAttempts", "2")
							.add("freeThrowMade", "1")
							.add("freeThrowPercent", "0.5")
							.add("reboundsOffense", "1")
							.add("reboundsDefense", "3")
							.add("personalFouls", "2")
							.add("rosterPlayer", factory.createObjectBuilder()
								.add("player", factory.createObjectBuilder()
									.add("lastName", "Cousins")
									.add("firstName", "DeMarcus")
									.add("birthdate", "1990-08-13")
								)
							)
						)
						.add(factory.createObjectBuilder()
							.add("position", "F")
							.add("starter", "false")
							.add("minutes", "4")
							.add("points", "2")
							.add("assists", "2")
							.add("turnovers", "2")
							.add("steals", "1")
							.add("blocks", "1")
							.add("fieldGoalAttempts", "8")
							.add("fieldGoalMade", "2")
							.add("fieldGoalPercent", "0.25")
							.add("threePointAttempts", "1")
							.add("threePointMade", "0")
							.add("threePointPercent", "1.0")
							.add("freeThrowAttempts", "2")
							.add("freeThrowMade", "1")
							.add("freeThrowPercent", "0.5")
							.add("reboundsOffense", "0")
							.add("reboundsDefense", "5")
							.add("personalFouls", "0")
							.add("rosterPlayer", factory.createObjectBuilder()
								.add("player", factory.createObjectBuilder()
									.add("lastName", "Thompson")
									.add("firstName", "Jason")
									.add("birthdate", "1986-07-21")
								)
							)
						)
					)
					.add("team", factory.createObjectBuilder()
						.add("teamKey", homeTeamKey)
					)
				)
				.add(factory.createObjectBuilder()
					.add("location", "Away")
					.add("result", "Win")
					.add("minutes", "240")
					.add("points", "106")
					.add("assists", "23")
					.add("turnovers", "15")
					.add("steals", "5")
					.add("blocks", "3")
					.add("fieldGoalAttempts", "96")
					.add("fieldGoalMade", "37")
					.add("fieldGoalPercent", "0.385")
					.add("threePointAttempts", "29")
					.add("threePointMade", "12")
					.add("threePointPercent", "0.414")
					.add("freeThrowAttempts", "26")
					.add("freeThrowMade", "20")
					.add("freeThrowPercent", "0.769")
					.add("reboundsOffense", "23")
					.add("reboundsDefense", "36")
					.add("personalFouls", "15")
					.add("pointsPeriod1", "25")
					.add("pointsPeriod2", "23")
					.add("pointsPeriod3", "34")
					.add("pointsPeriod4", "24")
					.add("pointsPeriod5", "null")
					.add("pointsPeriod6", "null")
					.add("pointsPeriod7", "null")
					.add("pointsPeriod8", "null")
					.add("daysOff", "0")
					.add("boxScorePlayers", factory.createArrayBuilder()
						.add(factory.createObjectBuilder()
							.add("position", "F")
							.add("starter", "false")
							.add("minutes", "4")
							.add("points", "0")
							.add("assists", "5")
							.add("turnovers", "3")
							.add("steals", "1")
							.add("blocks", "3")
							.add("fieldGoalAttempts", "5")
							.add("fieldGoalMade", "2")
							.add("fieldGoalPercent", "0.4")
							.add("threePointAttempts", "1")
							.add("threePointMade", "1")
							.add("threePointPercent", "1.0")
							.add("freeThrowAttempts", "0")
							.add("freeThrowMade", "0")
							.add("freeThrowPercent", "0.0")
							.add("reboundsOffense", "1")
							.add("reboundsDefense", "5")
							.add("personalFouls", "3")
							.add("rosterPlayer", factory.createObjectBuilder()
								.add("player", factory.createObjectBuilder()
									.add("lastName", playerLastName)
									.add("firstName", playerFirstName)
									.add("birthdate", playerBirthdate)
								)
							)
						)
					)
					.add("team", factory.createObjectBuilder()
						.add("teamKey", awayTeamKey)
					)
				)
			)
		.build();
		return value;
	}
}
