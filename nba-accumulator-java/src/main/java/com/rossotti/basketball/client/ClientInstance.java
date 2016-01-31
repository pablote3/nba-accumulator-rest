package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientInstance {
	//static???
//	private Client client = ClientBuilder.newBuilder().build().register(XmlStatsFilter.class);
//
//	public Client getClient() {
//		return client;
//	}
	
	private String clientId;
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientId() {
		return clientId;
	}
}
