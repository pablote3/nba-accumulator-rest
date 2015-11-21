package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Team;

public interface TeamDAO {
	public Team findTeamByKey(String key, LocalDate asOfDate);
	public List<Team> findTeamsByDateRange(LocalDate fromDate, LocalDate toDate);
	public void createTeam(Team team);
}
