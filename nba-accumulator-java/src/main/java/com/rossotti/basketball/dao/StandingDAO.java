package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.model.Standing;

public interface StandingDAO {
	public Standing findStanding(String teamKey, LocalDate asOfDate);
	public List<Standing> findStandings(LocalDate asOfDate);
	public Standing createStanding(Standing standing);
	public Standing updateStanding(Standing standing);
	public Standing deleteStanding(String teamKey, LocalDate asOfDate);
}
