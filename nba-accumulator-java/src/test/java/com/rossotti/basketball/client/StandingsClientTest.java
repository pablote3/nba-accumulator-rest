package com.rossotti.basketball.client;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.util.ThreadSleep;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class StandingsClientTest {
	@Autowired
	private RestClientBean restClientBean;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveStandings_200() throws IOException {
		String event = "https://erikberg.com/nba/standings/20160216.json";
		StandingsDTO standings = restClientBean.retrieveStandings(event);
		Assert.assertEquals(200, standings.httpStatus);
		Assert.assertEquals("cleveland-cavaliers", standings.standing[0].getTeam_id());
	}

	@Test
	public void retrieveStandings_404() throws IOException {
		String event = "https://erikberg.com/nba/standings/20160236.json";
		StandingsDTO standings = restClientBean.retrieveStandings(event);
		Assert.assertEquals(404, standings.httpStatus);
	}
}