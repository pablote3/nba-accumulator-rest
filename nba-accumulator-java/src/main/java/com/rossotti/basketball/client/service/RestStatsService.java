package com.rossotti.basketball.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service

public class RestStatsService {
	@Autowired
	private PropertyService propertyService;

	@Autowired
	private RestClientService restClientService;

	private final Logger logger = LoggerFactory.getLogger(RestStatsService.class);

	public StatsDTO retrieveBoxScore(String event) {
		StatsDTO statsDTO;
		try {
			String eventUrl = getEventUrl("xmlstats.urlBoxScore", event);
			statsDTO = (GameDTO)restClientService.retrieveStats(eventUrl, new GameDTO());
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			statsDTO = new GameDTO();
			statsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return statsDTO;
	}

	public StatsDTO retrieveRoster(String event) {
		StatsDTO statsDTO;
		try {
			String eventUrl = getEventUrl("xmlstats.urlRoster", event);
			statsDTO = (RosterDTO)restClientService.retrieveStats(eventUrl, new RosterDTO());
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			statsDTO = new RosterDTO();
			statsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return statsDTO;
	}

	public StatsDTO retrieveStandings(String event) {
		StatsDTO statsDTO;
		try {
			String eventUrl = getEventUrl("xmlstats.urlStandings", event);
			statsDTO = (StandingsDTO)restClientService.retrieveStats(eventUrl, new StandingsDTO());
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			statsDTO = new StandingsDTO();
			statsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return statsDTO;
	}

	private String getEventUrl(String propertyName, String event) throws PropertyException {
		String url = propertyService.getProperty_Http(propertyName);
		String eventUrl = url + event + ".json";
		return eventUrl;
	}
}