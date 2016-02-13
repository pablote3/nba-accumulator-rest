package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.util.PropertyResourceLoader;

@Service
public class StandingClient {
	private static Client client;
	private PropertyResourceLoader resourceProperties;
	private static final String baseUrl = "https://erikberg.com/nba/standings/";
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Autowired
	public void setResourceProperties(PropertyResourceLoader resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	public PropertyResourceLoader getResourceProperties() {
		return resourceProperties;
	}

	static {
		String accessToken = null;
		String userAgent = null;
		client = RestClient.buildClient(accessToken, userAgent);
	}
	
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