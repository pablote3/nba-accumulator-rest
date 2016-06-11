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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.StatusCode;
import com.rossotti.basketball.dao.repository.GameRepository;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
	@Mock
	private GameRepository gameRepo;

	@InjectMocks
	private GameService gameService = new GameService();

	@Before
	public void setUp() {
		when(gameRepo.findByDate((LocalDate) anyObject()))
			.thenReturn(createMockGames())
			.thenReturn(new ArrayList<Game>());
		when(gameRepo.findByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGame_GameStatus(new LocalDateTime("2015-11-26T10:00"), GameStatus.Scheduled))
			.thenReturn(null);
		when(gameRepo.findPreviousGameDateTimeByDateTeam((LocalDate) anyObject(), anyString()))
			.thenReturn(new LocalDateTime("2015-11-24T10:00"))
			.thenReturn(null);
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString()))
			.thenReturn(createMockGames())
			.thenReturn(new ArrayList<Game>());
		when(gameRepo.updateGame((Game) anyObject()))
			.thenReturn(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), StatusCode.Updated))
			.thenReturn(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), StatusCode.NotFound));
	}

	@Test
	public void findByDate() {
		List<Game> games;
		//games found
		games = gameService.findByDate(new LocalDate(2015, 11, 26));
		Assert.assertEquals(2, games.size());

		//games not found
		games = gameService.findByDate(new LocalDate(2015, 8, 26));
		Assert.assertEquals(0, games.size());
	}
	
	@Test
	public void findByDateTeam() {
		Game game;
		//game found
		game = gameService.findByDateTeam(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new LocalDateTime("2015-11-26T10:00"), game.getGameDateTime());

		//game not found
		game = gameService.findByDateTeam(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertNull(game);
	}

	@Test
	public void findPreviousGameDateTime() {
		LocalDateTime previousGameDate;
		//previous game found
		previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new LocalDateTime("2015-11-24T10:00"), previousGameDate);

		//previous game not found
		previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertNull(previousGameDate);
	}

	@Test
	public void findByDateTeamSeason() {
		List<Game> games;
		//two games found
		games = gameService.findByDateTeamSeason(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, games.size());

		//no games found
		games = gameService.findByDateTeamSeason(new LocalDate(2015, 8, 26), "sacramento-hornets");
		Assert.assertEquals(0, games.size());
	}

	@Test
	public void updateGame() {
		Game game;
		//game updated
		game = gameService.updateGame(createMockGame_StatusCode(new LocalDateTime("2015-11-24T10:00"), null));
		Assert.assertTrue(game.isUpdated());

		//game not found
		game = gameService.updateGame(createMockGame_StatusCode(new LocalDateTime("2015-08-26T10:00"), null));
		Assert.assertTrue(game.isNotFound());
	}

	private List<Game> createMockGames() {
		List<Game> games = Arrays.asList(
				createMockGame_GameStatus(new LocalDateTime("2015-11-24T10:00"), GameStatus.Completed),
				createMockGame_GameStatus(new LocalDateTime("2015-11-26T10:00"), GameStatus.Scheduled)
		);
		return games;
	}

	private Game createMockGame_GameStatus(LocalDateTime asOfDate, GameStatus status) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
		game.setStatus(status);
		return game;
	}

	private Game createMockGame_StatusCode(LocalDateTime asOfDate, StatusCode status) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
		game.setStatusCode(status);
		return game;
	}
}