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
		if (team != null) {
			return team;
		}
		else {
			return new Team(StatusCode.NotFound);
		}
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
		getSessionFactory().getCurrentSession().persist(createTeam);
		createTeam.setStatusCode(StatusCode.Created);
		return createTeam;
	}

	@Override
	public Team updateTeam(Team updateTeam) {
		getSessionFactory().getCurrentSession().saveOrUpdate(updateTeam);
		updateTeam.setStatusCode(StatusCode.Updated);
		return updateTeam;
	}

	@Override
	public Team deleteTeam(Team deleteTeam) {
		getSessionFactory().getCurrentSession().delete(deleteTeam);
		deleteTeam.setStatusCode(StatusCode.Deleted);
		return deleteTeam;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
