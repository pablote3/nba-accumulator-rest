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
	public void retrieveBoxScore_200() throws IOException {
		String event = "20150415-utah-jazz-at-houston-rockets";
		GameDTO game = fileClientService.retrieveBoxScore(event);
		Assert.assertEquals(200, game.httpStatus);
		Assert.assertEquals("Utah Jazz", game.away_team.getFull_name());
	}
	@Test
	public void retrieveBoxScore_404() throws IOException {
		String event = "20160311-houston-rockets-at-boston-celticsss";
		GameDTO game = fileClientService.retrieveBoxScore(event);
		Assert.assertEquals(404, game.httpStatus);
	}

	@Test
	public void retrieveRoster_200() throws IOException {
		String event = "toronto-raptors-20141029";
		RosterDTO roster = fileClientService.retrieveRoster(event);
		Assert.assertEquals(200, roster.httpStatus);
		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
	}
	@Test
	public void retrieveRoster_404() throws IOException {
		String event = "toronto-raptiers-20141029";
		RosterDTO roster = fileClientService.retrieveRoster(event);
		Assert.assertEquals(404, roster.httpStatus);
	}

	@Test
	public void retrieveStandings_200() throws IOException {
		String event = "20160216";
		StandingsDTO standings = fileClientService.retrieveStandings(event);
		Assert.assertEquals(200, standings.httpStatus);
		Assert.assertEquals("cleveland-cavaliers", standings.standing[0].getTeam_id());
	}
	@Test
	public void retrieveStandings_404() throws IOException {
		String event = "20160236";
		StandingsDTO standings = fileClientService.retrieveStandings(event);
		Assert.assertEquals(404, standings.httpStatus);
	}
}