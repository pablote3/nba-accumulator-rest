package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.rossotti.basketball.model.Game;

public interface GameDAO {
	public Game findById(Long id);
	public List<Game> findById(List<Long> ids);
	public Long findIdByDateTeam(LocalDate gameDate, String teamKey);
	public List<Long> findIdsByDateRangeSize(LocalDate gameDate, int maxRows);
	public List<Long> findIdsByDateScheduled(LocalDate gameDate);
	public LocalDateTime findPreviousGameDateTimeByDateTeam(LocalDate gameDate, String teamKey);
	public List<Long> findByDateTeamSeason(LocalDate gameDate, String teamKey);
	public int findCountGamesByDateScheduled(LocalDate gameDate);
//	public List<Game> findGames(LocalDate gameDate);
//	public Game createGame(Game game);
//	public Game updateGame(Game game);
//	public Game deleteGame(LocalDate gameDate, String teamKey);
}
