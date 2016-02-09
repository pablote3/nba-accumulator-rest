package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rossotti.basketball.util.PropertyResourceLoader;

@Repository
public class ClientBean {
	private Client client;
	private PropertyResourceLoader resourceProperties;
	
	@Autowired
	public void setResourceProperties(PropertyResourceLoader resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	public PropertyResourceLoader getResourceProperties() {
		return resourceProperties;
	}

	ClientRequestFilter clientFilter = new ClientRequestFilter() {
		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			String accessToken = getResourceProperties().getProperties().getProperty("xmlstats.accessToken");
			String authHeader = "Bearer " + accessToken;
			String userAgent = getResourceProperties().getProperties().getProperty("xmlstats.userAgent");
			requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
			requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
		}
	};

	public Client getClient() {
		client = ClientBuilder.newBuilder().build();
		client.register(clientFilter);
		return client;
	}
}