package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Team;

public interface TeamDAO {
	public Team findTeam(String key, LocalDate fromDate, LocalDate toDate);
	public List<Team> findTeams(String key);
	public List<Team> findTeams(LocalDate fromDate, LocalDate toDate);
	public Team createTeam(Team team);
	public Team updateTeam(Team team);
	public Team deleteTeam(String key, LocalDate fromDate, LocalDate toDate);
}
