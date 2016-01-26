package com.rossotti.basketball.client;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class StandingClientTest {

	@Autowired
	private StandingClient standingClient;

	@Ignore
	@Test
	public void retrieveStandings() {
		StandingsDTO standings = standingClient.retrieveStandings("20130131");
		Assert.assertEquals(new DateTime("2013-01-31T05:00:00", DateTimeZone.UTC), standings.standings_date);
		StandingDTO standing = standings.standing[1];
		Assert.assertEquals("5-5", standing.getLast_ten());
	}
}
