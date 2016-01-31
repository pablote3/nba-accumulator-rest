package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.StandingsDTO;

@Repository
public class StandingClient {
	private static final String baseUrl = "https://erikberg.com/nba/standings/";
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private static Client client = ClientBuilder.newBuilder().build().register(XmlStatsFilter.class);

	public StandingsDTO retrieveStandings(String event) {
		String standingsUrl = baseUrl + event + ".json";
		Response response = client.target(standingsUrl).request().get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		StandingsDTO standings = null;
		try {
			standings = mapper.readValue(response.readEntity(String.class), StandingsDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return standings;
	}
}