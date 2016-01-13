package com.rossotti.basketball.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.RosterPlayer;
import com.rossotti.basketball.model.StatusCode;

@Repository
@Transactional
public class RosterPlayerDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public RosterPlayer findRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate) {
		String sql = 	"select rp " +
						"from RosterPlayer rp " +
						"inner join rp.player p " +
						"where p.lastName = '" + lastName + "' " +
						"and p.firstName = '" + firstName + "' " +
						"and p.birthdate = '" + birthdate + "' " +
						"and rp.fromDate <= '" + asOfDate + "' " +
						"and rp.toDate >= '" + asOfDate + "' ";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		RosterPlayer rosterPlayer = (RosterPlayer)query.uniqueResult();
		if (rosterPlayer == null) {
			rosterPlayer = new RosterPlayer(StatusCode.NotFound);
		}
		return rosterPlayer;
	}

	public RosterPlayer findRosterPlayer(String lastName, String firstName, String teamKey, LocalDate asOfDate) {
		String sql = 	"select rp " +
						"from RosterPlayer rp " +
						"inner join rp.player p " +
						"inner join rp.team t " +
						"where p.lastName = '" + lastName + "' " +
						"and p.firstName = '" + firstName + "' " +
						"and t.teamKey = '" + teamKey + "' " +
						"and rp.fromDate <= '" + asOfDate + "' " +
						"and rp.toDate >= '" + asOfDate + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		RosterPlayer rosterPlayer = (RosterPlayer)query.uniqueResult();
		if (rosterPlayer == null) {
			rosterPlayer = new RosterPlayer(StatusCode.NotFound);
		}
		return rosterPlayer;
	}

	@SuppressWarnings("unchecked")
	public List<RosterPlayer> findRosterPlayers(String lastName, String firstName, LocalDate birthdate) {
		String sql = 	"select rp " +
				"from RosterPlayer rp " +
				"inner join rp.player p " +
				"where p.lastName = '" + lastName + "' " +
				"and p.firstName = '" + firstName + "' " +
				"and p.birthdate = '" + birthdate + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<RosterPlayer> rosterPlayers = (List<RosterPlayer>)query.list();
		if (rosterPlayers == null) {
			rosterPlayers = new ArrayList<RosterPlayer>();
		}
		return rosterPlayers;
	}

	@SuppressWarnings("unchecked")
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

	public RosterPlayer createRosterPlayer(RosterPlayer rp) {
		RosterPlayer rosterPlayer = findRosterPlayer(rp.getPlayer().getLastName(), rp.getPlayer().getFirstName(), rp.getPlayer().getBirthdate(), rp.getFromDate());
		if (rosterPlayer.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(rp);
			rp.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return rp;
	}

	public RosterPlayer updateRosterPlayer(RosterPlayer rp) {
		RosterPlayer rosterPlayer = findRosterPlayer(rp.getPlayer().getLastName(), rp.getPlayer().getFirstName(), rp.getPlayer().getBirthdate(), rp.getFromDate());
		if (rosterPlayer.isFound()) {
			rosterPlayer.setFromDate(rp.getFromDate());
			rosterPlayer.setToDate(rp.getToDate());
			rosterPlayer.setNumber(rp.getNumber());
			rosterPlayer.setPosition(rp.getPosition());
			rosterPlayer.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(rosterPlayer);
		}
		return rosterPlayer;
	}

	public RosterPlayer deleteRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate) {
		RosterPlayer rosterPlayer = findRosterPlayer(lastName, firstName, birthdate, asOfDate);
		if (rosterPlayer.isFound()) {
			getSessionFactory().getCurrentSession().delete(rosterPlayer);
			rosterPlayer = new RosterPlayer(StatusCode.Deleted);
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
