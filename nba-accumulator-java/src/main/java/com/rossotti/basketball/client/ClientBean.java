package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;

public class ClientBean {
	private ClientFilter clientFilter;
	
	//static???
	private Client client;

	public Client getClient() {
		client = ClientBuilder.newBuilder().build();
		client.register(getClientFilter());
		return client;
	}

	@Autowired
	public void setClientFilter(ClientFilter clientFilter) {
		this.clientFilter = clientFilter;
	}
	public ClientFilter getClientFilter() {
		return clientFilter;
	}
}