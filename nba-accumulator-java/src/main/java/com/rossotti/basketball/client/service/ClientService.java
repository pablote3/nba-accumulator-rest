package com.rossotti.basketball.client.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;

@Service

public class ClientService {
	@Autowired
	private PropertyService propertyService;
	
	private final Logger logger = LoggerFactory.getLogger(ClientService.class);
	private Client client;

	ClientRequestFilter clientFilter = new ClientRequestFilter() {
		@Override
		public void filter(ClientRequestContext requestContext) throws PropertyException {
			String accessToken = propertyService.getProperty_String("xmlstats.accessToken");
			String userAgent = propertyService.getProperty_String("xmlstats.userAgent");
			String authHeader = "Bearer " + accessToken;
			requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
			requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
		}
	};

	public Client getClient() {
		client = ClientBuilder.newBuilder().build();
		client.register(clientFilter);
		logger.info('\n' + "Client service initialized");
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
}