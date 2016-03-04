package com.rossotti.basketball.client.rest;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.rossotti.basketball.util.ResourceLoader;

@Configuration
@PropertySource("/WEB-INF/service.properties")
public class RestClient {
	@Value("${xmlstats.accessToken}")
	private static String accessToken;

	@Value("${xmlstats.userAgent}")
	private static String userAgent;

	private static RestClient instance = new RestClient();
	private static Client client;

	public RestClient(){}

	public static RestClient getInstance() {
		return instance;
	}

	static {
		ClientRequestFilter filter = new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				System.out.println("accessToken = " + accessToken);
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