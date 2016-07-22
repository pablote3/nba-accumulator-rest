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
public class FileStatsService {
	@Autowired
	private PropertyService propertyService;

	@Autowired
	private FileClientService fileClientService;

	private final Logger logger = LoggerFactory.getLogger(FileStatsService.class);

	public GameDTO retrieveBoxScore(String event) {
		GameDTO gameDTO = new GameDTO();
		try {
			String path = propertyService.getProperty_Path("xmlstats.fileBoxScore");
			gameDTO = (GameDTO)fileClientService.retrieveStats(path, event, gameDTO);
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
			String path = propertyService.getProperty_Path("xmlstats.fileRoster");
			rosterDTO = (RosterDTO)fileClientService.retrieveStats(path, event, rosterDTO);
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
			String path = propertyService.getProperty_Path("xmlstats.fileStandings");
			standingsDTO = (StandingsDTO)fileClientService.retrieveStats(path, event, standingsDTO);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return standingsDTO;
	}
}