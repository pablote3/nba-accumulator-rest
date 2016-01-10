package com.rossotti.basketball.mapper.pub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Official;
import com.rossotti.basketball.model.Player;
import com.rossotti.basketball.model.RosterPlayer;
import com.rossotti.basketball.model.RosterPlayer.Position;
import com.rossotti.basketball.model.Standing;
import com.rossotti.basketball.model.Team;

public class JsonModelTest {
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void deserialize_JsonToPojo_Official() {
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("official.json");
			File file = path.toFile();
			InputStream baseJson = new FileInputStream(file);
			Official official = mapper.readValue(baseJson, Official.class);
			Assert.assertEquals("Cash", official.getLastName());
			baseJson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Team() {
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("team.json");
			File file = path.toFile();
			InputStream baseJson = new FileInputStream(file);
			Team team = mapper.readValue(baseJson, Team.class);
			Assert.assertEquals("TD Garden", team.getSiteName());
			baseJson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_Player() {
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("player.json");
			File file = path.toFile();
			InputStream baseJson = new FileInputStream(file);
			Player player = mapper.readValue(baseJson, Player.class);
			Assert.assertEquals((short)220, player.getWeight().shortValue());
			baseJson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deserialize_JsonToPojo_RosterPlayer() {
		InputStream baseJson;
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("rosterPlayer.json");
			File file = path.toFile();
			baseJson = new FileInputStream(file);
			RosterPlayer rosterPlayer = mapper.readValue(baseJson, RosterPlayer.class);
			Assert.assertEquals(Position.SF, rosterPlayer.getPosition());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deserialize_JsonToPojo_Game() {
		InputStream baseJson;
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("game.json");
			File file = path.toFile();
			baseJson = new FileInputStream(file);
			Game game = mapper.readValue(baseJson, Game.class);
			Assert.assertEquals(new LocalDateTime("2015-10-28T20:00"), game.getGameDateTime());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deserialize_JsonToPojo_Standing() {
		try {
			Path path =  Paths.get(System.getProperty("config.test")).resolve("standing.json");
			File file = path.toFile();
			InputStream baseJson = new FileInputStream(file);
			Standing standing = mapper.readValue(baseJson, Standing.class);
			Assert.assertEquals((short)98, standing.getPointsAgainst().shortValue());
			baseJson.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
