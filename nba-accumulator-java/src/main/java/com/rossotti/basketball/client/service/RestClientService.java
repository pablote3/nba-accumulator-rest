package com.rossotti.basketball.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.business.GameBusiness;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service
public class RestClientService {
	@Autowired
	private ClientService clientService;

	@Autowired
	private PropertyService propertyService;

	private final Logger logger = LoggerFactory.getLogger(GameBusiness.class);

	private StatsDTO retrieveStats(String url, StatsDTO statsDTO) {
		return clientService.getStatsDTO(url, statsDTO);
	}

	public GameDTO retrieveBoxScore(String event) {
		GameDTO dto = new GameDTO();
		try {
			String url = propertyService.getProperty_Http("xmlstats.urlBoxScore");
			String eventUrl = url + event + ".json";
			dto = (GameDTO)retrieveStats(eventUrl, dto);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			dto.setStatusCode(StatusCodeDTO.ServerException);
		}
		return dto;
	}

	public RosterDTO retrieveRoster(String event) {
		RosterDTO dto = new RosterDTO();
		try {
			String url = propertyService.getProperty_Http("xmlstats.urlRoster");
			String eventUrl = url + event + ".json";
			dto = (RosterDTO)retrieveStats(eventUrl, dto);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			dto.setStatusCode(StatusCodeDTO.ServerException);
		}
		return dto;
	}

	public StandingsDTO retrieveStandings(String event) {
		StandingsDTO dto = new StandingsDTO();
		try {
			String url = propertyService.getProperty_Http("xmlstats.urlStandings");
			String eventUrl = url + event + ".json";
			dto = (StandingsDTO)retrieveStats(eventUrl, dto);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			dto.setStatusCode(StatusCodeDTO.ServerException);
		}
		return dto;
	}

	public ClientService getClientService() {
		return clientService;
	}
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}
}