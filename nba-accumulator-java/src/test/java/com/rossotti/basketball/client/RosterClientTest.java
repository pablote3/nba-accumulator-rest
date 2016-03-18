package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.util.ThreadSleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RosterClientTest {
	@Autowired
	private RestClientBean restClientBean;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveRoster_200() throws IOException {
		String event = "https://erikberg.com/nba/roster/toronto-raptors.json";
		RosterDTO roster = restClientBean.retrieveRoster(event);
		Assert.assertEquals(200, roster.httpStatus);
		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
	}

	@Test
	public void retrieveRoster_404() throws IOException {
		String event = "https://erikberg.com/nba/roster/toronto-raptiers.json";
		RosterDTO roster = restClientBean.retrieveRoster(event);
		Assert.assertEquals(404, roster.httpStatus);
	}
}