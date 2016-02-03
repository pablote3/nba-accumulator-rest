package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;

import com.rossotti.basketball.util.PropertyResourceLoader;

public class ClientFilter implements ClientRequestFilter {
	private PropertyResourceLoader resourceProperties;

	@Autowired
	public void setResourceProperties(PropertyResourceLoader resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	public PropertyResourceLoader getResourceProperties() {
		return resourceProperties;
	}

	@Override
	public void filter(final ClientRequestContext requestContext) throws IOException {
		String accessToken = getResourceProperties().getProperties().getProperty("xmlstats.accessToken");
		String authHeader = "Bearer " + accessToken;
		String userAgent = getResourceProperties().getProperties().getProperty("xmlstats.userAgent");
		requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
		requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
	}
}
