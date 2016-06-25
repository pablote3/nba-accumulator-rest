package com.rossotti.basketball.client.service;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.service.FileClientService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class FileClientServiceTest {
	@Autowired
	private FileClientService fileClientService;

	@Test
	public void retrieveBoxScore_Found() throws IOException {
		String event = "20150415-utah-jazz-at-houston-rockets";
		GameDTO game = fileClientService.retrieveBoxScore(event);
		Assert.assertTrue(game.isFound());
		Assert.assertEquals("Utah Jazz", game.away_team.getFull_name());
	}
	@Test
	public void retrieveBoxScore_NotFound() throws IOException {
		String event = "20150415-utah-jazz-at-houston-rocketeers";
		GameDTO game = fileClientService.retrieveBoxScore(event);
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveRoster_Found() throws IOException {
		String event = "toronto-raptors";
		RosterDTO roster = fileClientService.retrieveRoster(event);
		Assert.assertTrue(roster.isFound());
		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
	}
	@Test
	public void retrieveRoster_NotFound() throws IOException {
		String event = "toronto-raptiers";
		RosterDTO roster = fileClientService.retrieveRoster(event);
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveStandings_Found() throws IOException {
		String event = "20141028";
		StandingsDTO standings = fileClientService.retrieveStandings(event);
		Assert.assertTrue(standings.isFound());
		Assert.assertEquals("philadelphia-76ers", standings.standing[0].getTeam_id());
	}
	@Test
	public void retrieveStandings_NotFound() throws IOException {
		String event = "20140236";
		StandingsDTO standings = fileClientService.retrieveStandings(event);
		Assert.assertTrue(standings.isNotFound());
	}
}