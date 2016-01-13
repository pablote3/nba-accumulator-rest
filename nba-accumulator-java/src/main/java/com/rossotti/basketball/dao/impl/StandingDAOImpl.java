package com.rossotti.basketball.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.StandingDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Standing;
import com.rossotti.basketball.model.StatusCode;

@Repository
@Transactional
public class StandingDAOImpl implements StandingDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Standing findStanding(String teamKey, LocalDate asOfDate) {
		String sql = 	"select s " +
						"from Standing s " +
						"inner join s.team t " +
						"where t.teamKey = '" + teamKey + "' " +
						"and s.standingDate = '" + asOfDate + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		Standing standing = (Standing)query.uniqueResult();
		if (standing == null) {
			standing = new Standing(StatusCode.NotFound);
		}
		return standing;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Standing> findStandings(LocalDate asOfDate) {
		String sql = 	"select s " +
						"from Standing s " +
						"inner join s.team t " +
						"where s.standingDate = '" + asOfDate + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<Standing> standings = (List<Standing>)query.list();
		if (standings == null) {
			standings = new ArrayList<Standing>();
		}
		return standings;
	}

	@Override
	public Standing createStanding(Standing s) {
		Standing standing = findStanding(s.getTeam().getTeamKey(), s.getStandingDate());
		if (standing.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(s);
			s.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return s;
	}

	@Override
	public Standing updateStanding(Standing s) {
		Standing standing = findStanding(s.getTeam().getTeamKey(), s.getStandingDate());
		if (standing.isFound()) {
			standing.setRank(s.getRank());
			standing.setOrdinalRank(s.getOrdinalRank());
			standing.setGamesWon(s.getGamesWon());
			standing.setGamesLost(s.getGamesLost());
			standing.setStreak(s.getStreak());
			standing.setStreakType(s.getStreakType());
			standing.setStreakTotal(s.getStreakTotal());
			standing.setGamesBack(s.getGamesBack());
			standing.setPointsFor(s.getPointsFor());
			standing.setPointsAgainst(s.getPointsAgainst());
			standing.setHomeWins(s.getHomeWins());
			standing.setHomeLosses(s.getHomeLosses());
			standing.setAwayWins(s.getAwayWins());
			standing.setAwayLosses(s.getAwayLosses());
			standing.setConferenceWins(s.getConferenceWins());
			standing.setConferenceLosses(s.getConferenceLosses());
			standing.setLastFive(s.getLastFive());
			standing.setLastTen(s.getLastTen());
			standing.setGamesPlayed(s.getGamesPlayed());
			standing.setPointsScoredPerGame(s.getPointsScoredPerGame());
			standing.setPointsAllowedPerGame(s.getPointsAllowedPerGame());
			standing.setWinPercentage(s.getWinPercentage());
			standing.setPointDifferential(s.getPointDifferential());
			standing.setPointDifferentialPerGame(s.getPointDifferentialPerGame());
			standing.setOpptGamesWon(s.getOpptGamesWon());
			standing.setOpptGamesPlayed(s.getOpptGamesPlayed());
			standing.setOpptOpptGamesWon(s.getOpptOpptGamesWon());
			standing.setOpptOpptGamesPlayed(s.getOpptOpptGamesPlayed());
			standing.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(standing);
		}
		return standing;
	}

	@Override
	public Standing deleteStanding(String teamKey, LocalDate asOfDate) {
		Standing standing = findStanding(teamKey, asOfDate);
		if (standing.isFound()) {
			getSessionFactory().getCurrentSession().delete(standing);
			standing = new Standing(StatusCode.Deleted);
		}
		return standing;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
