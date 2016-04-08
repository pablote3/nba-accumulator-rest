package com.rossotti.basketball.app.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.service.RosterPlayerServiceBean;
import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.util.DateTimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RosterPlayerServiceBeanTest {
	@Autowired
	private RosterPlayerServiceBean rosterPlayerServiceBean;

	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void getBoxScorePlayers_Found() {
		GameDTO gameDTO = null;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient_Valid.json");
			gameDTO = mapper.readValue(baseJson, GameDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDTO.event_information.getStart_date_time());
		String awayTeamKey = gameDTO.away_team.getTeam_id();
		BoxScorePlayerDTO[] awayBoxScorePlayerDTO = gameDTO.away_stats;
		List<BoxScorePlayer> awayBoxScorePlayers = rosterPlayerServiceBean.getBoxScorePlayers(awayBoxScorePlayerDTO, gameDate, awayTeamKey);
		Assert.assertEquals(4, awayBoxScorePlayers.size());
		Assert.assertEquals((short)4, awayBoxScorePlayers.get(2).getReboundsDefense().shortValue());
		Assert.assertEquals("12", awayBoxScorePlayers.get(2).getRosterPlayer().getNumber());
		Assert.assertEquals("Caldwell-Pope", awayBoxScorePlayers.get(2).getRosterPlayer().getPlayer().getLastName());
	}

	@Test(expected=NoSuchEntityException.class)
	public void getBoxScorePlayers_NotFound() {
		GameDTO gameDTO = null;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient_Invalid.json");
			gameDTO = mapper.readValue(baseJson, GameDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BoxScorePlayerDTO[] awayBoxScorePlayerDTO = gameDTO.away_stats;
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDTO.event_information.getStart_date_time());
		String awayTeamKey = gameDTO.away_team.getTeam_id();
		rosterPlayerServiceBean.getBoxScorePlayers(awayBoxScorePlayerDTO, gameDate, awayTeamKey);
	}
}