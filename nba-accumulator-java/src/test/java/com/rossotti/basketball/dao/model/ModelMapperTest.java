package com.rossotti.basketball.dao.model;

import java.io.IOException;
import java.io.InputStream;

import com.rossotti.basketball.client.service.JsonProvider;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelMapperTest {
	private final ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void deserialize_JsonToPojo_Official() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/officialModel.json");
		Official official = mapper.readValue(baseJson, Official.class);
		Assert.assertEquals("Cash", official.getLastName());
		baseJson.close();
	}

	@Test
	public void deserialize_JsonToPojo_Team() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/teamModel.json");
		Team team = mapper.readValue(baseJson, Team.class);
		Assert.assertEquals("TD Garden", team.getSiteName());
		baseJson.close();
	}

	@Test
	public void deserialize_JsonToPojo_Player() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/playerModel.json");
		Player player = mapper.readValue(baseJson, Player.class);
		Assert.assertEquals((short)220, player.getWeight().shortValue());
		baseJson.close();
	}

	@Test
	public void deserialize_JsonToPojo_RosterPlayer() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/rosterPlayerModel.json");
		RosterPlayer rosterPlayer = mapper.readValue(baseJson, RosterPlayer.class);
		Assert.assertEquals(Position.SF, rosterPlayer.getPosition());
		baseJson.close();
	}

	@Test
	public void deserialize_JsonToPojo_Game() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/gameModel.json");
		Game game = mapper.readValue(baseJson, Game.class);
		Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), game.getGameDateTime());
		baseJson.close();
	}

	@Test
	public void deserialize_JsonToPojo_Standing() throws IOException {
		InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/standingModel.json");
		Standing standing = mapper.readValue(baseJson, Standing.class);
		Assert.assertEquals((short)98, standing.getPointsAgainst().shortValue());
		baseJson.close();
	}
}