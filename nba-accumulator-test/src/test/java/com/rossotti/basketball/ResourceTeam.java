package com.rossotti.basketball;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;

public class ResourceTeam {
	
	@Before
	 public void setUp(){
		RestAssured.baseURI = "http://localhost:8080/nba-accumulator-web/rest";
	 }

	@Test
	public void foundTeam_NoDate() {
		expect().
			statusCode(200).
			defaultParser(Parser.JSON).
			body("fullName", equalTo("Seattle Supersonics")).
			body("abbr", equalTo("SEA")).
		when().
			get("/teams/seattle-supersonics");
	}
	
	@Test
	public void foundTeam_WithDate() {
		expect().
			statusCode(200).
			defaultParser(Parser.JSON).
			body("fullName", equalTo("Seattle Supersonics")).
			body("abbr", equalTo("SEA")).
		when().
			get("/teams/seattle-supersonics/2012-07-01");
	}
	
	@Test
	public void badRequest_IllegalDate() {
		expect().
			statusCode(400).
		when().
			get("/teams/seattle-supersonics/2012-07");
	}
	
	@Test
	public void noSuchEntity_NoDate() {
		expect().
			statusCode(404).
		when().
			get("/teams/seattle-supersonic");
	}
	
	@Test
	public void noSuchEntity_WithDate() {
		expect().
			statusCode(404).
		when().
			get("/teams/seattle-supersonics/2005-10-10");
	}
	
//	@Test
//	public void createTeam() {
//		JsonObject json = buildJsonTeam();
//		
//		given().
//			contentType(ContentType.JSON).
//			body(json.toString()).
//		expect().
//			statusCode(201).
//		when().
//			put("/users");
//	}
	
	private static JsonObject buildJsonTeam() {
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
			.add("team_id", "Utah Jazz")
			.add("abbreviation", "UTA")
			.add("from_date", "2012-07-01")
			.add("to_date", "9999-12-31")
			.add("first_name", "Utah")
			.add("last_name", "Jazz")
			.add("conference", "East")
		.build();
		return value;
	}
}
