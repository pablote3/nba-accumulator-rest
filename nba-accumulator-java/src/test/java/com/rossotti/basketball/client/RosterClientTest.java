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
		String roster = clientBean.getClientId();
		System.out.println("Roster = " + roster);
//		RosterDTO roster = rosterClient.retrieveRoster("toronto-raptors");
//		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
//		RosterPlayerDTO rosterPlayer = roster.players[1];
//		Assert.assertEquals("Bismack", rosterPlayer.getFirst_name());
	}
}
