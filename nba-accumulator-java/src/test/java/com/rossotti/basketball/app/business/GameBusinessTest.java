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
import com.rossotti.basketball.app.service.PropertyService;
import com.rossotti.basketball.app.service.RosterPlayerService;
import com.rossotti.basketball.app.service.TeamService;
import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientRetrieverService;
import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.AppGame;
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
public class GameBusinessTest {
	@Mock
	private PropertyService propertyService;

	@Mock
	private FileClientService fileClientService;

	@Mock
	private RestClientRetrieverService restClientService;

	@Mock
	private RosterPlayerService rosterPlayerService;

	@Mock
	private OfficialService officialService;

	@Mock
	private TeamService teamService;

	@Mock
	private GameService gameService;

	@InjectMocks
	private GameBusiness gameBusiness;

	@Test
	public void propertyService_propertyException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenThrow(new PropertyException("propertyName"));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppServerError());
	}

	@Test
	public void propertyService_propertyNull() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(null);
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppServerError());
	}

	@Test
	public void fileClientService_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCodeDTO.NotFound));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void fileClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCodeDTO.ClientException));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void restClientService_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCodeDTO.NotFound));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void restClientService_clientException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_StatusCode(StatusCodeDTO.ClientException));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void rosterPlayerService_getBoxScorePlayers_noSuchEntityException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenThrow(new NoSuchEntityException(RosterPlayer.class));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void officialService_getGameOfficials_noSuchEntityException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialService.getGameOfficials((OfficialDTO[]) anyObject(), (LocalDate) anyObject()))
			.thenThrow(new NoSuchEntityException(Official.class));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}

	@Test
	public void teamService_findTeam_noSuchEntityException() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialService.getGameOfficials((OfficialDTO[]) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamService.findTeam(anyString(), (LocalDate) anyObject()))
			.thenThrow(new NoSuchEntityException(Team.class));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppClientError());
	}
	
	@Test
	public void gameService_updateGame_gameNotFound() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialService.getGameOfficials((OfficialDTO[]) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamService.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam_Found());
		when(gameService.updateGame((Game)anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.NotFound));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppServerError());
	}

	@Test
	public void gameService_updateGame_complete() {
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.Api);
		when(restClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayers_Found());
		when(officialService.getGameOfficials((OfficialDTO[]) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamService.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeam_Found());
		when(gameService.updateGame((Game)anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Updated));
		AppGame game = gameBusiness.scoreGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isAppCompleted());
	}

	private Game createMockGame_Scheduled() {
		Game game = new Game();
		game.setGameDateTime(new LocalDateTime("2015-11-26T10:00"));
		game.setStatus(GameStatus.Scheduled);
		Team teamHome = new Team();
		teamHome.setTeamKey("brooklyn-nets");
		BoxScore boxScoreHome = new BoxScore();
		boxScoreHome.setLocation(Location.Home);
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		Team teamAway = new Team();
		teamAway.setTeamKey("detroit-pistons");
		BoxScore boxScoreAway = new BoxScore();
		boxScoreAway.setLocation(Location.Away);
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}

	private Game createMockGame_StatusCode(StatusCodeDAO status) {
		Game game = new Game();
		game.setGameDateTime(new LocalDateTime("2015-11-24T10:00"));
		game.setStatusCode(status);
		return game;
	}

	private GameDTO createMockGameDTO_Found() {
		GameDTO game = null;
		try {
			ObjectMapper mapper = JsonProvider.buildObjectMapper();
			InputStream baseJson = this.getClass().getClassLoader().getResourceAsStream("mockClient/gameClient.json");
			game = mapper.readValue(baseJson, GameDTO.class);
			game.setStatusCode(StatusCodeDTO.Found);
		}
		catch (IOException e) {
			game = new GameDTO();
			game.setStatusCode(StatusCodeDTO.ClientException);
		}
		return game;
	}

	private GameDTO createMockGameDTO_StatusCode(StatusCodeDTO statusCodeDTO) {
		GameDTO game = new GameDTO();
		game.setStatusCode(statusCodeDTO);
		return game;
	}

	private List<BoxScorePlayer> createMockBoxScorePlayers_Found() {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
		boxScorePlayers.add(createMockBoxScorePlayer(1L, "Bogdanović", "Bojan"));
		boxScorePlayers.add(createMockBoxScorePlayer(2L, "Larkin", "DeShane"));
		boxScorePlayers.add(createMockBoxScorePlayer(3L, "Robinson", "Thomas"));
		boxScorePlayers.add(createMockBoxScorePlayer(4L, "Karasev", "Sergey"));
		return boxScorePlayers;
	}

	private BoxScorePlayer createMockBoxScorePlayer(Long id, String lastName, String firstName) {
		BoxScorePlayer boxScorePlayer = new BoxScorePlayer();
		boxScorePlayer.setId(id);
		Player player = new Player();
		player.setLastName(lastName);
		player.setFirstName(firstName);
		RosterPlayer rosterPlayer = new RosterPlayer();
		rosterPlayer.setPlayer(player);
		boxScorePlayer.setRosterPlayer(rosterPlayer);
		return boxScorePlayer;
	}

	private List<GameOfficial> createMockGameOfficials_Found() {
		List<GameOfficial> gameOfficials = new ArrayList<GameOfficial>();
		gameOfficials.add(createMockGameOfficial(1L, "Zarba", "Zach"));
		gameOfficials.add(createMockGameOfficial(2L, "Forte", "Brian"));
		gameOfficials.add(createMockGameOfficial(3L, "Roe", "Eli"));
		return gameOfficials;
	}

	private GameOfficial createMockGameOfficial(Long id, String lastName, String firstName) {
		GameOfficial gameOfficial = new GameOfficial();
		Official official = new Official();
		official.setId(id);
		official.setLastName(lastName);
		official.setFirstName(firstName);
		gameOfficial.setOfficial(official);
		return gameOfficial;
	}

	private Team createMockTeam_Found() {
		Team team = new Team();
		team.setId(1L);
		team.setTeamKey("brooklyn-nets");
		return team;
	}
}