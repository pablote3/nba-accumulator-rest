package com.rossotti.basketball.dao.impl;

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

import com.rossotti.basketball.dao.GameDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Game.Status;
import com.rossotti.basketball.model.StatusCode;
import com.rossotti.basketball.util.DateTimeUtil;

@Repository
@Transactional
public class GameDAOImpl implements GameDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Game findByDateTeam(LocalDate gameDate, String teamKey) {
		LocalDateTime fromDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime toDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		String sql = 	"select game " +
						"from Game game " +
						"inner join game.boxScores boxScores " +
						"inner join boxScores.team team " +
						"where game.gameDate between '" + fromDateTime + "' and '" + toDateTime +"' " +
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
						"where game.gameDate between '" + fromDateTime + "' and '" + toDateTime +"' " +
						"and game.status in ('" + Status.Completed + "', '" + Status.Scheduled + "') " + 
						"and team.teamKey = '" + teamKey + "' " +
						"order by gameDate asc";
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
				.add(Restrictions.between("gameDate", gameDateTime, maxDateTime))
				.addOrder(Order.asc("gameDate"))
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
	public List<Game> findByDateScheduled(LocalDate gameDate) {
		LocalDateTime minDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime maxDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		List<Game> findGames = getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.between("gameDate", minDateTime, maxDateTime))
				.add(Restrictions.eq("status", Status.Scheduled))
				.addOrder(Order.asc("gameDate"))
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
						"where game.gameDate <= '" + gameDateTime + "' " +
						"and game.status = '" + Status.Completed + "' " +
						"and team.teamKey = '" + teamKey + "' " +
						"order by gameDate desc";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		List<Game> games = query.list();
		
		LocalDateTime lastGameDateTime = null;
		if (games.size() > 0) {
			lastGameDateTime = games.get(0).getGameDate();
		}
		return lastGameDateTime;
	}

	@SuppressWarnings("unchecked")
	public int findCountGamesByDateScheduled(LocalDate gameDate) {
		LocalDateTime fromDateTime = DateTimeUtil.getLocalDateTimeMin(gameDate);
		LocalDateTime toDateTime = DateTimeUtil.getLocalDateTimeMax(gameDate);
		List<Game> games = getSessionFactory().getCurrentSession().createCriteria(Game.class)
				.add(Restrictions.between("gameDate", fromDateTime, toDateTime))
				.add(Restrictions.eq("status", Status.Scheduled))
				.list();
		return games.size();
	}

	@Override
	public Game createGame(Game createGame) {
		Game game = findByDateTeam(DateTimeUtil.getLocalDate(createGame.getGameDate()), createGame.getBoxScores().get(0).getTeam().getTeamKey());
		if (game.isNotFound()) {
			getSessionFactory().getCurrentSession().persist(createGame);
			createGame.setStatusCode(StatusCode.Created);
		}
		else {
			throw new DuplicateEntityException();
		}
		return createGame;
	}

//	@Override
//	public Game updateGame(Game updateGame) {
//		Long id = findIdByDateTeam(DateTimeUtil.getLocalDate(updateGame.getGameDate()), updateGame.getBoxScores().get(0).getTeam().getTeamKey());
//		if (id > 0L) {
//			Game game = findById(id);
//			if (game.isFound()) {
//				game.setStatus(updateGame.getStatus());
//				for (int i = 0; i < game.getBoxScores().size(); i++) {
//					if (game.getBoxScores().get(i).getLocation()) {
//						
//					}
//				}
//				
//				boxScore.setMinutes(stats.getMinutes());
//		        boxScore.setPoints(stats.getPoints());
//		        boxScore.setAssists(stats.getAssists());
//		        boxScore.setTurnovers(stats.getTurnovers());
//		        boxScore.setSteals(stats.getSteals());
//		        boxScore.setBlocks(stats.getBlocks());
//		        boxScore.setFieldGoalAttempts(stats.getFieldGoalAttempts());
//		        boxScore.setFieldGoalMade(stats.getFieldGoalMade());
//		        boxScore.setFieldGoalPercent(stats.getFieldGoalPercent());
//		        boxScore.setThreePointAttempts(stats.getThreePointAttempts());
//		        boxScore.setThreePointMade(stats.getThreePointMade());
//		        boxScore.setThreePointPercent(stats.getThreePointPercent());
//		        boxScore.setFreeThrowAttempts(stats.getFreeThrowAttempts());
//		        boxScore.setFreeThrowMade(stats.getFreeThrowMade());
//		        boxScore.setFreeThrowPercent(stats.getFreeThrowPercent());
//		        boxScore.setReboundsOffense(stats.getReboundsOffense());
//		        boxScore.setReboundsDefense(stats.getReboundsDefense());
//		        boxScore.setPersonalFouls(stats.getPersonalFouls());
//
//				game.setStatusCode(StatusCode.Updated);
//				getSessionFactory().getCurrentSession().persist(game);
//			}
//		}
//		return game;
//	}

//	@Override
//	public Game deleteGame(LocalDate gameDate, String teamKey) {
//		Game game = findGame(lastName, firstName, birthdate);
//		if (game.isFound()) {
//			getSessionFactory().getCurrentSession().delete(game);
//			game = new Game(StatusCode.Deleted);
//		}
//		return game;
//	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
