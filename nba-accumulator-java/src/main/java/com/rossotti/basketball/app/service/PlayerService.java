package com.rossotti.basketball.app.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rossotti.basketball.dao.model.Player;
import com.rossotti.basketball.dao.repository.PlayerRepository;

@Service
public class PlayerService {
	private final PlayerRepository playerRepo;

	@Autowired
	public PlayerService(PlayerRepository playerRepo) {
		this.playerRepo = playerRepo;
	}

	public Player findByPlayerNameBirthdate(String lastName, String firstName, LocalDate birthdate) {
		return playerRepo.findPlayer(lastName, firstName, birthdate);
	}

	public Player createPlayer(Player player) {
		return playerRepo.createPlayer(player);
	}
}