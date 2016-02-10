package com.rossotti.basketball.client;

import static org.mockito.Mockito.stub;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.util.PropertyResourceLoader;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ClientTestValid {
	private Client client;
	private PropertyResourceLoader resourceProperties;

	@Autowired
	public void setResourceProperties(PropertyResourceLoader resourceProperties) {
		this.resourceProperties = resourceProperties;
	}
	public PropertyResourceLoader getResourceProperties() {
		return resourceProperties;
	}

	@Mock
	private ClientBean clientBean;

	@InjectMocks
	private RosterClient rosterClient = new RosterClient();

	@Before
	public void initializeMockito() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void retrieveRoster_200() {
		createClientBean();
		stub(rosterClient.getClientBean()).toReturn(clientBean);
		RosterDTO response = rosterClient.retrieveRoster("toronto-raptors");
		Assert.assertEquals("TOR", response.team.getAbbreviation());
	}
	
	private ClientRequestFilter clientFilter = new ClientRequestFilter() {
		@Override
		public void filter(ClientRequestContext requestContext) throws IOException {
			String accessToken = getResourceProperties().getProperties().getProperty("xmlstats.accessToken");
			String authHeader = "Bearer " + accessToken;
			String userAgent = getResourceProperties().getProperties().getProperty("xmlstats.userAgent");
			requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
			requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
			System.out.println("userAgent = " + userAgent);
		}
	};
	
	private void createClientBean() {
		client = ClientBuilder.newBuilder().build();
		client.register(clientFilter);
		clientBean.setClient(client);
	}
}
