package com.rossotti.basketball.client.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;

@RunWith(MockitoJUnitRunner.class)
public class RestClientServiceTest {
	@Mock
	PropertyService propertyService;

	@Mock
	RestClientService clientService;

	@InjectMocks
	private RestClientRetrieverService restClientService;

	@Test
	public void retrieveBoxScore_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = restClientService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_notFound() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.NotFound));
		GameDTO game = restClientService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_clientException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.ClientException));
		GameDTO game = restClientService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isClientException());
	}

	@Test
	public void retrieveBoxScore_found() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.Found));
		GameDTO game = restClientService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void retrieveRoster_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		RosterDTO roster = restClientService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isServerException());
	}

	@Test
	public void retrieveRoster_notFound() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		RosterDTO roster = restClientService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveRoster_clientException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		RosterDTO roster = restClientService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isClientException());
	}

	@Test
	public void retrieveRoster_found() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		RosterDTO roster = restClientService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isFound());
	}

	@Test
	public void retrieveStandings_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		StandingsDTO standings = restClientService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isServerException());
	}

	@Test
	public void retrieveStandings_notFound() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		StandingsDTO standings = restClientService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isNotFound());
	}

	@Test
	public void retrieveStandings_clientException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		StandingsDTO standings = restClientService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isClientException());
	}

	@Test
	public void retrieveStandings_found() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(clientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		StandingsDTO standings = restClientService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isFound());
	}
	private StatsDTO createMockGameDTO(StatsDTO stats, StatusCodeDTO statusCode) {
		stats.setStatusCode(statusCode);
		return stats;
	}
}