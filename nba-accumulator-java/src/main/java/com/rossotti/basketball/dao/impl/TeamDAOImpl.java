package com.rossotti.basketball.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Team;

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
			throw new NoSuchEntityException();
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
		if (teams == null || teams.size() == 0) {
			throw new NoSuchEntityException();
		}
		return teams;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findTeams(String teamKey) {
		List<Team> teams = getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("teamKey", teamKey))
			.list();
		if (teams == null || teams.size() == 0) {
			throw new NoSuchEntityException();
		}
		return teams;
	}

	@Override
	public void createTeam(Team createTeam) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
				.add(Restrictions.eq("teamKey", createTeam.getTeamKey()))
				.add(Restrictions.le("fromDate", createTeam.getFromDate()))
				.add(Restrictions.ge("toDate", createTeam.getToDate()))
				.uniqueResult();
		if (team == null) {
			getSessionFactory().getCurrentSession().persist(createTeam);
		}
		else {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public void updateTeam(Team updateTeam) {
		Team team = findTeam(updateTeam.getTeamKey(), updateTeam.getFromDate(), updateTeam.getToDate());
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

	@Override
	public void deleteTeam(String teamKey, LocalDate fromDate, LocalDate toDate) {
		Team team = findTeam(teamKey, fromDate, toDate);
		getSessionFactory().getCurrentSession().delete(team);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
