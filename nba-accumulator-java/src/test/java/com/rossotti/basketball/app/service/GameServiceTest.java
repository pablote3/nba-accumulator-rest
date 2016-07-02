package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rossotti.basketball.app.exception.PropertyException;
import com.rossotti.basketball.app.provider.JsonProvider;
import com.rossotti.basketball.app.resource.ClientSource;
import com.rossotti.basketball.client.dto.BoxScorePlayerDTO;
import com.rossotti.basketball.client.dto.GameDTO;
import com.rossotti.basketball.client.dto.OfficialDTO;
import com.rossotti.basketball.client.dto.RosterDTO;
import com.rossotti.basketball.client.dto.StatusCodeDTO;
import com.rossotti.basketball.client.service.FileClientService;
import com.rossotti.basketball.client.service.RestClientService;
import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScorePlayer;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.Official;
import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.model.RosterPlayer;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.GameRepository;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
	@Mock
	private GameRepository gameRepo;

	@Mock
	private PropertyService propertyService;

	@Mock
	private FileClientService fileClientService;

//	@Mock
//	private RestClientService restClientService;

	@Mock
	private RosterPlayerService rosterPlayerService;

	@Mock
	private OfficialService officialService;

	@Mock
	private TeamService teamService;

	@InjectMocks
	private GameService gameService = new GameService();

	@Before
	public void setUp() {
		when(gameRepo.findByDate((LocalDate) anyObject()))
			.thenReturn(createMockGames())
			.thenReturn(new ArrayList<Game>());
		when(gameRepo.findByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGame_Scheduled(new LocalDateTime("2015-11-26T10:00")))
			.thenReturn(null);
		when(gameRepo.findPreviousGameDateTimeByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(new LocalDateTime("2015-11-24T10:00"))
			.thenReturn(null);
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGames())
			.thenReturn(new ArrayList<Game>());
		when(gameRepo.updateGame((Game) anyObject()))
			.thenReturn(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), StatusCodeDAO.Updated))
			.thenReturn(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), StatusCodeDAO.NotFound));
		when(propertyService.getProperty_ClientSource(anyString()))
			.thenReturn(ClientSource.File);
//			.thenReturn(null)
//			.thenThrow(new PropertyException("propertyName"));
		when(fileClientService.retrieveBoxScore(anyString()))
			.thenReturn(createMockGameDTO_Found());
//			.thenReturn(createMockGameDTO(StatusCodeDTO.NotFound))
//			.thenReturn(createMockGameDTO(StatusCodeDTO.ClientException));
//		when(restClientService.retrieveBoxScore(anyString()))
//			.thenReturn(createMockBoxScore(StatusCodeDTO.Found))
//			.thenReturn(createMockBoxScore(StatusCodeDTO.NotFound))
//			.thenReturn(createMockBoxScore(StatusCodeDTO.ClientException));
		when(rosterPlayerService.getBoxScorePlayers((BoxScorePlayerDTO[]) anyObject(), (LocalDate) anyObject(), anyString()))
			.thenReturn(createMockBoxScorePlayersHome_Found())
			.thenReturn(createMockBoxScorePlayersAway_Found());
		when(officialService.getGameOfficials((OfficialDTO[]) anyObject(), (LocalDate) anyObject()))
			.thenReturn(createMockGameOfficials_Found());
		when(teamService.findTeam(anyString(), (LocalDate) anyObject()))
			.thenReturn(createMockTeamHome_Found())
			.thenReturn(createMockTeamAway_Found());
	}

	//@Ignore
	@Test
	public void scoreGame() {
		Game game;
		//game updated
		game = gameService.scoreGame(createMockGame_Scheduled(new LocalDateTime("2015-11-24T10:00")));
		
		
		Assert.assertTrue(game.isUpdated());

//		//game not found
//		game = gameService.scoreGame(createMockGame_StatusCode(new LocalDateTime("2015-08-26T10:00"), null));
//		Assert.assertTrue(game.isNotFound());
	}

//	@Test
//	public void findByDate() {
//		List<Game> games;
//		//games found
//		games = gameService.findByDate(new LocalDate(2015, 11, 26));
//		Assert.assertEquals(2, games.size());
//
//		//games not found
//		games = gameService.findByDate(new LocalDate(2015, 8, 26));
//		Assert.assertEquals(0, games.size());
//	}
//	
//	@Test
//	public void findByDateTeam() {
//		Game game;
//		//game found
//		game = gameService.findByDateTeam(new LocalDate(2015, 11, 26), "sacramento-hornets");
//		Assert.assertEquals(new LocalDateTime("2015-11-26T10:00"), game.getGameDateTime());
//
//		//game not found
//		game = gameService.findByDateTeam(new LocalDate(2015, 8, 26), "sacramento-hornets");
//		Assert.assertNull(game);
//	}
//
//	@Test
//	public void findPreviousGameDateTime() {
//		LocalDateTime previousGameDate;
//		//previous game found
//		previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 11, 26), "sacramento-hornets");
//		Assert.assertEquals(new LocalDateTime("2015-11-24T10:00"), previousGameDate);
//
//		//previous game not found
//		previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 8, 26), "sacramento-hornets");
//		Assert.assertNull(previousGameDate);
//	}
//
//	@Test
//	public void findByDateTeamSeason() {
//		List<Game> games;
//		//two games found
//		games = gameService.findByDateTeamSeason(new LocalDate(2015, 11, 26), "sacramento-hornets");
//		Assert.assertEquals(2, games.size());
//
//		//no games found
//		games = gameService.findByDateTeamSeason(new LocalDate(2015, 8, 26), "sacramento-hornets");
//		Assert.assertEquals(0, games.size());
//	}
//
//	@Test
//	public void updateGame() {
//		Game game;
//		//game updated
//		game = gameService.updateGame(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), null));
//		Assert.assertTrue(game.isUpdated());
//
//		//game not found
//		game = gameService.updateGame(createMockGame_StatusCode(new LocalDateTime("2015-08-26T10:00"), null));
//		Assert.assertTrue(game.isNotFound());
//	}

	private List<Game> createMockGames() {
		List<Game> games = Arrays.asList(
			createMockGame_Completed(new LocalDateTime("2015-11-24T10:00")),
			createMockGame_Scheduled(new LocalDateTime("2015-11-26T10:00"))
		);
		return games;
	}

	private Game createMockGame_Scheduled(LocalDateTime asOfDate) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
		game.setStatus(GameStatus.Scheduled);
		Team teamHome = new Team();
		teamHome.setTeamKey("brooklyn-nets");
		BoxScore boxScoreHome = new BoxScore();
		boxScoreHome.setTeam(teamHome);
		game.addBoxScore(boxScoreHome);
		Team teamAway = new Team();
		teamAway.setTeamKey("detroit-pistons");
		BoxScore boxScoreAway = new BoxScore();
		boxScoreAway.setTeam(teamAway);
		game.addBoxScore(boxScoreAway);
		return game;
	}

	private Game createMockGame_Completed(LocalDateTime asOfDate) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
		game.setStatus(GameStatus.Completed);
		return game;
	}

	private Game createMockGame_StatusCode(LocalDateTime asOfDate, StatusCodeDAO status) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
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

	private List<BoxScorePlayer> createMockBoxScorePlayersAway_Found() {
		List<BoxScorePlayer> boxScorePlayers = new ArrayList<BoxScorePlayer>();
		boxScorePlayers.add(createMockBoxScorePlayer(1L, "Drummond", "Andre"));
		boxScorePlayers.add(createMockBoxScorePlayer(2L, "Morris", "Marcus"));
		boxScorePlayers.add(createMockBoxScorePlayer(3L, "Caldwell-Pope", "Kentavious"));
		boxScorePlayers.add(createMockBoxScorePlayer(4L, "Jackson", "Reggie"));
		return boxScorePlayers;
	}

	private List<BoxScorePlayer> createMockBoxScorePlayersHome_Found() {
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

	private Team createMockTeamHome_Found() {
		Team team = new Team();
		team.setId(1L);
		team.setTeamKey("brooklyn -nets");
		return team;
	}

	private Team createMockTeamAway_Found() {
		Team team = new Team();
		team.setId(1L);
		team.setTeamKey("detroit-pistons");
		return team;
	}
}