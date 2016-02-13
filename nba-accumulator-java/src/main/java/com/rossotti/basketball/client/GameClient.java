package com.rossotti.basketball.client;

import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.util.ResourceLoader;

@Service
public class GameClient {
	private static Client client;
	private static Properties properties = ResourceLoader.getProperties();
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private static final String baseUrl = "https://erikberg.com/nba/boxscore/";

	static {
		String accessToken = properties.getProperty("xmlstats.accessToken");
		String userAgent = properties.getProperty("xmlstats.userAgent");
		client = RestClient.buildClient(accessToken, userAgent);
	}

	public GameDTO retrieveBoxScore(String event) {
		String boxScoreUrl = baseUrl + event + ".json";
		Response response = client.target(boxScoreUrl).request().get();

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		GameDTO game = null;
		try {
			game = mapper.readValue(response.readEntity(String.class), GameDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}
}