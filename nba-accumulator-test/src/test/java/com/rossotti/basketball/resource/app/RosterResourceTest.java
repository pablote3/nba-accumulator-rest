package com.rossotti.basketball.resource.app;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class RosterResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void manageRoster_RosterPlayersExist() {
		expect().
			statusCode(200).
		given().
			contentType(ContentType.JSON).
		when().
			post("/roster/2015-04-12/toronto-raptors");
	}

	@Test
	public void manageRoster_PlayerOnAnotherTeamRoster() {
		expect().
		statusCode(200).
	given().
		contentType(ContentType.JSON).
	when().
		post("/roster/2015-04-12/utah-jazz");
	}

	@Test
	public void manageRoster_PlayerNotOnAnyTeamRoster() {
		expect().
		statusCode(200).
	given().
		contentType(ContentType.JSON).
	when().
		post("/roster/2015-04-12/san-antonio-spurs");
	}

	@Test
	public void manageRoster_PlayerDoesNotExist() {
		expect().
		statusCode(200).
	given().
		contentType(ContentType.JSON).
	when().
		post("/roster/2015-04-12/brooklyn-nets");
	}

//	@Test
//	public void scoreGame_FileNotFound() {
//		expect().
//			statusCode(404).
//		given().
//			contentType(ContentType.JSON).
//			body(postJsonGame("2015-04-16T20:00", "toronto-raptors", "new-york-knicks").toString()).
//			when().
//			post("/games");
//	}
//
//	@Test
//	public void scoreGame_GameCompleted() {
//		expect().
//			statusCode(500).
//		given().
//			contentType(ContentType.JSON).
//			body(postJsonGame("2015-04-15T20:00", "chicago-bulls", "atlanta-hawks").toString()).
//			when().
//			post("/games");
//	}
}
