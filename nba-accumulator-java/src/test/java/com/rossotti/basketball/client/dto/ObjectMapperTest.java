package com.rossotti.basketball.client.dto;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;

public class ObjectMapperTest {
	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void deserialize_JsonToPojo_Roster() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/rosterClient.json");
			RosterDTO roster = mapper.readValue(baseJson, RosterDTO.class);
			Assert.assertEquals("toronto-raptors", roster.team.getTeam_id());
			Assert.assertEquals("São Gonçalo, Brazil", roster.players[8].getBirthplace());
			Assert.assertEquals("Valančiūnas", roster.players[13].getLast_name());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Game() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json");
			GameDTO game = mapper.readValue(baseJson, GameDTO.class);
			Assert.assertEquals("detroit-pistons", game.away_team.getTeam_id());
			Assert.assertEquals(17, game.home_period_scores[1]);
			Assert.assertEquals("Anthony Tolliver", game.away_stats[6].getDisplay_name());
			Assert.assertEquals("Zarba", game.officials[0].getLast_name());
			Assert.assertEquals("completed", game.event_information.getStatus());
			Assert.assertTrue(game.away_totals.getThree_point_field_goals_attempted().equals((short)24));
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Standings() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/standingsClient.json");
			StandingsDTO standings = mapper.readValue(baseJson, StandingsDTO.class);
			Assert.assertEquals("toronto-raptors", standings.standing[1].getTeam_id());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}