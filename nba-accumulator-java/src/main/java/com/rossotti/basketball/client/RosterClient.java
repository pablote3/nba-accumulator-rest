package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.RosterDTO;

@Service
public class RosterClient {
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public static RosterDTO retrieveRoster(String event) {
		RosterDTO roster = null;
		String baseUrl = "https://erikberg.com/nba/roster/";
		String rosterUrl = baseUrl + event + ".json";
		Response response = RestClient.getInstance().getClient().target(rosterUrl).request().get();

		if (response.getStatus() != 200) {
			roster = new RosterDTO();
		} else {
			try {
				roster = mapper.readValue(response.readEntity(String.class), RosterDTO.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		roster.httpStatus = response.getStatus();
		return roster;
	}
}