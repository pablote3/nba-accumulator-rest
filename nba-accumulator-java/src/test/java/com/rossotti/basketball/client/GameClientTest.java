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

import com.rossotti.basketball.client.dto.GameDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GameClientTest {

	@Autowired
	private GameClient boxScoreClient;

	@Ignore
	@Test
	public void retrieveGames() {
		GameDTO game = boxScoreClient.retrieveBoxScore("20151129-detroit-pistons-at-brooklyn-nets");
		Assert.assertEquals(new DateTime("2015-11-29T23:00:00", DateTimeZone.UTC), game.event_information.getStart_date_time());
		Assert.assertEquals("Detroit Pistons", game.away_team.getFull_name());
		Assert.assertEquals(19, game.away_period_scores[1]);
		Assert.assertTrue(game.away_totals.getPoints().equals((short)83));
		Assert.assertTrue(game.away_stats[1].getPoints().equals((short)7));
		Assert.assertEquals("Roe", game.officials[1].getLast_name());
	}
}
