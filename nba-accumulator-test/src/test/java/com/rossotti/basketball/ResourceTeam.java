package com.rossotti.basketball;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

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
	public void foundTeam_NoAsOfDate() {
		expect().
			statusCode(200).
			defaultParser(Parser.JSON).
			body("fullName", equalTo("Seattle Supersonics")).
			body("abbr", equalTo("SEA")).
		when().
			get("/teams/seattle-supersonics");
	}
	
	@Test
	public void foundTeam_WithAsOfDate() {
		expect().
			statusCode(200).
			defaultParser(Parser.JSON).
			body("fullName", equalTo("Seattle Supersonics")).
			body("abbr", equalTo("SEA")).
		when().
			get("/teams/seattle-supersonics/2012-07-01");
	}
	
	@Test
	public void badRequest_IllegalAsOfDate() {
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
//		expect().
//			statusCode(201).
//		given().
//			contentType(ContentType.JSON).
//			body(buildJsonTeam().toString()).
//		when().
//			post("/teams");
//	}

//	private static JsonObject buildJsonTeam() {
//		JsonBuilderFactory factory = Json.createBuilderFactory(null);
//		JsonObject value = factory.createObjectBuilder()
//			.add("team_id", "providence-steamrollers")
//			.add("abbreviation", "PS")
//			.add("from_date", "1946-07-01")
//			.add("to_date", "1949-06-30")
//			.add("first_name", "Providence")
//			.add("last_name", "Steamrollers")
//			.add("conference", "East")
//			.add("division", "Atlantic")
//			.add("site_name", "Rhode Island Auditorium")
//			.add("city", "Providence")
//			.add("state", "Rhode Island")
//			.add("full_name", "Providence Steamrollers")
//		.build();
//		return value;
//	}
}
