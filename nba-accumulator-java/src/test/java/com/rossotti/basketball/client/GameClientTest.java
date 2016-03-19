package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.util.ThreadSleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GameClientTest {
	@Autowired
	private RestClientBean restClientBean;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String event = "20160311-houston-rockets-at-boston-celtics";
		GameDTO game = restClientBean.retrieveBoxScore(event);
		Assert.assertEquals(200, game.httpStatus);
		Assert.assertEquals("Houston Rockets", game.away_team.getFull_name());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String event = "20160311-boston-celtics-at-houston-rocketeers";
		GameDTO game = restClientBean.retrieveBoxScore(event);
		Assert.assertEquals(404, game.httpStatus);
	}
}