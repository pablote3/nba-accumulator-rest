package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;

@Service
public class RestClient {
	@Autowired
	private ClientBean clientBean;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public GameDTO retrieveBoxScore(String event) {
		GameDTO game = null;
		String baseUrl = "https://erikberg.com/nba/boxscore/";
		String boxScoreUrl = baseUrl + event + ".json";

		Response response = clientBean.getClient().target(boxScoreUrl).request().get();

		if (response.getStatus() != 200) {
			game = new GameDTO();
			response.readEntity(String.class);
		} else {
			try {
				game = mapper.readValue(response.readEntity(String.class), GameDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		game.httpStatus = response.getStatus();
		response.close();
		return game;
	}

	public RosterDTO retrieveRoster(String event) {
		RosterDTO roster = null;
		String baseUrl = "https://erikberg.com/nba/roster/";
		String rosterUrl = baseUrl + event + ".json";
		Response response = clientBean.getClient().target(rosterUrl).request().get();

		if (response.getStatus() != 200) {
			roster = new RosterDTO();
			response.readEntity(String.class);
		} else {
			try {
				roster = mapper.readValue(response.readEntity(String.class), RosterDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		roster.httpStatus = response.getStatus();
		response.close();
		return roster;
	}

	public StandingsDTO retrieveStandings(String event) {
		StandingsDTO standings = null;
		String baseUrl = "https://erikberg.com/nba/standings/";
		String standingsUrl = baseUrl + event + ".json";
		Response response = clientBean.getClient().target(standingsUrl).request().get();

		if (response.getStatus() != 200) {
			standings = new StandingsDTO();
			response.readEntity(String.class);
		} else {
			try {
				standings = mapper.readValue(response.readEntity(String.class), StandingsDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		standings.httpStatus = response.getStatus();
		response.close();
		return standings;
	}
}