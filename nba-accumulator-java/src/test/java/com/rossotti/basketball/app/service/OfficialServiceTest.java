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
import com.rossotti.basketball.app.service.OfficialServiceBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.util.DateTimeUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class OfficialServiceTest {
	@Autowired
	private OfficialServiceBean officialServiceBean;

	private ObjectMapper mapper = JsonProvider.buildObjectMapper();

	@Test
	public void mapGameOfficials_Found() {
		GameDTO gameDTO = null;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient_Valid.json");
			gameDTO = mapper.readValue(baseJson, GameDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		OfficialDTO[] officialsDTO = gameDTO.officials;
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDTO.event_information.getStart_date_time());
		List<GameOfficial> officials = officialServiceBean.getGameOfficials(officialsDTO, gameDate);
		Assert.assertEquals(3, officials.size());
		Assert.assertEquals("Roe", officials.get(2).getOfficial().getLastName());
		Assert.assertEquals("45", officials.get(2).getOfficial().getNumber());
	}

	@Test(expected=NoSuchEntityException.class)
	public void mapGameOfficials_NotFound() {
		GameDTO gameDTO = null;
		try {
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient_Invalid.json");
			gameDTO = mapper.readValue(baseJson, GameDTO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		OfficialDTO[] officialsDTO = gameDTO.officials;
		LocalDate gameDate = DateTimeUtil.getLocalDate(gameDTO.event_information.getStart_date_time());
		officialServiceBean.getGameOfficials(officialsDTO, gameDate);
	}
}