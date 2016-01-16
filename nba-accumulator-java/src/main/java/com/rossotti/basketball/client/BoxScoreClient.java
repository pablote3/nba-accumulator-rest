package com.rossotti.basketball.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.rossotti.basketball.model.Game;

public class BoxScoreClient {
	static final String AUTHORIZATION = "Authorization";
	static final String USER_AGENT = "User-agent";
	static final String ACCEPT_ENCODING = "Accept-encoding";
	static final String GZIP = "gzip";

	public static void main(String[] args) {
		String accessToken = "Bearer 18ac608e-2d25-4124-ab9a-5daa98c5773c";
		String userAgentName = "pablote/1.3 (rossotti.paul@gmail.com)";
		String urlBoxScore = "https://erikberg.com/nba/boxscore/";
		String fileBoxScore = "/home/pablote/pdrive/pwork/rest/accumulator/config/fileBoxScore/2015_16";
		Source source = Source.API;
		String event = "20160114-dallas-mavericks-at-oklahoma-city-thunder.json";
		
		Client client = ClientBuilder.newBuilder().build();
		WebTarget target = client.target(urlBoxScore + event);
//		Response response = target.request().post(Entity.entity(object, "application/json"));
//	
//		//Read output in string format
//		String value = response.readEntity(String.class);
	}	
}
