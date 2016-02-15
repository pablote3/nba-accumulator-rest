package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;

@Service
public class GameClient {
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public static GameDTO retrieveBoxScore(String event) {
		GameDTO game = null;
		String baseUrl = "https://erikberg.com/nba/boxscore/";
		String boxScoreUrl = baseUrl + event + ".json";
		Response response = RestClient.getInstance().getClient().target(boxScoreUrl).request().get();

		if (response.getStatus() != 200) {
			game = new GameDTO();
		} else {
			try {
				game = mapper.readValue(response.readEntity(String.class), GameDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		game.httpStatus = response.getStatus();
		return game;
	}
}