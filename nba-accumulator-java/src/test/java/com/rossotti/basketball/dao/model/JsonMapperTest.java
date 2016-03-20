package com.rossotti.basketball.dao.model;

import java.io.IOException;
import java.io.InputStream;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.model.RosterPlayer.Position;

public class JsonMapperTest {
	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void deserialize_JsonToPojo_Official() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/officialModel.json");
			Official official = mapper.readValue(baseJson, Official.class);
			Assert.assertEquals("Cash", official.getLastName());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Team() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/teamModel.json");
			Team team = mapper.readValue(baseJson, Team.class);
			Assert.assertEquals("TD Garden", team.getSiteName());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Player() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/playerModel.json");
			Player player = mapper.readValue(baseJson, Player.class);
			Assert.assertEquals((short)220, player.getWeight().shortValue());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_RosterPlayer() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/rosterPlayerModel.json");
			RosterPlayer rosterPlayer = mapper.readValue(baseJson, RosterPlayer.class);
			Assert.assertEquals(Position.SF, rosterPlayer.getPosition());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Game() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/gameModel.json");
			Game game = mapper.readValue(baseJson, Game.class);
			Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), game.getGameDateTime());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Standing() {
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockModel/standingModel.json");
			Standing standing = mapper.readValue(baseJson, Standing.class);
			Assert.assertEquals((short)98, standing.getPointsAgainst().shortValue());
			baseJson.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
