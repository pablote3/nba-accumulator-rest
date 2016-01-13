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
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.StatusCode;
import com.rossotti.basketball.model.Team;

@Repository
@Transactional
public class TeamDAOImpl implements TeamDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Team findTeam(String teamKey, LocalDate asOfDate) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("teamKey", teamKey))
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
			.uniqueResult();
		if (team == null) {
			team = new Team(StatusCode.NotFound);
		}
		return team;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findTeams(LocalDate asOfDate) {
		List<Team> teams = getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
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
		Team team = findTeam(createTeam.getTeamKey(), createTeam.getFromDate());
		if (team.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createTeam);
			createTeam.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return createTeam;
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
			team.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(team);
		}
		return team;
	}

	@Override
	public Team deleteTeam(String teamKey, LocalDate asOfDate) {
		Team team = findTeam(teamKey, asOfDate);
		if (team.isFound()) {
			getSessionFactory().getCurrentSession().delete(team);
			team = new Team(StatusCode.Deleted);
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
