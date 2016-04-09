package com.rossotti.basketball.app.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.repository.GameRepository;

@Service
public class GameService {
	@Autowired
	private GameRepository gameRepo;

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