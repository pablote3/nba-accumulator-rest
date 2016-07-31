package com.rossotti.basketball.app.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameDay;
import com.rossotti.basketball.dao.model.GameDay.StatusCode;
import com.rossotti.basketball.dao.repository.GameRepository;

@Service
public class GameService {
	@Autowired
	private GameRepository gameRepo;

	public GameDay findByDate(LocalDate gameDate) {
		List<Game> games = gameRepo.findByDate(gameDate);
		GameDay gameDay = new GameDay();
		gameDay.setGameDate(gameDate);
		gameDay.setStatusCode(StatusCode.Found);
		gameDay.setGames(games);
		return gameDay;
	}

	public Game findByDateTeam(LocalDate gameDate, String teamKey) {
		return gameRepo.findByDateTeam(gameDate, teamKey);
	}

	public LocalDateTime findPreviousGameDateTime(LocalDate gameDate, String teamKey) {
		return gameRepo.findPreviousGameDateTimeByDateTeam(gameDate, teamKey);
	}

	public List<Game> findByDateTeamSeason(LocalDate gameDate, String teamKey) {
		return gameRepo.findByDateTeamSeason(gameDate, teamKey);
	}

	public Game updateGame(Game game) {
		return gameRepo.updateGame(game);
	}
}