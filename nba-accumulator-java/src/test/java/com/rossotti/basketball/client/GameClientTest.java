package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.util.ThreadSleep;

public class GameClientTest {
	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String gameEvent = "20131101-utah-jazz-at-phoenix-suns";
		GameDTO game = GameClient.retrieveBoxScore(gameEvent);
		Assert.assertEquals(200, game.httpStatus);
		Assert.assertEquals("Utah Jazz", game.away_team.getFull_name());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String gameEvent = "20131101-utah-jazz-at-phoenix-sunnys";
		GameDTO game = GameClient.retrieveBoxScore(gameEvent);
		Assert.assertEquals(404, game.httpStatus);
	}
}