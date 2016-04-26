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
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.Team;

public class StandingResourceTest {
	@Before
	public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	}

	@Test
	public void findByTeamDate_Found() {
		String response = get("/standings/chicago-bulls/2014-11-06").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		Assert.assertEquals("Chicago Bulls", JsonPath.read(document, "$.team.fullName"));
		Assert.assertEquals(511, JsonPath.read(document, "$.pointsFor"));
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
		String response = get("/standings/2014-11-06").asString();
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);
		List<Standing> standings = JsonPath.read(document, "$.standings");
		Assert.assertTrue("Standings size " + standings.size() + " should be at least 30", standings.size() >= 30);
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

//	@Test
//	public void createTeam_Created() {
//		expect().
//			statusCode(201).
//		given().
//			contentType(ContentType.JSON).
//			body(createJsonTeam("seattle-supersonics").toString()).
//		when().
//			post("/teams");
//	}
//
//	@Test
//	public void createTeam_Found() {
//		expect().
//			statusCode(403).
//		given().
//			contentType(ContentType.JSON).
//			body(createJsonTeam("boston-celtics").toString()).
//		when().
//			post("/teams");
//	}
//
//	@Test
//	public void updateTeam_Updated() {
//		expect().
//			statusCode(204).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonTeam("chicago-bulls").toString()).
//		when().
//			put("/teams");
//	}
//
//	@Test
//	public void updateTeam_NotFound() {
//		expect().
//			statusCode(404).
//		given().
//			contentType(ContentType.JSON).
//			body(updateJsonTeam("chicago-cows").toString()).
//		when().
//			put("/teams");
//	}
//
//	@Test
//	public void deleteTeam_Deleted() {
//		expect().
//			statusCode(204).
//		when().
//			delete("/teams/providence-steamrollers/2013-07-01");
//	}
//
//	@Test
//	public void deleteTeam_NotFound() {
//		expect().
//			statusCode(404).
//		when().
//			delete("/teams/providence-steamers/2013-07-01");
//	}
//
//	@Test
//	public void deleteTeam_BadRequest() {
//		expect().
//			statusCode(400).
//		when().
//			delete("/teams/providence-steamrollers/2012-07");
//	}
//
//	private static JsonObject createJsonTeam(String teamKey) {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//			.add("teamKey", teamKey)
//			.add("abbr", "PS")
//			.add("fromDate", "2013-07-01")
//			.add("toDate", "9999-12-30")
//			.add("firstName", "Seattle")
//			.add("lastName", "Supersonics")
//			.add("conference", "West")
//			.add("division", "Pacific")
//			.add("siteName", "Key West Arena")
//			.add("city", "Seattle")
//			.add("state", "WA")
//			.add("fullName", "Seattle Supersonics")
//		.build();
//		return value;
//	}
//	
//	private static JsonObject updateJsonTeam(String teamKey) {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//			.add("teamKey", teamKey)
//			.add("abbr", "CHI")
//			.add("fromDate", "2012-07-01")
//			.add("toDate", "2018-06-30")
//			.add("firstName", "Chicago")
//			.add("lastName", "Booleans")
//			.add("conference", "East")
//			.add("division", "Central")
//			.add("siteName", "United Center")
//			.add("city", "Chicago")
//			.add("state", "IL")
//			.add("fullName", "Chicago Bulls")
//		.build();
//		return value;
//	}
}
