package com.rossotti.basketball.client.service;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.util.ThreadSleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RestClientServiceTest {
	@Autowired
	private RestClientService restClientService;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveBoxScore_Found() throws IOException {
		String event = "20160311-houston-rockets-at-boston-celtics";
		GameDTO game = restClientService.retrieveBoxScore(event);
		Assert.assertTrue(game.isFound());
		Assert.assertEquals("Houston Rockets", game.away_team.getFull_name());
	}
	@Test
	public void retrieveBoxScore_NotFound() throws IOException {
		String event = "20160311-houston-rockets-at-boston-celticsss";
		GameDTO game = restClientService.retrieveBoxScore(event);
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveRoster_Found() throws IOException {
		String event = "toronto-raptors";
		RosterDTO roster = restClientService.retrieveRoster(event);
		Assert.assertTrue(roster.isFound());
		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
	}
	@Test
	public void retrieveRoster_NotFound() throws IOException {
		String event = "toronto-raptiers";
		RosterDTO roster = restClientService.retrieveRoster(event);
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveStandings_Found() throws IOException {
		String event = "20160216";
		StandingsDTO standings = restClientService.retrieveStandings(event);
		Assert.assertTrue(standings.isFound());
		Assert.assertEquals("cleveland-cavaliers", standings.standing[0].getTeam_id());
	}
	@Test
	public void retrieveStandings_NotFound() throws IOException {
		String event = "20160236";
		StandingsDTO standings = restClientService.retrieveStandings(event);
		Assert.assertTrue(standings.isNotFound());
	}
}