package com.rossotti.basketball.app.business;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.StandingsService;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StandingDTO;
import com.rossotti.basketball.client.dto.StandingsDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestStatsService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppStandings;
import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StandingRecord;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;

@RunWith(MockitoJUnitRunner.class)
public class StandingsBusinessTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileClientService fileClientService;

	@Mock
	private RestStatsService restStatsService;

	@Mock
	private StandingsService standingsService;

	@InjectMocks
	private StandingsBusiness standingsBusiness;

	@Test
	public void propertyService_propertyException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppServerError());
	}

	@Test
	public void propertyService_propertyNull() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(null);
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppServerError());
	}

	@Test
	public void fileClientService_standingsNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.NotFound));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void fileClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.ClientException));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void fileClientService_emptyList() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.Found));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void restClientService_standingsNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.NotFound));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void restClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.ClientException));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void restClientService_emptyList() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restStatsService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.Found));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@Test
	public void standingsService_noSuchEntity_team() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createMockStandingsDTO_StatusCode(StatusCodeDTO.Found));
		when(standingsService.getStandings((StandingsDTO) anyObject()))
			.thenThrow(new NoSuchEntityException(Team.class));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppClientError());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void standingsService_createStanding_exists() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createStandingsDTO_Found());
		when(standingsService.getStandings((StandingsDTO) anyObject()))
			.thenReturn(createMockStandings());
		when(standingsService.buildStandingsMap((List<Standing>) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockStandingsMap());
		when(standingsService.buildHeadToHeadMap(anyString(), (LocalDate) anyObject(), (Map<String, StandingRecord>) anyObject()))
			.thenReturn(createMockHeadToHeadMap());
		when(standingsService.calculateStrengthOfSchedule(anyString(), (LocalDate) anyObject(), (Map<String, StandingRecord>) anyObject(), (Map<String, StandingRecord>) anyObject()))
			.thenReturn(new StandingRecord(5, 10, 20, 40));
		when(standingsService.createStanding((Standing) anyObject()))
			.thenReturn(createMockStanding_StatusCode(StatusCodeDAO.Found));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppServerError());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void standingsService_createStanding_created() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveStandings(anyString()))
			.thenReturn(createStandingsDTO_Found());
		when(standingsService.getStandings((StandingsDTO) anyObject()))
			.thenReturn(createMockStandings());
		when(standingsService.buildStandingsMap((List<Standing>) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockStandingsMap());
		when(standingsService.buildHeadToHeadMap(anyString(), (LocalDate) anyObject(), (Map<String, StandingRecord>) anyObject()))
			.thenReturn(createMockHeadToHeadMap());
		when(standingsService.calculateStrengthOfSchedule(anyString(), (LocalDate) anyObject(), (Map<String, StandingRecord>) anyObject(), (Map<String, StandingRecord>) anyObject()))
			.thenReturn(new StandingRecord(5, 10, 20, 40));
		when(standingsService.createStanding((Standing) anyObject()))
			.thenReturn(createMockStanding_StatusCode(StatusCodeDAO.Created));
		AppStandings standings = standingsBusiness.rankStandings("2014-10-28");
		Assert.assertTrue(standings.isAppCompleted());
	}

	private StandingsDTO createStandingsDTO_Found() {
		StandingsDTO standings = null;
		try {
			ObjectMapper mapper = JsonProvider.buildObjectMapper();
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/standingsClient.json");
			standings = mapper.readValue(baseJson, RosterDTO.class);
			standings.setStatusCode(StatusCodeDTO.Found);
		}
		catch (IOException e) {
			standings = new RosterDTO();
			standings.setStatusCode(StatusCodeDTO.ClientException);
		}
		return standings;
	}

	private StandingsDTO createMockStandingsDTO_StatusCode(StatusCodeDTO statusCodeDTO) {
		StandingsDTO standings = new StandingsDTO();
		standings.setStatusCode(statusCodeDTO);
		standings.standing = new StandingDTO[0];
		return standings;
	}

	private List<Standing> createMockStandings() {
		List<Standing> standings = new ArrayList<Standing>();
		standings.add(createMockStanding());
		return standings;
	}

	private Standing createMockStanding() {
		Standing standing = new Standing();
		standing.setTeam(createMockTeam());
		standing.setStandingDate(new LocalDate("2015-12-27"));
		return standing;
	}

	private Team createMockTeam() {
		Team team = new Team();
		team.setTeamKey("cleveland-cavaliers");
		return team;
	}
	
	private Map<String, StandingRecord> createMockStandingsMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<String, StandingRecord>();
		standingsMap.put("cleveland-cavaliers", new StandingRecord(5, 10, 0, 0));
		return standingsMap;
	}

	private Map<String, StandingRecord> createMockHeadToHeadMap() {
		Map<String, StandingRecord> standingsMap = new HashMap<String, StandingRecord>();
		standingsMap.put("cleveland-cavaliers", new StandingRecord(5, 10, 0, 0));
		return standingsMap;
	}

	private Standing createMockStanding_StatusCode(StatusCodeDAO status) {
		Standing standing = new Standing();
		standing.setStatusCode(status);
		return standing;
	}
}