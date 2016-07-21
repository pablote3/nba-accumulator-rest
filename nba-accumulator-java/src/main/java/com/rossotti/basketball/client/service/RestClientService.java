package com.rossotti.basketball.client.service;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service

public class RestClientService {
	@Autowired
	private PropertyService propertyService;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(RestClientService.class);
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

	private Client getClient() {
		client = ClientBuilder.newBuilder().build();
		client.register(clientFilter);
		logger.info('\n' + "Client service initialized");
		return client;
	}

	public StatsDTO retrieveStats(String url, StatsDTO statsDTO) {
		Response response = getClient().target(url).request().get();
		if (response.getStatus() != 200) {
			statsDTO.setStatusCode(StatusCodeDTO.NotFound);
			response.readEntity(String.class);
		}
		else {
			try {
				statsDTO = mapper.readValue(response.readEntity(String.class), statsDTO.getClass());
				statsDTO.setStatusCode(StatusCodeDTO.Found);
			}
			catch (JsonParseException jpe) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				jpe.printStackTrace();
			}
			catch (JsonMappingException jme) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				jme.printStackTrace();
			}
			catch (IOException ioe) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				ioe.printStackTrace();
			}
		}
		response.close();
		return statsDTO;
	}
}