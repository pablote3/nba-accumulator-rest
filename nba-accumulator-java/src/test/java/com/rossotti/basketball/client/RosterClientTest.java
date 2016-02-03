package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

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
	@Autowired
	private ClientBean clientBean;
	
	private static final String baseUrl = "https://erikberg.com/nba/roster/";

//	@Ignore
	@Test
	public void retrieveRoster() {
		String rosterUrl = baseUrl + "toronto-raptors" + ".json";
		Client client = clientBean.getClient();
		Response response = clientBean.getClient().target(rosterUrl).request().get();
		System.out.println("Here");


//		RosterDTO roster = clientBean.retrieveRoster("toronto-raptors");
//		Assert.assertEquals("Toronto Raptors", roster.team.getFull_name());
//		RosterPlayerDTO rosterPlayer = roster.players[1];
//		Assert.assertEquals("Bismack", rosterPlayer.getFirst_name());
	}
}
