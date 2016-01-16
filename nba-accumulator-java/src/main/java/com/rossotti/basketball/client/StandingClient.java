package com.rossotti.basketball.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.rossotti.basketball.client.dto.Standings;

public class StandingClient {

	public static void main(String[] args) {

		final String authHeader = "Bearer " + "accessToken";

		String urlStanding = "https://erikberg.com/nba/standings/";
		String event = "20130131.json";
//		String fileStanding = "/home/pablote/pdrive/pwork/play/accumulator/config/fileStanding/2015_16";
//		Source source = Source.API;

		Client client = ClientBuilder.newBuilder().build();
		client.register(new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
				requestContext.getHeaders().add(HttpHeaders.USER_AGENT, "userAgentName");
			}
		});
		WebTarget target = client.target(urlStanding + event);
		Response response = target.request().get();
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
//		response.getHeaders().getFirst(key)
		
		Standings standings = (Standings)response.getEntity();
		
//		InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes()));
//		ByteArrayInputStream bais = new ByteArrayInputStream(((String) response.getEntity()).getBytes());
//		BufferedReader baseJson = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity().getBytes())));
//
//		//Read output in string format
//		String value = response.readEntity(String.class);
		System.out.println(standings.toString());
	}	
}
