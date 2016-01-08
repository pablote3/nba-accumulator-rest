package com.rossotti.basketball.dao;

import java.util.List;

import org.joda.time.LocalDate;

import com.rossotti.basketball.model.RosterPlayer;

public interface RosterPlayerDAO {
	public RosterPlayer findRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate);
	public RosterPlayer findRosterPlayer(String lastName, String firstName, String teamKey, LocalDate asOfDate);
	public List<RosterPlayer> findRosterPlayers(String lastName, String firstName, LocalDate birthdate);
	public List<RosterPlayer> findRosterPlayers(String teamKey, LocalDate asOfDate);
	public RosterPlayer createRosterPlayer(RosterPlayer rosterPlayer);
	public RosterPlayer updateRosterPlayer(RosterPlayer rosterPlayer);
	public RosterPlayer deleteRosterPlayer(String lastName, String firstName, LocalDate birthdate, LocalDate asOfDate);
}
