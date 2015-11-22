package com.rossotti.basketball.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Team;

@Repository
public class TeamDAOImpl implements TeamDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Team findTeamByKeyAsOfDate(String key, LocalDate asOfDate) {
		Session session = getSessionFactory().openSession();
		Team team;
		team = (Team)session.createCriteria(Team.class)
			.add(Restrictions.eq("key", key))
			.add(Restrictions.le("fromDate", asOfDate))
			.add(Restrictions.ge("toDate", asOfDate))
			.uniqueResult();
		if (team == null) {
			throw new NoSuchEntityException();
		}
		return team;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Team> findTeamsByDateRange(LocalDate fromDate, LocalDate toDate) {
		Session session = getSessionFactory().openSession();
		List<Team> teams = session.createCriteria(Team.class)
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
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.persist(team);
		} catch (ConstraintViolationException e) {
			throw new DuplicateEntityException();
		}
		tx.commit();
		session.close();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
