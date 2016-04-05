package com.rossotti.basketball.dao.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.PlayerDAO;
import com.rossotti.basketball.dao.model.Player;

@Service
public class PlayerServiceBean {
	@Autowired
	private PlayerDAO playerDAO;

	public Player findByPlayerNameBirthdate(String lastName, String firstName, LocalDate birthdate) {
		return playerDAO.findPlayer(lastName, firstName, birthdate);
	}

	public Player createPlayer(Player player) {
		return playerDAO.createPlayer(player);
	}
}