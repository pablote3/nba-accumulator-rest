package com.rossotti.basketball.app.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
import com.rossotti.basketball.dao.repository.GameRepository;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {
	@Mock
	private GameRepository gameRepo;

	@InjectMocks
	private GameService gameService = new GameService();

	@Before
	public void setUp() throws Exception {
		when(gameRepo.findPreviousGameDateTimeByDateTeam((LocalDate) anyObject(), anyString())).thenReturn(new LocalDateTime("2015-11-24T10:00"));
		when(gameRepo.findByDateTeamSeason((LocalDate) anyObject(), anyString())).thenReturn(createMockGames());
	}

	@Test
	public void findPreviousGameDateTime() {
		LocalDateTime previousGameDate = gameService.findPreviousGameDateTime(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(new LocalDateTime("2015-11-24T10:00"), previousGameDate);
	}

	@Test
	public void findByDateTeamSeason() {
		List<Game> games = gameService.findByDateTeamSeason(new LocalDate(2015, 11, 26), "sacramento-hornets");
		Assert.assertEquals(2, games.size());
	}

	private List<Game> createMockGames() {
		List<Game> games = Arrays.asList(
				createMockGame(new LocalDateTime("2015-11-24T10:00"), GameStatus.Completed),
				createMockGame(new LocalDateTime("2015-11-26T10:00"), GameStatus.Scheduled)
		);
		return games;
	}

	private Game createMockGame(LocalDateTime asOfDate, GameStatus status) {
		Game game = new Game();
		game.setGameDateTime(asOfDate);
		game.setStatus(status);
		return game;
	}
}