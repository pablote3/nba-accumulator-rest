package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;

public class ClientBean {

	private ClientFilter clientFilter;
	
	//static???
//	private Client client = ClientBuilder.newBuilder().build().register(getClientFilter());
//
//	public Client getClient() {
//		return client;
//	}

	@Autowired
	public void setClientFilter(ClientFilter clientFilter) {
		this.clientFilter = clientFilter;
	}
	public ClientFilter getClientFilter() {
		return clientFilter;
	}
}