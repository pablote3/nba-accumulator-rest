package com.rossotti.basketball.dao;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Team;

public interface TeamDAO {
	public Team findTeamByKey(String key, LocalDate asOfDate);
	public void createTeam(Team team);
}
