package com.rossotti.basketball.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rossotti.basketball.dao.TeamDAO;
import com.rossotti.basketball.models.Team;

@Repository("teamDAO")
public class TeamDAOImpl implements TeamDAO {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void create(Team team) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.persist(team);
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
