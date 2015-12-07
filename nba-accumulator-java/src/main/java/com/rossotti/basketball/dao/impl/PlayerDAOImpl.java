package com.rossotti.basketball.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rossotti.basketball.dao.PlayerDAO;
import com.rossotti.basketball.dao.exceptions.DuplicateEntityException;
import com.rossotti.basketball.dao.exceptions.NoSuchEntityException;
import com.rossotti.basketball.models.Player;

@Repository
@Transactional
public class PlayerDAOImpl implements PlayerDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Player findPlayer(String lastName, String firstName, LocalDate birthDate) {
		Player player = (Player)getSessionFactory().getCurrentSession().createCriteria(Player.class)
			.add(Restrictions.eq("lastName", lastName))
			.add(Restrictions.eq("firstName", firstName))
			.add(Restrictions.eq("birthDate", birthDate))
			.uniqueResult();
		if (player == null) {
			throw new NoSuchEntityException();
		}
		return player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> findPlayers(String lastName, String firstName) {
		List<Player> players = getSessionFactory().getCurrentSession().createCriteria(Player.class)
			.add(Restrictions.eq("lastName", lastName))
			.add(Restrictions.eq("firstName", firstName))
			.list();
		if (players == null || players.size() == 0) {
			throw new NoSuchEntityException();
		}
		return players;
	}

	@Override
	public void createPlayer(Player createPlayer) {
		Player player = (Player)getSessionFactory().getCurrentSession().createCriteria(Player.class)
				.add(Restrictions.eq("lastName", createPlayer.getLastName()))
				.add(Restrictions.eq("firstName", createPlayer.getFirstName()))
				.add(Restrictions.eq("birthDate", createPlayer.getBirthDate()))
				.uniqueResult();
		if (player == null) {
			getSessionFactory().getCurrentSession().persist(createPlayer);
		}
		else {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public void updatePlayer(Player updatePlayer) {
		Player player = findPlayer(updatePlayer.getLastName(), updatePlayer.getFirstName(), updatePlayer.getBirthDate());
		player.setLastName(updatePlayer.getLastName());
		player.setFirstName(updatePlayer.getFirstName());
		player.setBirthDate(updatePlayer.getBirthDate());
		player.setDisplayName(updatePlayer.getDisplayName());
		player.setHeight(updatePlayer.getHeight());
		player.setWeight(updatePlayer.getWeight());
		player.setBirthPlace(updatePlayer.getBirthPlace());
		getSessionFactory().getCurrentSession().persist(player);
	}

	@Override
	public void deletePlayer(String lastName, String firstName, LocalDate birthDate) {
		Player player = findPlayer(lastName, firstName, birthDate);
		getSessionFactory().getCurrentSession().delete(player);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
