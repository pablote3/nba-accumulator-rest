package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientInstance {
	//static???
	private Client client = ClientBuilder.newBuilder().build().register(XmlStatsFilter.class);

	public Client getClient() {
		return client;
	}
	
	private String env;
	
	public void setEnv(String env) {
		this.env = env;
	}
	public String getEnv() {
		return env;
	}
}
