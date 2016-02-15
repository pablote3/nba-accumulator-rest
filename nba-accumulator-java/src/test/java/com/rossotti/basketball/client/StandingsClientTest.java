package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.util.ThreadSleep;

public class StandingsClientTest {
	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String standingsEvent = "20130131";
		StandingsDTO standings = StandingsClient.retrieveStandings(standingsEvent);
		Assert.assertEquals(200, standings.httpStatus);
		Assert.assertEquals("miami-heat", standings.standing[0].getTeam_id());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String standingsEvent = "20130132";
		StandingsDTO standings = StandingsClient.retrieveStandings(standingsEvent);
		Assert.assertEquals(404, standings.httpStatus);
	}
}