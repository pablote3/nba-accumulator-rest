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
public class FileStatsService {
	private final PropertyService propertyService;

	private final FileClientService fileClientService;

	private final Logger logger = LoggerFactory.getLogger(FileStatsService.class);

	@Autowired
	public FileStatsService(PropertyService propertyService, FileClientService fileClientService) {
		this.propertyService = propertyService;
		this.fileClientService = fileClientService;
	}

	public GameDTO retrieveBoxScore(String event, LocalDate asOfDate) {
		GameDTO gameDTO = new GameDTO();
		try {
			String path = propertyService.getProperty_Path("xmlstats.fileBoxScore");
			gameDTO = (GameDTO)fileClientService.retrieveStats(path, event, gameDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			gameDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return gameDTO;
	}

	public RosterDTO retrieveRoster(String event, LocalDate asOfDate) {
		RosterDTO rosterDTO = new RosterDTO();
		try {
			String path = propertyService.getProperty_Path("xmlstats.fileRoster");
			rosterDTO = (RosterDTO)fileClientService.retrieveStats(path, event, rosterDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			rosterDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return rosterDTO;
	}

	public StandingsDTO retrieveStandings(String event, LocalDate asOfDate) {
		StandingsDTO standingsDTO = new StandingsDTO();
		try {
			String path = propertyService.getProperty_Path("xmlstats.fileStandings");
			standingsDTO = (StandingsDTO)fileClientService.retrieveStats(path, event, standingsDTO, asOfDate);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			standingsDTO.setStatusCode(StatusCodeDTO.ServerException);
		}
		return standingsDTO;
	}
}