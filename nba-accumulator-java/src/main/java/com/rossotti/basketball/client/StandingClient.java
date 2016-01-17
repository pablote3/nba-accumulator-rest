package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.client.dto.Standings;

public class StandingClient {
	
	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public static void main(String[] args) {


		String event = "20130131.json";
//		String fileStanding = "/home/pablote/pdrive/pwork/play/accumulator/config/fileStanding/2015_16";
//		Source source = Source.API;

		Client client = ClientBuilder.newBuilder().build();
		client.register(new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws IOException {
//				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
//				requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgentName);
				
//				requestContext.getHeaders().add(HttpHeaders.ACCEPT_ENCODING, "gzip");
			}
		});
		WebTarget target = client.target(event);
		Response response = target.request().get();
		
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

//		InputStream baseJson = new InputStreamReader((InputStream) response.getEntity(), StandardCharsets.UTF_8);
		
//		Standings standings = response.readEntity(Standings.class);
		
		Standings standings = null;
		try {
			standings = mapper.readValue(response.readEntity(String.class), Standings.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(standings);
	}
}
