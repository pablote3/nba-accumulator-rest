package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.rossotti.basketball.model.Game;

public interface GameDAO {
	public Game findByDateTeam(LocalDate gameDate, String teamKey);
	public List<Game> findByDateTeamSeason(LocalDate gameDate, String teamKey);
	public List<Game> findByDateRangeSize(LocalDate gameDate, int maxRows);
	public List<Game> findByDateScheduled(LocalDate gameDate);
	public LocalDateTime findPreviousGameDateTimeByDateTeam(LocalDate gameDate, String teamKey);
	public int findCountGamesByDateScheduled(LocalDate gameDate);
	public Game createGame(Game game);
	public Game updateGame(Game game);
	public Game deleteGame(LocalDate gameDate, String teamKey);
}
