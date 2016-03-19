package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;

@Service
public class RestClientBean {
	@Autowired
	private ClientBean clientBean;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	private StatsDTO retrieveStats(String url, StatsDTO statsDTO) {
		Response response = clientBean.getClient().target(url).request().get();
		
		if (response.getStatus() != 200) {
			response.readEntity(String.class);
		} else {
			try {
				statsDTO = mapper.readValue(response.readEntity(String.class), statsDTO.getClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		statsDTO.httpStatus = response.getStatus();
		response.close();
		return statsDTO;
	}

	public GameDTO retrieveBoxScore(String event) {
		GameDTO gameDTO = new GameDTO();;
		String boxScoreUrl = "https://erikberg.com/nba/boxscore/" + event + ".json";
		return (GameDTO)retrieveStats(boxScoreUrl, gameDTO);
	}

	public RosterDTO retrieveRoster(String event) {
		RosterDTO rosterDTO = new RosterDTO();
		String rosterUrl = "https://erikberg.com/nba/roster/" + event + ".json";
		return (RosterDTO)retrieveStats(rosterUrl, rosterDTO);
	}

	public StandingsDTO retrieveStandings(String event) {
		StandingsDTO standingsDTO = new StandingsDTO();
		String standingsUrl = "https://erikberg.com/nba/standings/" + event + ".json";
		return (StandingsDTO)retrieveStats(standingsUrl, standingsDTO);
	}

	public ClientBean getClientBean() {
		return clientBean;
	}
	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}
}