package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Team;

public interface TeamDAO {
	public Team findTeam(String key, LocalDate fromDate, LocalDate toDate);
	public List<Team> findTeams(LocalDate fromDate, LocalDate toDate);
	public void createTeam(Team team);
	public void updateTeam(Team team);
	public void deleteTeam(String key, LocalDate fromDate, LocalDate toDate);
}
