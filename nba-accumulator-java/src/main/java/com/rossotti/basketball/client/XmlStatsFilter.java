package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class XmlStatsFilter implements ClientRequestFilter {
	final String accessToken = "token";
	final String userAgentName = "pablote/1.3 (rossotti.paul@gmail.com)";
	final String authHeader = "Bearer " + accessToken;
	
	@Override
	public void filter(final ClientRequestContext requestContext) throws IOException {
		requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
		requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgentName);
	}
}
