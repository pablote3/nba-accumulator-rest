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
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service

public class RestStatsService {
	@Autowired
	private PropertyService propertyService;

	@Autowired
	private RestClientService restClientService;

	private final Logger logger = LoggerFactory.getLogger(RestStatsService.class);

	public GameDTO retrieveBoxScore(String event) {
		GameDTO gameDTO = new GameDTO();
		try {
			String eventUrl = getEventUrl("xmlstats.urlBoxScore", event);
			gameDTO = (GameDTO)restClientService.retrieveStats(eventUrl, gameDTO);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			gameDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return gameDTO;
	}

	public RosterDTO retrieveRoster(String event) {
		RosterDTO rosterDTO = new RosterDTO();
		try {
			String eventUrl = getEventUrl("xmlstats.urlRoster", event);
			rosterDTO = (RosterDTO)restClientService.retrieveStats(eventUrl, rosterDTO);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			rosterDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return rosterDTO;
	}

	public StandingsDTO retrieveStandings(String event) {
		StandingsDTO standingsDTO = new StandingsDTO();
		try {
			String eventUrl = getEventUrl("xmlstats.urlStandings", event);
			standingsDTO = (StandingsDTO)restClientService.retrieveStats(eventUrl, standingsDTO);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return standingsDTO;
	}

	private String getEventUrl(String propertyName, String event) throws PropertyException {
		String url = propertyService.getProperty_Http(propertyName);
		String eventUrl = url + event + ".json";
		return eventUrl;
	}
}