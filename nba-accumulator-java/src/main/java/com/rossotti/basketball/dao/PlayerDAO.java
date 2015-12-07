package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.models.Player;

public interface PlayerDAO {
	public Player findPlayer(String lastName, String firstName, LocalDate birthDate);
	public List<Player> findPlayers(String lastName, String firstName);
	public void createPlayer(Player player);
	public void updatePlayer(Player player);
	public void deletePlayer(String lastName, String firstName, LocalDate birthDate);
}
