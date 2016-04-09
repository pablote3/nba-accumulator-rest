package com.rossotti.basketball.client;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.service.PropertyServiceBean;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;

@Service
public class RestClientBean {
	@Autowired
	private ClientBean clientBean;

	@Autowired
	private PropertyServiceBean propertyBean;

	private static ObjectMapper mapper = JsonProvider.buildObjectMapper();

	private StatsDTO retrieveStats(String url, StatsDTO statsDTO) {
		Response response = clientBean.getClient().target(url).request().get();
		
		if (response.getStatus() != 200) {
			response.readEntity(String.class);
		} else {
			try {
				statsDTO = mapper.readValue(response.readEntity(String.class), statsDTO.getClass());
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
		}
		statsDTO.httpStatus = response.getStatus();
		response.close();
		return statsDTO;
	}

	public GameDTO retrieveBoxScore(String event) {
		String url = propertyBean.getProperty_Http("xmlstats.urlBoxScore");
		String eventUrl = url + event + ".json";
		GameDTO dto = new GameDTO();
		return (GameDTO)retrieveStats(eventUrl, dto);
	}

	public RosterDTO retrieveRoster(String event) {
		String url = propertyBean.getProperty_Http("xmlstats.urlRoster");
		String eventUrl = url + event + ".json";
		RosterDTO dto = new RosterDTO();
		return (RosterDTO)retrieveStats(eventUrl, dto);
	}

	public StandingsDTO retrieveStandings(String event) {
		String url = propertyBean.getProperty_Http("xmlstats.urlStandings");
		String eventUrl = url + event + ".json";
		StandingsDTO dto = new StandingsDTO();
		return (StandingsDTO)retrieveStats(eventUrl, dto);
	}

	public ClientBean getClientBean() {
		return clientBean;
	}
	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}
}