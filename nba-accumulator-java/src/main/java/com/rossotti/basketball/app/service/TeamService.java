package com.rossotti.basketball.app.service;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.exception.NoSuchEntityException;
import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.TeamRepository;

@Service
public class TeamService {
	private final TeamRepository teamRepo;

	private final Logger logger = LoggerFactory.getLogger(TeamService.class);

	@Autowired
	public TeamService(TeamRepository teamRepo) {
		this.teamRepo = teamRepo;
	}

	public Team findTeam(String teamKey, LocalDate gameDate) {
		Team team = teamRepo.findTeam(teamKey, gameDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + teamKey);
			throw new NoSuchEntityException(Team.class);
		}
		return team;
	}

	public Team findTeamByLastName(String lastName, LocalDate gameDate) {
		Team team = teamRepo.findTeamByLastName(lastName, gameDate);
		if (team.isNotFound()) {
			logger.info("Team not found " + lastName);
			throw new NoSuchEntityException(Team.class);
		}
		return team;
	}
}