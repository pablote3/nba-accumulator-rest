package com.rossotti.basketball.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.rossotti.basketball.app.resource.ScoreResource;
import com.rossotti.basketball.dao.exception.PropertyException;

@Repository

@Configuration
@PropertySource("/resources/service.properties")
public class ClientBean {
	@Autowired
	private Environment env;
	
	private final Logger logger = LoggerFactory.getLogger(ScoreResource.class);
	private Client client;

	ClientRequestFilter clientFilter = new ClientRequestFilter() {
			@Override
			public void filter(ClientRequestContext requestContext) throws PropertyException {
				String accessToken = env.getProperty("xmlstats.accessToken");
				String userAgent = env.getProperty("xmlstats.userAgent");
				String authHeader = "Bearer " + accessToken;
				requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
				requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
			}
		};

		public Client getClient() {
			client = ClientBuilder.newBuilder().build();
			client.register(clientFilter);
			logger.info('\n' + "Client bean initialized");
			return client;
		}
}