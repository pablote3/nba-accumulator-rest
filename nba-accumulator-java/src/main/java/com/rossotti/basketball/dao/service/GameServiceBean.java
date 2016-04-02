package com.rossotti.basketball.dao.service;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.GameDAO;
import com.rossotti.basketball.dao.model.Game;

@Service
public class GameServiceBean {
	@Autowired
	private GameDAO gameDAO;

	public LocalDateTime findPreviousGameDateTime(LocalDate gameDate, String teamKey) {
		return gameDAO.findPreviousGameDateTimeByDateTeam(gameDate, teamKey);
	}

	public Game updateGame(Game game) {
		return gameDAO.updateGame(game);
	}
}