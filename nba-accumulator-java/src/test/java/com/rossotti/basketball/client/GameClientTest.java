package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.util.ThreadSleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GameClientTest {
	@Autowired
	private ClientBean clientBean;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveGame_200() throws IOException {
		String event = "https://erikberg.com/nba/boxscore/20160311-boston-celtics-at-houston-rockets.json";
		Client client = clientBean.getClient();
		Response response = client.target(event).request().get();
		Assert.assertEquals(200, response.getStatus());
//		Assert.assertEquals("Utah Jazz", game.away_team.getFull_name());
	}

	@Test
	public void retrieveGame_404() throws IOException {
		String event = "https://erikberg.com/nba/boxscore/20160311-boston-celtics-at-houston-rocketeers.json";
		Client client = clientBean.getClient();
		Response response = client.target(event).request().get();
		Assert.assertEquals(404, response.getStatus());
	}
}