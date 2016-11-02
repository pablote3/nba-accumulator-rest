package com.rossotti.basketball.client.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class RestClientService {
	private final PropertyService propertyService;

	private static final ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(RestClientService.class);

	private final ClientRequestFilter clientFilter = new ClientRequestFilter() {
		public void filter(ClientRequestContext requestContext) throws PropertyException {
			String accessToken = propertyService.getProperty_String("xmlstats.accessToken");
			String userAgent = propertyService.getProperty_String("xmlstats.userAgent");
			String authHeader = "Bearer " + accessToken;
			requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
			requestContext.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
		}
	};

	@Autowired
	public RestClientService(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	private Client getClient() {
		Client client = ClientBuilder.newBuilder().build();
		client.register(clientFilter);
		logger.debug("Client service initialized");
		return client;
	}

	public StatsDTO retrieveStats(String baseUrl, String event, StatsDTO statsDTO, LocalDate asOfDate) {
		String eventUrl = baseUrl + event + ".json";
		Response response = getClient().target(eventUrl).request().get();
		if (response.getStatus() == 401) {
			logger.info("Invalid token supplied on client request - returning http status = 401");
			statsDTO.setStatusCode(StatusCodeDTO.NotFound);
			response.readEntity(String.class);
		}
		else if (response.getStatus() != 200) {
			logger.info("Unable to retrieve client request - returning http status = " + response.getStatus());
			statsDTO.setStatusCode(StatusCodeDTO.NotFound);
			response.readEntity(String.class);
		}
		else {
			try {
				String baseJson = response.readEntity(String.class);
				if (statsDTO instanceof RosterDTO) {
					String file = propertyService.getProperty_Path("xmlstats.fileRoster") + "/" + event + "-" + DateTimeUtil.getStringDateNaked(asOfDate) + ".json";
					OutputStream outputStream = new FileOutputStream(file, false);
					outputStream.write(baseJson.getBytes());
					outputStream.close();
				}
				statsDTO = mapper.readValue(baseJson, statsDTO.getClass());
				statsDTO.setStatusCode(StatusCodeDTO.Found);
			} catch (IOException ioe) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				ioe.printStackTrace();
			}
		}
		response.close();
		return statsDTO;
	}
}