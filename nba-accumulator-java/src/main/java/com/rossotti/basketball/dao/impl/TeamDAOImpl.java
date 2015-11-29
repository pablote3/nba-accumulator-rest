package com.rossotti.basketball.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
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
	public Team findTeam(String key, LocalDate fromDate, LocalDate toDate) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("key", key))
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
		if (teams == null) {
			throw new NoSuchEntityException();
		}
		return teams;
	}

	@Override
	public void createTeam(Team team) {
		try {
			getSessionFactory().getCurrentSession().persist(team);
		} catch (ConstraintViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public void updateTeam(Team updateTeam) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("key", updateTeam.getKey()))
			.add(Restrictions.le("fromDate", updateTeam.getFromDate()))
			.add(Restrictions.ge("toDate", updateTeam.getToDate()))
			.uniqueResult();
		if (team != null) {
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
		else {
			throw new NoSuchEntityException();
		}
	}

	@Override
	public void deleteTeam(String key, LocalDate asOfDate) {
		Team team = (Team)getSessionFactory().getCurrentSession().createCriteria(Team.class)
			.add(Restrictions.eq("key", key))
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
			.uniqueResult();
		if (team != null) {
			getSessionFactory().getCurrentSession().delete(team);
		}
		else {
			throw new NoSuchEntityException();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
