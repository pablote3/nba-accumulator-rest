package com.rossotti.basketball.client;

import org.springframework.beans.factory.annotation.Autowired;

public class ClientBean {
	
	private ClientInstance clientInstance;

	@Autowired
	public void setClientInstance(ClientInstance clientInstance) {
		this.clientInstance = clientInstance;
	}
	public ClientInstance getClientInstance() {
		return clientInstance;
	}
}