package com.rossotti.basketball.client.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import org.joda.time.LocalDate;
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

@SuppressWarnings("CanBeFinal")
@RunWith(MockitoJUnitRunner.class)
public class FileStatsServiceTest {
	@Mock
	PropertyService propertyService;

	@Mock
	FileClientService fileClientService;

	@InjectMocks
	private FileStatsService fileStatsService;

	@Test
	public void retrieveBoxScore_propertyException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenThrow(new PropertyException("propertyName"));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets", new LocalDate("20150415"));
		Assert.assertTrue(game.isServerException());
	}

	@Test
	public void retrieveBoxScore_notFound() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.NotFound));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets", new LocalDate("20150415"));
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void retrieveBoxScore_clientException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.ClientException));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets", new LocalDate("20150415"));
		Assert.assertTrue(game.isClientException());
	}

	@Test
	public void retrieveBoxScore_found() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new GameDTO(), StatusCodeDTO.Found));
		GameDTO game = fileStatsService.retrieveBoxScore("20150415-utah-jazz-at-houston-rockets", new LocalDate("20150415"));
		Assert.assertTrue(game.isFound());
	}

	@Test
	public void retrieveRoster_propertyException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenThrow(new PropertyException("propertyName"));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", new LocalDate("20150415"));
		Assert.assertTrue(roster.isServerException());
	}

	@Test
	public void retrieveRoster_notFound() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", new LocalDate("20150415"));
		Assert.assertTrue(roster.isNotFound());
	}

	@Test
	public void retrieveRoster_clientException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", new LocalDate("20150415"));
		Assert.assertTrue(roster.isClientException());
	}

	@Test
	public void retrieveRoster_found() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		RosterDTO roster = fileStatsService.retrieveRoster("toronto-raptors", new LocalDate("20150415"));
		Assert.assertTrue(roster.isFound());
	}

	@Test
	public void retrieveStandings_propertyException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenThrow(new PropertyException("propertyName"));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028", new LocalDate("20150415"));
		Assert.assertTrue(standings.isServerException());
	}

	@Test
	public void retrieveStandings_notFound() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.NotFound));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028", new LocalDate("20150415"));
		Assert.assertTrue(standings.isNotFound());
	}

	@Test
	public void retrieveStandings_clientException() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.ClientException));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028", new LocalDate("20150415"));
		Assert.assertTrue(standings.isClientException());
	}

	@Test
	public void retrieveStandings_found() {
		when(propertyService.getProperty_Path(anyString()))
				.thenReturn("/home/pablote/");
		when(fileClientService.retrieveStats(anyString(), anyString(), (StatsDTO) anyObject(), (LocalDate) anyObject()))
				.thenReturn(createMockGameDTO(new RosterDTO(), StatusCodeDTO.Found));
		StandingsDTO standings = fileStatsService.retrieveStandings("20141028", new LocalDate("20150415"));
		Assert.assertTrue(standings.isFound());
	}

	private StatsDTO createMockGameDTO(StatsDTO stats, StatusCodeDTO statusCode) {
		stats.setStatusCode(statusCode);
		return stats;
	}
}