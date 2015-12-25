package com.rossotti.basketball.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.GameDAO;
import com.rossotti.basketball.dao.exception.DuplicateEntityException;
import com.rossotti.basketball.model.Game;
import com.rossotti.basketball.model.Player;
import com.rossotti.basketball.model.StatusCode;

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
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String stringDate = gameDate.toString(fmt);
		LocalDateTime fromTime = LocalDateTime.parse(stringDate + "T00:00");
		LocalDateTime toTime = LocalDateTime.parse(stringDate + "T23:59");
		String sql = 	"select game " +
						"from Game game " +
//						"left join game.boxScores boxScore " +
//						"inner join boxScore.team team " +
						"where game.gameDate between '" + fromTime + "' and '" + toTime +"'";
//						"where team.teamKey = '" + teamKey + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		Game game = (Game)query.uniqueResult();
		return game.getId();
	}
	
//	@Override
//	public Game findGame(LocalDate gameDate, String teamKey) {
////		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
////		String date = gameDate.toString(fmt);
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
