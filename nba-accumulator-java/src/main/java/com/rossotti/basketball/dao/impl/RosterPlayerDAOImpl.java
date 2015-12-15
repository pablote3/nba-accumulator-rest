package com.rossotti.basketball.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.RosterPlayerDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.models.RosterPlayer;
import com.rossotti.basketball.models.RosterPlayer.Status;

@Repository
@Transactional
public class RosterPlayerDAOImpl implements RosterPlayerDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public RosterPlayer findRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		String sql = 	"select rp " +
						"from RosterPlayer rp " +
						"inner join rp.player p " +
						"where p.lastName = '" + lastName + "' " +
						"and p.firstName = '" + firstName + "' " +
						"and p.birthdate = '" + birthdate + "' " +
						"and rp.fromDate <= '" + fromDate + "' " +
						"and rp.toDate >= '" + toDate + "' ";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		RosterPlayer rosterPlayer = (RosterPlayer)query.uniqueResult();
		if (rosterPlayer == null) {
			rosterPlayer = new RosterPlayer(Status.NotFound);
		}
		return rosterPlayer;
	}

	@Override
	public RosterPlayer findRosterPlayer(String lastName, String firstName, String teamKey, LocalDate fromDate, LocalDate toDate) {
		String sql = 	"select rp " +
						"from RosterPlayer rp " +
						"inner join rp.player p " +
						"inner join rp.team t " +
						"where p.lastName = '" + lastName + "' " +
						"and p.firstName = '" + firstName + "' " +
						"and t.teamKey = '" + teamKey + "' " +
						"and rp.fromDate <= '" + fromDate + "' " +
						"and rp.toDate >= '" + toDate + "' ";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		RosterPlayer rosterPlayer = (RosterPlayer)query.uniqueResult();
		if (rosterPlayer == null) {
			rosterPlayer = new RosterPlayer(Status.NotFound);
		}
		return rosterPlayer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RosterPlayer> findRosterPlayers(String teamKey, LocalDate asOfDate) {
		String sql = 	"select rp " +
						"from RosterPlayer rp " +
						"inner join rp.team t " +
						"where t.teamKey = '" + teamKey + "' " +
						"and rp.fromDate <= '" + asOfDate + "' " +
						"and rp.toDate >= '" + asOfDate + "' ";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<RosterPlayer> rosterPlayers = (List<RosterPlayer>)query.list();
		if (rosterPlayers == null) {
			rosterPlayers = new ArrayList<RosterPlayer>();
		}
		return rosterPlayers;
	}

	@Override
	public RosterPlayer createRosterPlayer(RosterPlayer rp) {
		RosterPlayer rosterPlayer = findRosterPlayer(rp.getPlayer().getLastName(), rp.getPlayer().getFirstName(), rp.getPlayer().getBirthdate(), rp.getFromDate(), rp.getToDate());
		if (rosterPlayer.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(rp);
			rp.setStatus(Status.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return rp;
	}

	@Override
	public RosterPlayer updateRosterPlayer(RosterPlayer rp) {
		RosterPlayer rosterPlayer = findRosterPlayer(rp.getPlayer().getLastName(), rp.getPlayer().getFirstName(), rp.getPlayer().getBirthdate(), rp.getFromDate(), rp.getToDate());
		if (rosterPlayer.isFound()) {
			rosterPlayer.setFromDate(rp.getFromDate());
			rosterPlayer.setToDate(rp.getToDate());
			rosterPlayer.setNumber(rp.getNumber());
			rosterPlayer.setPosition(rp.getPosition());
			rosterPlayer.setStatus(Status.Updated);
			getSessionFactory().getCurrentSession().persist(rosterPlayer);
		}
		return rosterPlayer;
	}

	@Override
	public RosterPlayer deleteRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate fromDate, LocalDate toDate) {
		RosterPlayer rosterPlayer = findRosterPlayer(lastName, firstName, birthdate, fromDate, toDate);
		if (rosterPlayer.isFound()) {
			getSessionFactory().getCurrentSession().delete(rosterPlayer);
			rosterPlayer = new RosterPlayer(Status.Deleted);
		}
		return rosterPlayer;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
