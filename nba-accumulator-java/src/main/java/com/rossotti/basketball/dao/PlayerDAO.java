package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.model.Player;

public interface PlayerDAO {
	public Player findPlayer(String lastName, String firstName, LocalDate birthDate);
	public List<Player> findPlayers(String lastName, String firstName);
	public Player createPlayer(Player player);
	public Player updatePlayer(Player player);
	public Player deletePlayer(String lastName, String firstName, LocalDate birthDate);
}
