package com.rossotti.basketball.business.impl;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.business.TeamBusiness;
import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Team;

@Service
public class TeamBusinessImpl implements TeamBusiness {
	@Autowired
	private TeamDAO teamDAO;

	@Override
	public Team findTeam(String teamKey, LocalDate asOfDate) {
		return teamDAO.findTeam(teamKey, asOfDate);
	}

	@Override
	public List<Team> findTeams(LocalDate asOfDate) {
		return teamDAO.findTeams(asOfDate);
	}

	@Override
	public List<Team> findTeams(String teamKey) {
		return teamDAO.findTeams(teamKey);
	}

	@Override
	public Team createTeam(Team createTeam) {
		Team findTeam = teamDAO.findTeam(createTeam.getTeamKey(), createTeam.getFromDate());
		if (findTeam.isNotFound()) {
			return teamDAO.createTeam(createTeam);
		}
		else {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public Team updateTeam(Team updateTeam) {
		Team team = findTeam(updateTeam.getTeamKey(), updateTeam.getFromDate());
		if (team.isFound()) {
			team.setLastName(updateTeam.getLastName());
			team.setFirstName(updateTeam.getFirstName());
			team.setFullName(updateTeam.getFullName());
			team.setAbbr(updateTeam.getAbbr());
			team.setFromDate(updateTeam.getFromDate());
			team.setToDate(updateTeam.getToDate());
			team.setConference(updateTeam.getConference());
			team.setDivision(updateTeam.getDivision());
			team.setCity(updateTeam.getCity());
			team.setState(updateTeam.getState());
			team.setSiteName(updateTeam.getSiteName());
			return teamDAO.deleteTeam(team);
		}
		else {
			return team;
		}
	}

	@Override
	public Team deleteTeam(String teamKey, LocalDate asOfDate) {
		Team team = teamDAO.findTeam(teamKey, asOfDate);
		if (team.isFound()) {
			return teamDAO.deleteTeam(team);
		}
		else {
			return team;
		}
	}
}
