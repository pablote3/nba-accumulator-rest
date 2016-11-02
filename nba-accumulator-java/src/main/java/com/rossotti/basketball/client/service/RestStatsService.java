package com.rossotti.basketball.client.service;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service

public class RestStatsService {
	private final PropertyService propertyService;

	private final RestClientService restClientService;

	private final Logger logger = LoggerFactory.getLogger(RestStatsService.class);

	@Autowired
	public RestStatsService(RestClientService restClientService, PropertyService propertyService) {
		this.restClientService = restClientService;
		this.propertyService = propertyService;
	}

	public GameDTO retrieveBoxScore(String event, LocalDate asOfDate) {
		GameDTO gameDTO = new GameDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlBoxScore");
			gameDTO = (GameDTO)restClientService.retrieveStats(baseUrl, event, gameDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			gameDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return gameDTO;
	}

	public RosterDTO retrieveRoster(String event, LocalDate asOfDate) {
		RosterDTO rosterDTO = new RosterDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlRoster");
			rosterDTO = (RosterDTO)restClientService.retrieveStats(baseUrl, event, rosterDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			rosterDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return rosterDTO;
	}

	public StandingsDTO retrieveStandings(String event, LocalDate asOfDate) {
		StandingsDTO standingsDTO = new StandingsDTO();
		try {
			String baseUrl = propertyService.getProperty_Http("xmlstats.urlStandings");
			standingsDTO = (StandingsDTO)restClientService.retrieveStats(baseUrl, event, standingsDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("Property exception = " + pe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return standingsDTO;
	}
}