package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class ClientFilter implements ClientRequestFilter {
	String accessToken;
	String userAgent;
	final String authHeader = "Bearer " + accessToken;

	public ClientFilter(String accessToken, String userAgent) {
		this.accessToken = accessToken;
		this.userAgent = userAgent;
	}

	@Override
	public void filter(final ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
		requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
	}
}
