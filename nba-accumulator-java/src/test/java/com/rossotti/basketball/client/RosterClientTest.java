package com.rossotti.basketball.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class RosterClientTest {

//	private RosterClient rosterClient;
	@Autowired
	private ClientBean clientBean;

//	@Ignore
	@Test
	public void retrieveRoster() {
		System.out.println("AccessToken = " + clientBean.getClientFilter().accessToken);
		System.out.println("UserAgent = " + clientBean.getClientFilter().userAgent);
//		RosterDTO roster = rosterClient.retrieveRoster("toronto-raptors");
//		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
//		RosterPlayerDTO rosterPlayer = roster.players[1];
//		Assert.assertEquals("Bismack", rosterPlayer.getFirst_name());
	}
}
