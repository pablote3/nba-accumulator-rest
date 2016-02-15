package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.util.ThreadSleep;

public class RosterClientTest {
	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String rosterEvent = "utah-jazz";
		RosterDTO roster = RosterClient.retrieveRoster(rosterEvent);
		Assert.assertEquals(200, roster.httpStatus);
		Assert.assertEquals("Utah Jazz", roster.team.getFull_name());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String gameEvent = "utah-jazzers";
		RosterDTO roster = RosterClient.retrieveRoster(gameEvent);
		Assert.assertEquals(404, roster.httpStatus);
	}
}