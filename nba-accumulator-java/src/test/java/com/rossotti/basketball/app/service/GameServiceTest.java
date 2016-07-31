package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.BoxScore.Location;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameDay;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.GameRepository;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
	@Mock
	private GameRepository gameRepo;

	@InjectMocks
	private GameService gameService;

	@Test
	public void findByDate_notFound() {
		when(gameRepo.findByDate((LocalDate) anyObject()))
			.thenReturn(new ArrayList<Game>());
		GameDay games = gameService.findByDate(new LocalDate(2015, 8, 26));
		Assert.assertEquals(0, games.getGames().size());
	}

	@Test
	public void findByDate_found() {
		when(gameRepo.findByDate((LocalDate) anyObject()))
			.thenReturn(createMockGames());
		GameDay games = gameService.findByDate(new LocalDate(2015, 11, 26));
		Assert.assertEquals(2, games.getGames().size());
	}

	@Test
	public void findByDateTeam_notFound() {
		when(gameRepo.findByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(null);
		Game game = gameService.findByDateTeam(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertNull(game);
	}

	@Test
	public void findByDateTeam_found() {
		when(gameRepo.findByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGame_Scheduled());
		Game game = gameService.findByDateTeam(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new LocalDateTime("2015-11-26T10:00"), game.getGameDateTime());
	}

	@Test
	public void findPreviousGameDateTime_notFound() {
		when(gameRepo.findPreviousGameDateTimeByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(null);
		LocalDateTime previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertNull(previousGameDate);
	}

	@Test
	public void findPreviousGameDateTime_found() {
		when(gameRepo.findPreviousGameDateTimeByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(new LocalDateTime("2015-11-24T10:00"));
		LocalDateTime previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new LocalDateTime("2015-11-24T10:00"), previousGameDate);
	}

	@Test
	public void findByDateTeamSeason_notFound() {
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString()))
			.thenReturn(new ArrayList<Game>());
		List<Game> games = gameService.findByDateTeamSeason(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void findByDateTeamSeason_found() {
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGames());
		List<Game> games = gameService.findByDateTeamSeason(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, games.size());
	}

	@Test
	public void updateGame_notFound() {
		when(gameRepo.updateGame((Game) anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.NotFound));
		Game game = gameService.updateGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isNotFound());
	}

	@Test
	public void updateGame_updated() {
		when(gameRepo.updateGame((Game) anyObject()))
			.thenReturn(createMockGame_StatusCode(StatusCodeDAO.Updated));
		Game game = gameService.updateGame(createMockGame_Scheduled());
		Assert.assertTrue(game.isUpdated());
	}

	private List<Game> createMockGames() {
		List<Game> games = Arrays.asList(
			createMockGame_Completed(),
			createMockGame_Scheduled()
		);
		return games;
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

	private Game createMockGame_Completed() {
		Game game = new Game();
		game.setGameDateTime(new LocalDateTime("2015-11-24T10:00"));
		game.setStatus(GameStatus.Completed);
		return game;
	}

	private Game createMockGame_StatusCode(StatusCodeDAO status) {
		Game game = new Game();
		game.setGameDateTime(new LocalDateTime("2015-11-24T10:00"));
		game.setStatusCode(status);
		return game;
	}
}