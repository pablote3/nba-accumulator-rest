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
public class ClientBeanTest {

	@Autowired
	private ClientBean clientBean;

	@Ignore
	@Test
	public void retrieveGames() {
		String boxScoreUrl = "https://erikberg.com/nba/boxscore/20151129-detroit-pistons-at-brooklyn-nets.json";
		Response response = clientBean.getClient().target(boxScoreUrl).request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Ignore
	@Test
	public void retrieveRoster() {
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		Response response = clientBean.getClient().target(rosterUrl).request().get();
		Assert.assertEquals(200, response.getStatus());
	}

	@Ignore
	@Test
	public void retrieveStandings() {
		String standingsUrl = "https://erikberg.com/nba/standings/20130131.json";
		Response response = clientBean.getClient().target(standingsUrl).request().get();
		Assert.assertEquals(200, response.getStatus());
	}
}
