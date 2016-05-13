package com.rossotti.basketball.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.model.Standing;
import com.rossotti.basketball.dao.model.StatusCode;

@Repository
@Transactional
public class StandingRepository {
	@Autowired
	private SessionFactory sessionFactory;

	public Standing findStanding(String teamKey, LocalDate asOfDate) {
		String sql = 	"select s from Standing s " +
						"inner join s.team t " +
						"where t.teamKey = :teamKey " +
						"and s.standingDate = :asOfDate";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		query.setParameter("teamKey", teamKey);
		query.setParameter("asOfDate", asOfDate);

		Standing standing = (Standing)query.uniqueResult();
		if (standing == null) {
			standing = new Standing(StatusCode.NotFound);
		}
		else {
			standing.setStatusCode(StatusCode.Found);
		}
		return standing;
	}

	@SuppressWarnings("unchecked")
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

	public Standing createStanding(Standing createStanding) {
		Standing standing = findStanding(createStanding.getTeam().getTeamKey(), createStanding.getStandingDate());
		if (standing.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createStanding);
			createStanding.setStatusCode(StatusCode.Created);
			return createStanding;
		}
		else {
			return standing;
		}
	}

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
