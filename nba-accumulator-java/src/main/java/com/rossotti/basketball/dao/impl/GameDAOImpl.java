package com.rossotti.basketball.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.GameDAO;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.StatusCode;
import com.rossotti.basketball.util.DateTimeUtil;

@Repository
@Transactional
public class GameDAOImpl implements GameDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Game findById(Long id) {
		Game game = (Game)getSessionFactory().getCurrentSession().createCriteria(Game.class)
			.add(Restrictions.eq("id", id))
			.uniqueResult();
		if (game == null) {
			game = new Game(StatusCode.NotFound);
		}
		return game;
	}
	
	public Long findIdByDateTeam(LocalDate gameDate, String teamKey) {
		String stringDate = DateTimeUtil.getStringDate(gameDate);
		LocalDateTime fromTime = DateTimeUtil.getLocalDateTimeMin(stringDate);
		LocalDateTime toTime = DateTimeUtil.getLocalDateTimeMax(stringDate);
		String sql = 	"select game " +
						"from Game game " +
						"left join game.boxScores boxScores " +
						"inner join boxScores.team team " +
						"where game.gameDate between '" + fromTime + "' and '" + toTime +"' " +
						"and team.teamKey = '" + teamKey + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		Game game = (Game)query.uniqueResult();
		if (game == null) {
			return 0L;
		} else {
			return game.getId();
		}
	}

//	public List<Long> findIdsByDateRangeSize(String propDate, String propSize) {
//		Query<Game> query = null;
//		int maxRows = Integer.parseInt(propSize);
//		if (maxRows > 0)
//			query.setMaxRows(maxRows);
//
//		LocalDate maxDate = DateTimeUtil.getDateMaxSeason(DateTimeUtil.createDateFromStringDate(propDate));
//
//		query.where().between("date", propDate, maxDate);
//		query.orderBy("t0.date asc");
//		List<Game> games = query.findList();
//
//		List<Long> gameIds = null;
//		if (games.size() > 0) {
//			gameIds = new ArrayList<Long>();
//			for (int i = 0; i < games.size(); i++) {
//				gameIds.add(games.get(i).getId());
//			}
//		}
//		return gameIds;
//	}

//	@Override
//	public Game findGame(LocalDate gameDate, String teamKey) {
//		String date = DateTimeUtil.getStringDate(gameDate);
//		
//		String sql = 	"select game " +
//				"from Game game " +
//				"inner join game.boxScores boxScore " +
//				"inner join boxScore.team team " +
//				"where game.date >= '" + gameDate + "' " +
////				"and game.date <= '" + gameDate + "' " +
//				"and boxScore.team.teamKey = '" + teamKey + "'";
//		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
//		Game game = (Game)query.uniqueResult();
//		if (game == null) {
//			game = new Game(StatusCode.NotFound);
//		}
//		return game;
//	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Game> findGames(LocalDate gameDate) {
//		List<Game> games = getSessionFactory().getCurrentSession().createCriteria(Game.class)
//			.add(Restrictions.eq("lastName", lastName))
//			.add(Restrictions.eq("firstName", firstName))
//			.list();
//		if (games == null) {
//			games = new ArrayList<Game>();
//		}
//		return games;
//	}
//
//	@Override
//	public Game createGame(Game createGame) {
//		Game game = findGame(createGame.getLastName(), createGame.getFirstName(), createGame.getBirthdate());
//		if (game.isNotFound()) {
//			getSessionFactory().getCurrentSession().persist(createGame);
//			createGame.setStatusCode(StatusCode.Created);
//		}
//		else {
//			throw new DuplicateEntityException();
//		}
//		return createGame;
//	}
//
//	@Override
//	public Game updateGame(Game updateGame) {
//		Game game = findGame(updateGame.getLastName(), updateGame.getFirstName(), updateGame.getBirthdate());
//		if (game.isFound()) {
//			game.setLastName(updateGame.getLastName());
//			game.setFirstName(updateGame.getFirstName());
//			game.setBirthdate(updateGame.getBirthdate());
//			game.setDisplayName(updateGame.getDisplayName());
//			game.setHeight(updateGame.getHeight());
//			game.setWeight(updateGame.getWeight());
//			game.setBirthplace(updateGame.getBirthplace());
//			game.setStatusCode(StatusCode.Updated);
//			getSessionFactory().getCurrentSession().persist(game);
//		}
//		return game;
//	}
//
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
