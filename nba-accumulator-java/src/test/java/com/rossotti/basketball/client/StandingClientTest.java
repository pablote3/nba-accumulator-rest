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
public class StandingClientTest {
	private static final String baseUrl = "https://erikberg.com/nba/standings/";

	@Autowired
	private ClientBean clientBean;

	@Ignore
	@Test
	public void retrieveStandings() {
		String standingsUrl = baseUrl + "20130131" + ".json";
		Response response = clientBean.getClient().target(standingsUrl).request().get();
		Assert.assertEquals(200, response.getStatus());
	}
}
