package com.rossotti.basketball.business;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.model.Team;

public interface TeamBusiness {
	public Team findTeam(String key, LocalDate asOfDate);
	public List<Team> findTeams(String key);
	public List<Team> findTeams(LocalDate asOfDate);
	public Team createTeam(Team team);
	public Team updateTeam(Team team);
	public Team deleteTeam(String key, LocalDate asOfDate);
}
