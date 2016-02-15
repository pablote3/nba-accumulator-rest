package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.rossotti.basketball.util.ResourceLoader;
import com.rossotti.basketball.util.ThreadSleep;

public class RestClientTest {
	private Client client;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Test
	public void retrieveRoster_200() throws IOException {
		String accessToken = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken");
		String userAgent = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent");
		client = RestClient.buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(200, status);
	}

	@Test
	public void retrieveRoster_401() throws IOException {
		String accessToken = "badToken";
		String userAgent = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent");
		client = RestClient.buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(401, status);
	}

	@Test
	public void retrieveRoster_404() throws IOException {
		String accessToken = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken");
		String userAgent = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent");
		client = RestClient.buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raps.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(404, status);
	}

//	@Ignore //could cause ban of IP
//	@Test
//	public void retrieveRoster_403() throws IOException {
//		String accessToken = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken");
//		String userAgent = "badUserAgent";
//		client = RestClient.buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
//		int status = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(403, status);
//	}

//	@Ignore //sending more than 6 requests in a minute is counted against account
//	@Test
//	public void retrieveRoster_429() throws IOException {
//		String accessToken = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken");
//		String userAgent = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent");
//		client = RestClient.buildClient(accessToken, userAgent);
//		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
//		int status200 = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(200, status200);
//		sleep(1);
//		client = RestClient.buildClient(accessToken, userAgent);
//		int status429 = client.target(rosterUrl).request().get().getStatus();
//		Assert.assertEquals(429, status429);
//	}
}