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

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.util.DateTimeUtil;

@Service
public class FileClientService {

	private static final ObjectMapper mapper = JsonProvider.buildObjectMapper();

	public StatsDTO retrieveStats(String stringPath, String event, StatsDTO statsDTO, LocalDate asOfDate) {
		String stringFile;
		if (statsDTO instanceof RosterDTO) {
			stringFile = event + "-" + DateTimeUtil.getStringDateNaked(asOfDate) + ".json";
		}
		else {
			stringFile = event + ".json";
		}
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
}