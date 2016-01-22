package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.RosterDTO;

@Repository
public class RosterClient {
	private static final String baseUrl = "https://erikberg.com/nba/roster/";
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	Client client = ClientBuilder.newBuilder().build().register(XmlStatsFilter.class);

	public RosterDTO retrieveRoster(String event) {
		String rosterUrl = baseUrl + event + ".json";
		Response response = client.target(rosterUrl).request().get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		RosterDTO roster = null;
		try {
			roster = mapper.readValue(response.readEntity(String.class), RosterDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roster;
	}
}
