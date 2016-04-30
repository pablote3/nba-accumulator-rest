package com.rossotti.basketball.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.model.BoxScore;
import com.rossotti.basketball.dao.model.Game;
import com.rossotti.basketball.dao.model.GameOfficial;
import com.rossotti.basketball.dao.model.GameStatus;
import com.rossotti.basketball.dao.model.StatusCode;
import com.rossotti.basketball.util.DateTimeUtil;

@Repository
@Transactional
public class GameRepository {
	@Autowired
	private SessionFactory sessionFactory;

	public Game findByDateTeam(LocalDate gameDate, String teamKey) {
		LocalDateTime fromDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime toDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		String sql = 	"select game " +
						"from Game game " +
						"inner join game.boxScores boxScores " +
						"inner join boxScores.team team " +
						"where game.gameDateTime between '" + fromDateTime + "' and '" + toDateTime +"' " +
						"and team.teamKey = '" + teamKey + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		Game findGame = (Game)query.uniqueResult();
		if (findGame == null) {
			return new Game(StatusCode.NotFound);
		} 
		else {
			Game game = (Game)getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.eq("id", findGame.getId()))
				.uniqueResult();
			return game;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Game> findByDateTeamSeason(LocalDate gameDate, String teamKey) {
		LocalDateTime fromDateTime = DateTimeUtil.getLocalDateTimeSeasonMin(gameDate);
		LocalDateTime toDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		String sql = 	"select game " +
						"from Game game " +
						"left join game.boxScores boxScores " +
						"inner join boxScores.team team " +
						"where game.gameDateTime between '" + fromDateTime + "' and '" + toDateTime +"' " +
						"and game.status in ('" + GameStatus.Completed + "', '" + GameStatus.Scheduled + "') " + 
						"and team.teamKey = '" + teamKey + "' " +
						"order by gameDateTime asc";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<Game> findGames = query.list();
		List<Game> games = new ArrayList<Game>();
		if (findGames != null) {
			for (int i = 0; i < findGames.size(); i++) {
				Game game = (Game)getSessionFactory().getCurrentSession().createCriteria(Game.class)
					.add(Restrictions.eq("id", findGames.get(i).getId()))
					.uniqueResult();
				games.add(game);
			}
		}
		return games;
	}

	@SuppressWarnings("unchecked")
	public List<Game> findByDateRangeSize(LocalDate gameDate, int maxRows) {
		LocalDateTime gameDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime maxDateTime = DateTimeUtil.getLocalDateTimeSeasonMax(gameDate);
		List<Game> findGames = getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.between("gameDateTime", gameDateTime, maxDateTime))
				.addOrder(Order.asc("gameDateTime"))
				.setMaxResults(maxRows)
				.list();
		List<Game> games = new ArrayList<Game>();
		if (findGames != null) {
			for (int i = 0; i < findGames.size(); i++) {
				Game game = (Game)getSessionFactory().getCurrentSession().createCriteria(Game.class)
					.add(Restrictions.eq("id", findGames.get(i).getId()))
					.uniqueResult();
				games.add(game);
			}
		}
		return games;
	}

	@SuppressWarnings("unchecked")
	public List<Game> findByDate(LocalDate gameDate) {
		LocalDateTime minDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime maxDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		List<Game> findGames = getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.between("gameDateTime", minDateTime, maxDateTime))
				.addOrder(Order.desc("status"))
				.addOrder(Order.asc("gameDateTime"))
				.list();
		List<Game> games = new ArrayList<Game>();
		if (findGames != null) {
			for (int i = 0; i < findGames.size(); i++) {
				Game game = (Game)getSessionFactory().getCurrentSession().createCriteria(Game.class)
					.add(Restrictions.eq("id", findGames.get(i).getId()))
					.uniqueResult();
				games.add(game);
			}
		}
		return games;
	}

	@SuppressWarnings("unchecked")
	public LocalDateTime findPreviousGameDateTimeByDateTeam(LocalDate gameDate, String teamKey) {
		LocalDateTime gameDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		String sql = 	"select game " +
						"from Game game " +
						"left join game.boxScores boxScores " +
						"inner join boxScores.team team " +
						"where game.gameDateTime <= '" + gameDateTime + "' " +
						"and game.status = '" + GameStatus.Completed + "' " +
						"and team.teamKey = '" + teamKey + "' " +
						"order by gameDateTime desc";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<Game> games = query.list();
		
		LocalDateTime lastGameDateTime = null;
		if (games.size() > 0) {
			lastGameDateTime = games.get(0).getGameDateTime();
		}
		return lastGameDateTime;
	}

	@SuppressWarnings("unchecked")
	public int findCountGamesByDateScheduled(LocalDate gameDate) {
		LocalDateTime fromDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime toDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		List<Game> games = getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.between("gameDateTime", fromDateTime, toDateTime))
				.add(Restrictions.eq("status", GameStatus.Scheduled))
				.list();
		return games.size();
	}

	public Game createGame(Game createGame) {
		Game findGame = findByDateTeam(DateTimeUtil.getLocalDate(createGame.getGameDateTime()), createGame.getBoxScores().get(0).getTeam().getTeamKey());
		if (findGame.isNotFound()) {
			for (int i = 0; i < createGame.getBoxScores().size(); i++) {
				createGame.getBoxScores().get(i).setGame(createGame);
			}
			getSessionFactory().getCurrentSession().persist(createGame);
			createGame.setStatusCode(StatusCode.Created);
			return createGame;
		}
		else {
			return findGame;
		}
	}

	public Game updateGame(Game updateGame) {
		Game findGame = findByDateTeam(DateTimeUtil.getLocalDate(updateGame.getGameDateTime()), updateGame.getBoxScores().get(0).getTeam().getTeamKey());
		if (findGame.isFound()) {
			findGame.setStatus(updateGame.getStatus());

			for (int i = 0; i < updateGame.getGameOfficials().size(); i++) {
				GameOfficial gameOfficial = updateGame.getGameOfficials().get(i);
				gameOfficial.setGame(findGame);
				findGame.addGameOfficial(gameOfficial);
			}
			BoxScore findHomeBoxScore = findGame.getBoxScoreHome();
			BoxScore updateHomeBoxScore = updateGame.getBoxScoreHome();
			findHomeBoxScore.setResult(updateHomeBoxScore.getResult());
			findHomeBoxScore.setMinutes(updateHomeBoxScore.getMinutes());
			findHomeBoxScore.setPoints(updateHomeBoxScore.getPoints());
			findHomeBoxScore.setPointsPeriod1(updateHomeBoxScore.getPointsPeriod1());
			findHomeBoxScore.setPointsPeriod2(updateHomeBoxScore.getPointsPeriod2());
			findHomeBoxScore.setPointsPeriod3(updateHomeBoxScore.getPointsPeriod3());
			findHomeBoxScore.setPointsPeriod4(updateHomeBoxScore.getPointsPeriod4());
			findHomeBoxScore.setPointsPeriod5(updateHomeBoxScore.getPointsPeriod5());
			findHomeBoxScore.setPointsPeriod6(updateHomeBoxScore.getPointsPeriod6());
			findHomeBoxScore.setPointsPeriod7(updateHomeBoxScore.getPointsPeriod7());
			findHomeBoxScore.setPointsPeriod8(updateHomeBoxScore.getPointsPeriod8());
			findHomeBoxScore.setAssists(updateHomeBoxScore.getAssists());
			findHomeBoxScore.setTurnovers(updateHomeBoxScore.getTurnovers());
			findHomeBoxScore.setSteals(updateHomeBoxScore.getSteals());
			findHomeBoxScore.setBlocks(updateHomeBoxScore.getBlocks());
			findHomeBoxScore.setFieldGoalAttempts(updateHomeBoxScore.getFieldGoalAttempts());
			findHomeBoxScore.setFieldGoalMade(updateHomeBoxScore.getFieldGoalMade());
			findHomeBoxScore.setFieldGoalPercent(updateHomeBoxScore.getFieldGoalPercent());
			findHomeBoxScore.setThreePointAttempts(updateHomeBoxScore.getThreePointAttempts());
			findHomeBoxScore.setThreePointMade(updateHomeBoxScore.getThreePointMade());
			findHomeBoxScore.setThreePointPercent(updateHomeBoxScore.getThreePointPercent());
			findHomeBoxScore.setFreeThrowAttempts(updateHomeBoxScore.getFreeThrowAttempts());
			findHomeBoxScore.setFreeThrowMade(updateHomeBoxScore.getFreeThrowMade());
			findHomeBoxScore.setFreeThrowPercent(updateHomeBoxScore.getFreeThrowPercent());
			findHomeBoxScore.setReboundsOffense(updateHomeBoxScore.getReboundsOffense());
			findHomeBoxScore.setReboundsDefense(updateHomeBoxScore.getReboundsDefense());
			findHomeBoxScore.setPersonalFouls(updateHomeBoxScore.getPersonalFouls());
			findHomeBoxScore.setDaysOff(updateHomeBoxScore.getDaysOff());
			findHomeBoxScore.setBoxScorePlayers(updateHomeBoxScore.getBoxScorePlayers());
			for (int i = 0; i < findHomeBoxScore.getBoxScorePlayers().size(); i++) {
				findHomeBoxScore.getBoxScorePlayers().get(i).setBoxScore(findHomeBoxScore);
			}

			BoxScore findAwayBoxScore = findGame.getBoxScoreAway();
			BoxScore updateAwayBoxScore = updateGame.getBoxScoreAway();
			findAwayBoxScore.setResult(updateAwayBoxScore.getResult());
			findAwayBoxScore.setMinutes(updateAwayBoxScore.getMinutes());
			findAwayBoxScore.setPoints(updateAwayBoxScore.getPoints());
			findAwayBoxScore.setPointsPeriod1(updateAwayBoxScore.getPointsPeriod1());
			findAwayBoxScore.setPointsPeriod2(updateAwayBoxScore.getPointsPeriod2());
			findAwayBoxScore.setPointsPeriod3(updateAwayBoxScore.getPointsPeriod3());
			findAwayBoxScore.setPointsPeriod4(updateAwayBoxScore.getPointsPeriod4());
			findAwayBoxScore.setPointsPeriod5(updateAwayBoxScore.getPointsPeriod5());
			findAwayBoxScore.setPointsPeriod6(updateAwayBoxScore.getPointsPeriod6());
			findAwayBoxScore.setPointsPeriod7(updateAwayBoxScore.getPointsPeriod7());
			findAwayBoxScore.setPointsPeriod8(updateAwayBoxScore.getPointsPeriod8());
			findAwayBoxScore.setAssists(updateAwayBoxScore.getAssists());
			findAwayBoxScore.setTurnovers(updateAwayBoxScore.getTurnovers());
			findAwayBoxScore.setSteals(updateAwayBoxScore.getSteals());
			findAwayBoxScore.setBlocks(updateAwayBoxScore.getBlocks());
			findAwayBoxScore.setFieldGoalAttempts(updateAwayBoxScore.getFieldGoalAttempts());
			findAwayBoxScore.setFieldGoalMade(updateAwayBoxScore.getFieldGoalMade());
			findAwayBoxScore.setFieldGoalPercent(updateAwayBoxScore.getFieldGoalPercent());
			findAwayBoxScore.setThreePointAttempts(updateAwayBoxScore.getThreePointAttempts());
			findAwayBoxScore.setThreePointMade(updateAwayBoxScore.getThreePointMade());
			findAwayBoxScore.setThreePointPercent(updateAwayBoxScore.getThreePointPercent());
			findAwayBoxScore.setFreeThrowAttempts(updateAwayBoxScore.getFreeThrowAttempts());
			findAwayBoxScore.setFreeThrowMade(updateAwayBoxScore.getFreeThrowMade());
			findAwayBoxScore.setFreeThrowPercent(updateAwayBoxScore.getFreeThrowPercent());
			findAwayBoxScore.setReboundsOffense(updateAwayBoxScore.getReboundsOffense());
			findAwayBoxScore.setReboundsDefense(updateAwayBoxScore.getReboundsDefense());
			findAwayBoxScore.setPersonalFouls(updateAwayBoxScore.getPersonalFouls());
			findAwayBoxScore.setDaysOff(updateAwayBoxScore.getDaysOff());
			findAwayBoxScore.setBoxScorePlayers(updateAwayBoxScore.getBoxScorePlayers());
			for (int i = 0; i < findAwayBoxScore.getBoxScorePlayers().size(); i++) {
				findAwayBoxScore.getBoxScorePlayers().get(i).setBoxScore(findAwayBoxScore);
			}

			findGame.setStatusCode(StatusCode.Updated);
			getSessionFactory().getCurrentSession().saveOrUpdate(findGame);
		}
		return findGame;
	}

	public Game deleteGame(LocalDate gameDate, String teamKey) {
		Game game = findByDateTeam(gameDate, teamKey);
		if (game.isFound()) {
			getSessionFactory().getCurrentSession().delete(game);
			game = new Game(StatusCode.Deleted);
		}
		return game;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
