package com.rossotti.basketball.client;

import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.util.ResourceLoader;

@Service
public class RosterClient {
	private static Client client;
	private static Properties properties = ResourceLoader.getProperties();
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private static final String baseUrl = "https://erikberg.com/nba/roster/";

	static {
		String accessToken = properties.getProperty("xmlstats.accessToken");
		String userAgent = properties.getProperty("xmlstats.userAgent");
		client = RestClient.buildClient(accessToken, userAgent);
	}

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