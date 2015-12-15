package com.rossotti.basketball.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.models.Team;
import com.rossotti.basketball.models.Team.Status;

@Repository
@Transactional
public class TeamDAOImpl implements TeamDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Team findTeam(String teamKey, LocalDate fromDate, LocalDate toDate) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("teamKey", teamKey))
			.add(Restrictions.le("fromDate", fromDate))
			.add(Restrictions.ge("toDate", toDate))
			.uniqueResult();
		if (team == null) {
			team = new Team(Status.NotFound);
		}
		return team;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findTeams(LocalDate fromDate, LocalDate toDate) {
		List<Team> teams = getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.le("fromDate", fromDate))
			.add(Restrictions.ge("toDate", toDate))
			.list();
		if (teams == null) {
			teams = new ArrayList<Team>();
		}
		return teams;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findTeams(String teamKey) {
		List<Team> teams = getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("teamKey", teamKey))
			.list();
		if (teams == null) {
			teams = new ArrayList<Team>();
		}
		return teams;
	}

	@Override
	public Team createTeam(Team createTeam) {
		Team team = findTeam(createTeam.getTeamKey(), createTeam.getFromDate(), createTeam.getToDate());
		if (team.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createTeam);
			createTeam.setStatus(Status.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return createTeam;
	}

	@Override
	public Team updateTeam(Team updateTeam) {
		Team team = findTeam(updateTeam.getTeamKey(), updateTeam.getFromDate(), updateTeam.getToDate());
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
			getSessionFactory().getCurrentSession().persist(team);
		}
		return team;
	}

	@Override
	public Team deleteTeam(String teamKey, LocalDate fromDate, LocalDate toDate) {
		Team team = findTeam(teamKey, fromDate, toDate);
		if (team.isFound()) {
			getSessionFactory().getCurrentSession().delete(team);
			team = new Team(Status.Deleted);
		}
		return team;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
