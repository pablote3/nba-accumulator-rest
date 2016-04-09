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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;

@Service
public class FileClientService {
	@Autowired
	private PropertyService propertyBean;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	private StatsDTO retrieveStats(String stringPath, String event, StatsDTO statsDTO) {
		String stringFile = event + ".json";
		Path path = Paths.get(stringPath).resolve(stringFile);
		InputStreamReader baseJson = null;
		try {
			File file = path.toFile();
			InputStream inputStreamJson = new FileInputStream(file);
			baseJson = new InputStreamReader(inputStreamJson, StandardCharsets.UTF_8);
			statsDTO = mapper.readValue(baseJson, statsDTO.getClass());
			statsDTO.httpStatus = 200;
		} catch (FileNotFoundException fnf) {
			statsDTO.httpStatus = 404;
			fnf.printStackTrace();
		} catch (JsonParseException jpe) {
			statsDTO.httpStatus = 500;
			jpe.printStackTrace();
		} catch (JsonMappingException jme) {
			statsDTO.httpStatus = 500;
			jme.printStackTrace();
		} catch (IOException ioe) {
			statsDTO.httpStatus = 500;
			ioe.printStackTrace();
		}
		finally {
			try {
				if (baseJson != null)
					baseJson.close();
			} catch (IOException ioe) {
				statsDTO.httpStatus = 500;
				ioe.printStackTrace();
			}
		}
		return statsDTO;
	}

	public GameDTO retrieveBoxScore(String event) {
		String path = propertyBean.getProperty_Path("xmlstats.fileBoxScore");
		GameDTO dto = new GameDTO();
		return (GameDTO)retrieveStats(path, event, dto);
	}

	public RosterDTO retrieveRoster(String event) {
		String path = propertyBean.getProperty_Path("xmlstats.fileRoster");
		RosterDTO dto = new RosterDTO();
		return (RosterDTO)retrieveStats(path, event, dto);
	}

	public StandingsDTO retrieveStandings(String event) {
		String path = propertyBean.getProperty_Path("xmlstats.fileStandings");
		StandingsDTO dto = new StandingsDTO();
		return (StandingsDTO)retrieveStats(path, event, dto);
	}
}