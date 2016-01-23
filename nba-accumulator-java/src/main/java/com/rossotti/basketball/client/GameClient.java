package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;

@Repository
public class GameClient {
	private static final String baseUrl = "https://erikberg.com/nba/boxscore/";
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public GameDTO retrieveBoxScore(String event) {
		String boxScoreUrl = baseUrl + event + ".json";
		Response response = ClientBean.getClient().target(boxScoreUrl).request().get();

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
