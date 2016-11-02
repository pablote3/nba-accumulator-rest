package com.rossotti.basketball.dao.repository;

import com.rossotti.basketball.dao.model.StatusCodeDAO;
import com.rossotti.basketball.dao.model.Team;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TeamRepository {
	private final SessionFactory sessionFactory;

	@Autowired
	public TeamRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Team findTeam(String teamKey, LocalDate asOfDate) {
		Team team = (Team)getSession().createCriteria(Team.class)
				.add(Restrictions.eq("teamKey", teamKey))
				.add(Restrictions.le("fromDate", asOfDate))
				.add(Restrictions.ge("toDate", asOfDate))
				.uniqueResult();
		if (team == null) {
			team = new Team(StatusCodeDAO.NotFound);
		}
		else {
			team.setStatusCode(StatusCodeDAO.Found);
		}
		return team;
	}

	public Team findTeamByLastName(String lastName, LocalDate asOfDate) {
		Team team = (Team)getSession().createCriteria(Team.class)
				.add(Restrictions.eq("lastName", lastName))
				.add(Restrictions.le("fromDate", asOfDate))
				.add(Restrictions.ge("toDate", asOfDate))
				.uniqueResult();
		if (team == null) {
			team = new Team(StatusCodeDAO.NotFound);
		}
		else {
			team.setStatusCode(StatusCodeDAO.Found);
		}
		return team;
	}

	@SuppressWarnings("unchecked")
	public List<Team> findTeams(LocalDate asOfDate) {
		List<Team> teams = getSession().createCriteria(Team.class)
				.add(Restrictions.le("fromDate", asOfDate))
				.add(Restrictions.ge("toDate", asOfDate))
				.list();
		if (teams == null) {
			teams = new ArrayList<Team>();
		}
		return teams;
	}

	@SuppressWarnings("unchecked")
	public List<Team> findTeams(String teamKey) {
		List<Team> teams = getSession().createCriteria(Team.class)
				.add(Restrictions.eq("teamKey", teamKey))
				.list();
		if (teams == null) {
			teams = new ArrayList<Team>();
		}
		return teams;
	}

	public Team createTeam(Team createTeam) {
		Team team = findTeam(createTeam.getTeamKey(), createTeam.getFromDate());
		if (team.isNotFound()) {
			getSession().persist(createTeam);
			createTeam.setStatusCode(StatusCodeDAO.Created);
			return createTeam;
		}
		else {
			return team;
		}
	}

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
			team.setStatusCode(StatusCodeDAO.Updated);
			getSession().saveOrUpdate(team);
		}
		return team;
	}

	public Team deleteTeam(String teamKey, LocalDate asOfDate) {
		Team team = findTeam(teamKey, asOfDate);
		if (team.isFound()) {
			getSession().delete(team);
			team.setStatusCode(StatusCodeDAO.Deleted);
			team = new Team(StatusCodeDAO.Deleted);
		}
		return team;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}