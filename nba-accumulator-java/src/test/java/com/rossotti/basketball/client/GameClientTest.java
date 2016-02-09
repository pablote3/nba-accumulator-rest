package com.rossotti.basketball.client;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GameClientTest {
	private static final String baseUrl = "https://erikberg.com/nba/boxscore/";

	@Autowired
	private ClientBean clientBean;

	@Ignore
	@Test
	public void retrieveGames() {
		String boxScoreUrl = baseUrl + "20151129-detroit-pistons-at-brooklyn-nets" + ".json";
		Response response = clientBean.getClient().target(boxScoreUrl).request().get();
		Assert.assertEquals(200, response.getStatus());
	}
}
