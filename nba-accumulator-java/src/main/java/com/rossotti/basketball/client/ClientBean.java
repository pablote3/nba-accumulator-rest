package com.rossotti.basketball.client;

import org.springframework.beans.factory.annotation.Autowired;

public class ClientBean {
	
	//private ClientInstance clientInstance;
	private String clientId;

	@Autowired
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientId() {
		return clientId;
	}

//	public void setClientInstance(ClientInstance clientInstance) {
//		this.clientInstance = clientInstance;
//	}
//	public ClientInstance getClientInstance() {
//		return clientInstance;
//	}
}