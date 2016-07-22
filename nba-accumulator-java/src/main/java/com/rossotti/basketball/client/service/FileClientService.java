package com.rossotti.basketball.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@Service
public class FileClientService {
	@Autowired
	private PropertyService propertyService;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();
	private final Logger logger = LoggerFactory.getLogger(FileClientService.class);

	public StatsDTO retrieveStats(String stringPath, String event, StatsDTO statsDTO) {
		String stringFile = event + ".json";
		Path path = Paths.get(stringPath).resolve(stringFile);
		InputStreamReader baseJson = null;
		try {
			File file = path.toFile();
			InputStream inputStreamJson = new FileInputStream(file);
			baseJson = new InputStreamReader(inputStreamJson, StandardCharsets.UTF_8);
			statsDTO = mapper.readValue(baseJson, statsDTO.getClass());
			statsDTO.setStatusCode(StatusCodeDTO.Found);
		} catch (FileNotFoundException fnf) {
			statsDTO.setStatusCode(StatusCodeDTO.NotFound);
			fnf.printStackTrace();
		} catch (JsonParseException jpe) {
			statsDTO.setStatusCode(StatusCodeDTO.ClientException);
			jpe.printStackTrace();
		} catch (JsonMappingException jme) {
			statsDTO.setStatusCode(StatusCodeDTO.ClientException);
			jme.printStackTrace();
		} catch (IOException ioe) {
			statsDTO.setStatusCode(StatusCodeDTO.ClientException);
			ioe.printStackTrace();
		}
		finally {
			try {
				if (baseJson != null)
					baseJson.close();
			} catch (IOException ioe) {
				statsDTO.setStatusCode(StatusCodeDTO.ClientException);
				ioe.printStackTrace();
			}
		}
		return statsDTO;
	}

	public GameDTO retrieveBoxScore(String event) {
		GameDTO dto = new GameDTO();
		try {
			String path = propertyService.getProperty_Path("xmlstats.fileBoxScore");
			dto = (GameDTO)retrieveStats(path, event, dto);
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
			String path = propertyService.getProperty_Path("xmlstats.fileRoster");
			dto = (RosterDTO)retrieveStats(path, event, dto);
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
			String path = propertyService.getProperty_Path("xmlstats.fileStandings");
			dto = (StandingsDTO)retrieveStats(path, event, dto);
		}
		catch (PropertyException pe) {
			logger.info("property exception = " + pe);
			dto.setStatusCode(StatusCodeDTO.ServerException);
		}
		return dto;
	}
}