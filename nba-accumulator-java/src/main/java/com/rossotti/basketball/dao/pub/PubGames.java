package com.rossotti.basketball.dao.pub;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PubGames {
	private final URI self;
	private final List<PubGame> games = new ArrayList<>();

	@JsonCreator
	public PubGames(@JsonProperty("self") URI self, 
					@JsonProperty("games") List<PubGame> games) {
		this.self = self;
		if (games != null) {
			this.games.addAll(games);
		}
	}

	public URI getSelf() {
		return self;
	}
	public List<PubGame> getGames() {
		return games;
	}
}
