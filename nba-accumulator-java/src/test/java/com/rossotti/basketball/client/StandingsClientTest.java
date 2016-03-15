package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.util.ThreadSleep;

public class StandingsClientTest {
	RestClient client;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
		client = new RestClient();
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String event = "20130131";

		StandingsDTO standings = client.retrieveStandings(event);
		Assert.assertEquals(200, standings.httpStatus);
		Assert.assertEquals("miami-heat", standings.standing[0].getTeam_id());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String event = "20130132";

		StandingsDTO standings = client.retrieveStandings(event);
		Assert.assertEquals(404, standings.httpStatus);
	}
}