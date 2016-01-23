package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientBean {
	private static Client client = ClientBuilder.newBuilder().build().register(XmlStatsFilter.class);

	public static Client getClient() {
		return client;
	}
}
