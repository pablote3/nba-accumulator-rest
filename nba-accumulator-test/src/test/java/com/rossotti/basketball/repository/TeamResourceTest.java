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
import com.rossotti.basketball.dao.model.Team;

public class TeamResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByTeamKeyDate_Found() {
		expect().
			statusCode(200).
			body("fullName", equalTo("Charlotte Bobcats")).
			body("abbr", equalTo("CHA")).
		when().
			get("/teams/charlotte-bobcats/2014-06-30");
	}

	@Test
	public void findByTeamKeyDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/teams/charlotte-bobcats/2014-07-01");
	}

	@Test
	public void findByTeamKeyDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/teams/seattle-supersonics/2012-07");
	}

	@Test
	public void findTeamsByDate_Found() {
		String response = get("/teams/2012-07-01").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<Team> teams = JsonPath.read(document, "$.teams");
		String abbr = JsonPath.read(document, "$.teams[0].abbr");
		Assert.assertEquals(30, teams.size());
		Assert.assertEquals("BOS", abbr);
	}

	@Test
	public void findTeamsByDate_NotFound() {
		expect().
			statusCode(404).
		when().
			get("/teams/2012-06-30");
	}

	@Test
	public void findByDate_BadRequest() {
		expect().
			statusCode(400).
		when().
			get("/teams/2012-07");
	}

	@Test
	public void createTeam_Created() {
		expect().
			statusCode(201).
		given().
			contentType(ContentType.JSON).
			body(buildJsonTeam("providence-steamrollers").toString()).
		when().
			post("/teams");
	}

	@Test
	public void createTeam_Exists() {
		expect().
			statusCode(400).
		given().
			contentType(ContentType.JSON).
			body(buildJsonTeam("boston-celtics").toString()).
		when().
			post("/teams");
	}

	private static JsonObject buildJsonTeam(String teamKey) {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("teamKey", teamKey)
			.add("abbr", "PS")
			.add("fromDate", "2013-07-01")
			.add("toDate", "9999-12-30")
			.add("firstName", "Providence")
			.add("lastName", "Steamrollers")
			.add("conference", "East")
			.add("division", "Atlantic")
			.add("siteName", "Rhode Island Auditorium")
			.add("city", "Providence")
			.add("state", "RI")
			.add("fullName", "Providence Steamrollers")
		.build();
		return value;
	}
}
