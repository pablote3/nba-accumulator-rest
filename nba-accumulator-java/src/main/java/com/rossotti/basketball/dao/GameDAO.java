package com.rossotti.basketball.dao;

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

import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.BoxScore;
import com.rossotti.basketball.model.BoxScore.Location;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Game.Status;
import com.rossotti.basketball.model.StatusCode;
import com.rossotti.basketball.util.DateTimeUtil;

@Repository
@Transactional
public class GameDAO {
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
		} else {
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
						"and game.status in ('" + Status.Completed + "', '" + Status.Scheduled + "') " + 
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
						"and game.status = '" + Status.Completed + "' " +
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
				.add(Restrictions.eq("status", Status.Scheduled))
				.list();
		return games.size();
	}

	public Game createGame(Game createGame) {
		Game game = findByDateTeam(DateTimeUtil.getLocalDate(createGame.getGameDateTime()), createGame.getBoxScores().get(0).getTeam().getTeamKey());
		if (game.isNotFound()) {
			for (int i = 0; i < createGame.getBoxScores().size(); i++) {
				createGame.getBoxScores().get(i).setGame(createGame);
			}
			getSessionFactory().getCurrentSession().persist(createGame);
			createGame.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return createGame;
	}

	public Game updateGame(Game updateGame) {
		Game findGame = findByDateTeam(DateTimeUtil.getLocalDate(updateGame.getGameDateTime()), updateGame.getBoxScores().get(0).getTeam().getTeamKey());
		if (findGame.isFound()) {
			findGame.setStatus(updateGame.getStatus());

			findGame.setGameOfficials(updateGame.getGameOfficials());
			for (int i = 0; i < findGame.getGameOfficials().size(); i++) {
				findGame.getGameOfficials().get(i).setGame(findGame);
			}

			int findHomeBoxScoreId = findGame.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0;
			BoxScore findHomeBoxScore = findGame.getBoxScores().get(findHomeBoxScoreId);
			int updateHomeBoxScoreId = updateGame.getBoxScores().get(0).getLocation().equals(Location.Home) ? 1 : 0;
			BoxScore updateHomeBoxScore = updateGame.getBoxScores().get(updateHomeBoxScoreId);
			findHomeBoxScore.setMinutes(updateHomeBoxScore.getMinutes());
			findHomeBoxScore.setPoints(updateHomeBoxScore.getPoints());
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
			findHomeBoxScore.setBoxScorePlayers(updateHomeBoxScore.getBoxScorePlayers());
			for (int i = 0; i < findHomeBoxScore.getBoxScorePlayers().size(); i++) {
				findHomeBoxScore.getBoxScorePlayers().get(i).setBoxScore(findHomeBoxScore);
			}

			int findAwayBoxScoreId = findGame.getBoxScores().get(0).getLocation().equals(Location.Away) ? 1 : 0;
			BoxScore findAwayBoxScore = findGame.getBoxScores().get(findAwayBoxScoreId);
			int updateAwayBoxScoreId = updateGame.getBoxScores().get(0).getLocation().equals(Location.Away) ? 1 : 0;
			BoxScore updateAwayBoxScore = updateGame.getBoxScores().get(updateAwayBoxScoreId);
			findAwayBoxScore.setMinutes(updateAwayBoxScore.getMinutes());
			findAwayBoxScore.setPoints(updateAwayBoxScore.getPoints());
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
