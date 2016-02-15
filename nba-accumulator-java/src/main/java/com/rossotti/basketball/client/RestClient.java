package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import com.rossotti.basketball.util.ResourceLoader;

public class RestClient {
	
	private static RestClient instance = new RestClient();
	private static Client client;

	private RestClient(){}

	public static RestClient getInstance() {
		return instance;
	}

	static {
		ClientRequestFilter filter = new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				String accessToken = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken");
				String userAgent = ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent");
				String authorization = "Bearer " + accessToken;
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authorization);
				requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
			}
		};
		
		client = ClientBuilder.newBuilder().build().register(filter);
	}
	
	public Client getClient() {
		return client;
	}
}