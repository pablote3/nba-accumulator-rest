package com.rossotti.basketball.app.business;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.RosterPlayerDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppGame;
import com.rossotti.basketball.dao.model.AppRoster;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Location;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
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
			.thenReturn(ClientSource.File);
//			.thenReturn(ClientSource.File)
//			.thenReturn(ClientSource.Api)
//			.thenReturn(ClientSource.Api)
//			.thenReturn(ClientSource.File)
//			.thenReturn(ClientSource.Api);
		when(fileClientService.retrieveRoster(anyString()))
			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.NotFound));
//			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.ClientException))
//			.thenReturn(new RosterDTO())
//			.thenReturn(createMockRosterDTO_Found());
//		when(restClientService.retrieveRoster(anyString()))
//			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.NotFound))
//			.thenReturn(createMockRosterDTO_StatusCode(StatusCodeDTO.ClientException))
//			.thenReturn(new RosterDTO())
//			.thenReturn(createMockRosterDTO_Found());
//		when(rosterPlayerService.getRosterPlayers((RosterPlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
//			.thenThrow(new NoSuchEntityException(RosterPlayer.class))
//			.thenReturn(new ArrayList<RosterPlayer>())
//			.thenReturn(createMockRosterPlayers());
//		when(rosterPlayerService.findByDatePlayerNameTeam((LocalDate) anyObject(), anyString(), anyString(), anyString()))
//			.thenReturn(new RosterPlayer(StatusCodeDAO.NotFound))
//			.thenReturn(createMockRosterPlayer("Jackson", "Reggie"));
//		when(rosterPlayerService.findLatestByPlayerNameBirthdateSeason((LocalDate) anyObject(), anyString(), anyString(), (LocalDate) anyObject()))
//			.thenReturn(new RosterPlayer(StatusCodeDAO.NotFound))
//			.thenReturn(createMockRosterPlayer("Jackson", "Reggie"));
//		when(playerService.findByPlayerNameBirthdate(anyString(), anyString(), (LocalDate) anyObject()))
//			.thenReturn(new Player())
//			.thenReturn(createMockPlayer("Bob", "Bowser"));
//		when(rosterPlayerService.findRosterPlayers((LocalDate) anyObject(), anyString()))
//			.thenReturn(new ArrayList<RosterPlayer>())
//			.thenReturn(createMockRosterPlayers());
//		when(playerService.createPlayer((Player)anyObject()))
//			.thenReturn(new Player(StatusCodeDAO.Created));
//		when(rosterPlayerService.createRosterPlayer((RosterPlayer)anyObject()))
//			.thenReturn(new RosterPlayer(StatusCodeDAO.Created));
//		when(rosterPlayerService.updateRosterPlayer((RosterPlayer)anyObject()))
//			.thenReturn(new RosterPlayer(StatusCodeDAO.Updated));
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

//		//fileClientService - roster dto not found
		roster = rosterPlayerBusiness.loadRoster("2014-10-28", "detroit-pistons");
		Assert.assertTrue(roster.isAppClientError());
//
//		//fileClientService - client exception
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//restClientService - game dto not found
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//restClientService - client exception
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//rosterPlayerService - no such entity exception
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//officialService - no such entity exception
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//teamService - no such entity exception
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppClientError());
//
//		//game not found
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppServerError());
//
//		//game updated - client source file
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppCompleted());
//
//		//game updated - client source api
//		game = gameBusiness.scoreGame(createMockGame_Scheduled());
//		Assert.assertTrue(game.isAppCompleted());
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
		return roster;
	}

	private List<RosterPlayer> createMockRosterPlayers() {
		List<RosterPlayer> rosterPlayers = new ArrayList<RosterPlayer>();
		rosterPlayers.add(createMockRosterPlayer("Drummond", "Andre"));
		rosterPlayers.add(createMockRosterPlayer("Morris", "Marcus"));
		rosterPlayers.add(createMockRosterPlayer("Caldwell-Pope", "Kentavious"));
		rosterPlayers.add(createMockRosterPlayer("Jackson", "Reggie"));
		return rosterPlayers;
	}

	private RosterPlayer createMockRosterPlayer(String lastName, String firstName) {
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(createMockPlayer(lastName, firstName));
		return rosterPlayer;
	}

	private Player createMockPlayer(String lastName, String firstName) {
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		return player;
	}

	private Team createMockTeam() {
		Team team = new Team();
		team.setId(1L);
		team.setTeamKey("brooklyn-nets");
		return team;
	}
}