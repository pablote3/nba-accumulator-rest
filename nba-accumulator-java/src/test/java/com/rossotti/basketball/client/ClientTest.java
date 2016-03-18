package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.rossotti.basketball.util.ThreadSleep;

public class ClientTest {
	private Client client;

	@Before
	public void setUp() {
		ThreadSleep.sleep(10);
	}

	@Ignore
	@Test
	public void retrieveRoster_200() throws IOException {
		String accessToken = "validAccessToken";
		String userAgent = "validUserAgent";
		client = buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(200, status);
	}

	@Test
	public void retrieveRoster_401() throws IOException {
		String accessToken = "badToken";
		String userAgent = "validUserAgent";
		client = buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(401, status);
	}

	@Ignore
	@Test
	public void retrieveRoster_404() throws IOException {
		String accessToken = "validAccessToken";
		String userAgent = "validUserAgent";
		client = buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raps.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(404, status);
	}

	@Ignore
	@Test
	public void retrieveRoster_403() throws IOException {
		//could cause ban of IP
		String accessToken = "validAccessToken";
		String userAgent = "badUserAgent";
		client = buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(403, status);
	}

	@Ignore
	@Test
	public void retrieveRoster_429() throws IOException {
		//sending more than 6 requests in a minute is counted against account
		String accessToken = "validAccessToken";
		String userAgent = "validUserAgent";
		client = buildClient(accessToken, userAgent);
		String rosterUrl = "https://erikberg.com/nba/roster/toronto-raptors.json";
		int status200 = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(200, status200);
		ThreadSleep.sleep(1);
		int status429 = client.target(rosterUrl).request().get().getStatus();
		Assert.assertEquals(429, status429);
	}

	private Client buildClient(final String accessToken, final String userAgent) {
		ClientRequestFilter filter = new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				String authorization = "Bearer " + accessToken;
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authorization);
				requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
			}
		};
		return ClientBuilder.newBuilder().build().register(filter);
	}
}