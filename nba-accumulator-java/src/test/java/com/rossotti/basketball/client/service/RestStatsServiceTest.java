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
public class RestStatsServiceTest {
	@Mock
	PropertyService propertyService;

	@Mock
	RestClientService restClientService;

	@InjectMocks
	private RestStatsService restStatsService;

	@Test
	public void retrieveBoxScore_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		GameDTO game = (GameDTO)restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_notFound() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.NotFound));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		GameDTO game = (GameDTO)restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_clientException() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.ClientException));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		GameDTO game = (GameDTO)restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isClientException());
	}

	@Test
	public void retrieveBoxScore_found() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.Found));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		GameDTO game = (GameDTO)restStatsService.retrieveBoxScore("20160311-houston-rockets-at-boston-celtics");
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void retrieveRoster_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		RosterDTO roster = (RosterDTO)restStatsService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isServerException());
	}

	@Test
	public void retrieveRoster_notFound() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		RosterDTO roster = (RosterDTO)restStatsService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveRoster_clientException() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		RosterDTO roster = (RosterDTO)restStatsService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isClientException());
	}

	@Test
	public void retrieveRoster_found() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		RosterDTO roster = (RosterDTO)restStatsService.retrieveRoster("toronto-raptors");
		Assert.assertTrue(roster.isFound());
	}

	@Test
	public void retrieveStandings_propertyException() {
		when(propertyService.getProperty_Http(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		StandingsDTO standings = (StandingsDTO)restStatsService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isServerException());
	}

	@Test
	public void retrieveStandings_notFound() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		StandingsDTO standings = (StandingsDTO)restStatsService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isNotFound());
	}

	@Test
	public void retrieveStandings_clientException() {
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		StandingsDTO standings = (StandingsDTO)restStatsService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isClientException());
	}

	@Test
	public void retrieveStandings_found() {
		when(propertyService.getProperty_Http(anyString()))
			.thenReturn("https://");
		when(restClientService.retrieveStats(anyString(), (StatsDTO) anyObject()))
			.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		StandingsDTO standings = (StandingsDTO)restStatsService.retrieveStandings("20141108");
		Assert.assertTrue(standings.isFound());
	}
	private StatsDTO createMockGameDTO(StatsDTO stats, StatusCodeDTO statusCode) {
		stats.setStatusCode(statusCode);
		return stats;
	}
}