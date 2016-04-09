package com.rossotti.basketball.app.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.repository.GameDAO;

@Service
public class GameService {
	@Autowired
	private GameDAO gameDAO;

	public LocalDateTime findPreviousGameDateTime(LocalDate gameDate, String teamKey) {
		return gameDAO.findPreviousGameDateTimeByDateTeam(gameDate, teamKey);
	}

	public List<Game> findByDateTeamSeason(LocalDate gameDate, String teamKey) {
		return gameDAO.findByDateTeamSeason(gameDate, teamKey);
	}

	public Game updateGame(Game game) {
		return gameDAO.updateGame(game);
	}
}