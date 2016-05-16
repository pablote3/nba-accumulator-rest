package com.rossotti.basketball.resource.app;

import static com.jayway.restassured.RestAssured.expect;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class RankResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void rankStandings_Complete() {
		expect().
			statusCode(200).
		given().
			contentType(ContentType.JSON).
		when().
			post("/rank/2014-11-09");
	}

	@Test
	public void rankStandings_FileNotFound() {
		expect().
			statusCode(404).
		given().
			contentType(ContentType.JSON).
		when().
			post("/rank/2014-11-10");
		}
}
