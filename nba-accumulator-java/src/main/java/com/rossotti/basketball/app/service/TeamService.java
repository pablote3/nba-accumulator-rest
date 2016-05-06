package com.rossotti.basketball.app.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.model.Team;
import com.rossotti.basketball.dao.repository.TeamRepository;

@Service
public class TeamService {
	@Autowired
	private TeamRepository teamRepo;

	public Team findTeam(String teamKey, LocalDate gameDate) {
		return teamRepo.findTeam(teamKey, gameDate);
	}
}