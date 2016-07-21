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
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service
public class RestClientRetrieverService {
	@Autowired
	private RestClientService restClientService;

	@Autowired
	private PropertyService propertyService;

	private final Logger logger = LoggerFactory.getLogger(GameBusiness.class);

	public GameDTO retrieveBoxScore(String event) {
		GameDTO dto = new GameDTO();
		try {
			String url = propertyService.getProperty_Http("xmlstats.urlBoxScore");
			String eventUrl = url + event + ".json";
			dto = (GameDTO)restClientService.retrieveStats(eventUrl, dto);
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
			dto = (RosterDTO)restClientService.retrieveStats(eventUrl, dto);
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
			dto = (StandingsDTO)restClientService.retrieveStats(eventUrl, dto);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			dto.setStatusCode(StatusCodeDTO.ServerException);
		}
		return dto;
	}
}