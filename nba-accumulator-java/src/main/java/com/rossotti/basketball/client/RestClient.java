package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class RestClient {
	public static Client buildClient(final String accessToken, final String userAgent) {
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