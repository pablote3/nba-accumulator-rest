package com.rossotti.basketball.app.business;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.app.service.GameService;
import com.rossotti.basketball.app.service.OfficialService;
import com.rossotti.basketball.app.service.PlayerService;
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.RosterPlayerService;
import com.rossotti.basketball.app.service.TeamService;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppRoster;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;

@RunWith(MockitoJUnitRunner.class)
public class RosterPlayerBusinessTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileClientService fileClientService;

	@Mock
	private RestClientService restClientService;

	@Mock
	private RosterPlayerService rosterPlayerService;

	@Mock
	private OfficialService officialService;

	@Mock
	private TeamService teamService;

	@Mock
	private PlayerService playerService;

	@Mock
	private GameService gameService;

	@InjectMocks
	private RosterPlayerBusiness rosterPlayerBusiness = new RosterPlayerBusiness();

	@Before
	public void setUp() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenThrow(new PropertyException("propertyName"))
			.thenReturn(null)
			.thenReturn(ClientSource.File)
			.thenReturn(ClientSource.File)
			.thenReturn(ClientSource.File)
			.thenReturn(ClientSource.Api)
			.thenReturn(ClientSource.Api)
			.thenReturn(ClientSource.Api)
			.thenReturn(ClientSource.File)
			.thenReturn(ClientSource.Api);
		when(fileClientService.retrieveRoster(anyString()))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.NotFound))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.ClientException))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.Found))
			.thenReturn(createMockRosterDTO_Found());
		when(restClientService.retrieveRoster(anyString()))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.NotFound))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.ClientException))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.Found))
			.thenReturn(createMockRosterDTO_Found());
		when(rosterPlayerService.getRosterPlayers((RosterPlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenThrow(new NoSuchEntityException(Team.class))
			.thenReturn(new ArrayList<RosterPlayer>())
			.thenReturn(createMockRosterPlayers());
		when(rosterPlayerService.findByDatePlayerNameTeam((LocalDate) anyObject(), anyString(), anyString(), anyString()))
			.thenReturn(new RosterPlayer(StatusCodeDAO.NotFound))
			.thenReturn(createMockRosterPlayer("Jackson", "Reggie"));
		when(rosterPlayerService.findLatestByPlayerNameBirthdateSeason((LocalDate) anyObject(), anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(new RosterPlayer(StatusCodeDAO.NotFound));
//			.thenReturn(createMockRosterPlayer("Jackson", "Reggie"));
		when(playerService.findByPlayerNameBirthdate(anyString(), anyString(), (LocalDate) anyObject()))
			.thenReturn(new Player(StatusCodeDAO.NotFound))
			.thenReturn(createMockPlayer("Bob", "Bowser"));
		when(playerService.createPlayer((Player) anyObject()))
			.thenReturn(createMockPlayer("Jones", "Basketball"));
		when(rosterPlayerService.findRosterPlayers((LocalDate) anyObject(), anyString()))
			.thenReturn(new ArrayList<RosterPlayer>())
			.thenReturn(createMockRosterPlayers());
	}

	@Test
	public void loadRoster() {
		AppRoster roster;

		//propertyService - property exception
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppServerError());

		//propertyService - property null
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppServerError());

		//fileClientService - roster dto not found
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//fileClientService - client exception
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//fileClientService - empty client list
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//restClientService - roster dto not found
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//restClientService - client exception
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//restClientService - empty client list
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());
		
		//rosterPlayerService - getRosterPlayers - no such entity exception - team
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());

		//rosterPlayerService - getRosterPlayers - empty list
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppServerError());

		//rosterPlayerService - findRosterPlayers - empty list
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppServerError());

		//roster updated
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppCompleted());
	}

	private RosterDTO createMockRosterDTO_Found() {
		RosterDTO roster = null;
		try {
			ObjectMapper mapper = JsonProvider.buildObjectMapper();
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/rosterClient.json");
			roster = mapper.readValue(baseJson, RosterDTO.class);
			roster.setStatusCode(StatusCodeDTO.Found);
		}
		catch (IOException e) {
			roster = new RosterDTO();
			roster.setStatusCode(StatusCodeDTO.ClientException);
		}
		return roster;
	}

	private RosterDTO createMockRosterDTO_StatusCode(StatusCodeDTO statusCodeDTO) {
		RosterDTO roster = new RosterDTO();
		roster.setStatusCode(statusCodeDTO);
		roster.players = new RosterPlayerDTO[0];
		return roster;
	}

	private List<RosterPlayer> createMockRosterPlayers() {
		List<RosterPlayer> rosterPlayers = new ArrayList<RosterPlayer>();
		rosterPlayers.add(createMockRosterPlayer("Morris", "Marcus"));
		rosterPlayers.add(createMockRosterPlayer("Drummond", "Andre"));
		return rosterPlayers;
	}

	private RosterPlayer createMockRosterPlayer(String lastName, String firstName) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setFromDate(new LocalDate("2015-10-27"));
		rosterPlayer.setToDate(new LocalDate("2015-12-27"));
		rosterPlayer.setTeam(createMockTeam());
		rosterPlayer.setStatusCode(StatusCodeDAO.Found);
		rosterPlayer.setPlayer(createMockPlayer(lastName, firstName));
		return rosterPlayer;
	}
	
	private Player createMockPlayer(String lastName, String firstName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		player.setBirthdate(new LocalDate("1995-12-27"));
		return player;
	}

	private Team createMockTeam() {
		Team team = new Team();
		team.setTeamKey("brooklyn-nets");
		return team;
	}
}