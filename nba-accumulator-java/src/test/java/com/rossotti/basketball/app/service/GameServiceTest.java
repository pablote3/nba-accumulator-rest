package com.rossotti.basketball.app.service;

import java.io.IOException;
import java.io.InputStream;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.service.GameServiceBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.util.DateTimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GameServiceTest {
	@Autowired
	private GameServiceBean gameServiceBean;

	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void findPreviousGameDateTime_Found() {
		GameDTO gameDTO = null;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient_Valid.json");
			gameDTO = mapper.readValue(baseJson, GameDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDTO.event_information.getStart_date_time());
		String awayTeamKey = gameDTO.away_team.getTeam_id();
		LocalDateTime previousGameDate = gameServiceBean.findPreviousGameDateTime(gameDate, awayTeamKey);
		Assert.assertEquals(new LocalDateTime("2015-11-24T10:00"), previousGameDate);
	}
}