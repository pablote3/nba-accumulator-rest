package com.rossotti.basketball.dao.model;

import java.util.List;

import org.joda.time.LocalDate;

public class GameDay {

	private LocalDate gameDate;
	public LocalDate getGameDate() {
		return gameDate;
	}
	public void setGameDate(LocalDate gameDate) {
		this.gameDate = gameDate;
	}

	private StatusCode statusCode;
	public StatusCode getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public enum StatusCode {
		Found,
		Complete,
		Incomplete
	}

	private List<Game> games;
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
	public void add(Game game) {
		games.add(game);
	}

	public String toString() {
		return new StringBuffer()
			.append("  gameDate: " + this.gameDate + "\n")
			.append("  statusCode: " + this.statusCode)
			.toString();
	}
}