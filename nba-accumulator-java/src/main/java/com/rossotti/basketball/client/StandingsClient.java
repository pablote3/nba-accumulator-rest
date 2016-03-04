package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.rest.RestClient;

@Service
public class StandingsClient {
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public static StandingsDTO retrieveStandings(String event) {
		StandingsDTO standings = null;
		String baseUrl = "https://erikberg.com/nba/standings/";
		String standingsUrl = baseUrl + event + ".json";
		Response response = RestClient.getInstance().getClient().target(standingsUrl).request().get();

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